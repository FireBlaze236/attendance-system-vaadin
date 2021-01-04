package com.packagename.prototype1.backend.facerec;

import nu.pattern.OpenCV;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Base64;

import static org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_UNCHANGED;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.opencv.core.CvType.CV_32SC1;
import static org.opencv.imgproc.Imgproc.*;

public class FaceRecognizerModule {

    public static FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();
    public static MatVector images = new MatVector(1);
    //List<org.bytedeco.opencv.opencv_core.Mat> images = new ArrayList<org.bytedeco.opencv.opencv_core.Mat>();
    public static org.bytedeco.opencv.opencv_core.Mat labels = new org.bytedeco.opencv.opencv_core.Mat(1, 1, CV_32SC1);
    public static IntBuffer labelsBuf = labels.createBuffer();
    //My code
    public static void init()
    {
        OpenCV.loadLocally();
        faceRecognizer.read("mymodel.xml");
    }


    public static int compareimage (String str) throws IOException {
        OpenCV.loadLocally();
        CascadeClassifier faceDetector = new CascadeClassifier("haarcascade_frontalface_alt.xml");
        //FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();
        byte[] data = Base64.getDecoder().decode(
                str.substring(str.indexOf(",") + 1).getBytes()
        );
        Mat imgFrame = Imgcodecs.imdecode(new MatOfByte(data), IMREAD_UNCHANGED);

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(imgFrame, faceDetections);

        int prediction = -1;

        for (Rect rect : faceDetections.toArray()) {
            System.out.println("ttt");

            rectangle(imgFrame, rect, new Scalar(0, 0, 0, 0));

            Mat faceCaptured = new Mat(imgFrame, rect);
            cvtColor(faceCaptured, faceCaptured, Imgproc.COLOR_BGRA2GRAY);
            resize(faceCaptured, faceCaptured, new Size(160, 160));

            IntPointer label = new IntPointer(1);
            DoublePointer confidence = new DoublePointer(1);

            org.bytedeco.opencv.opencv_core.Mat bufface = new org.bytedeco.opencv.opencv_core.Mat() { { address = faceCaptured.getNativeObjAddr(); } };

            //faceRecognizer.read("mymodel.xml");

            faceRecognizer.predict (bufface, label, confidence);

            prediction = label.get(0);
            double x = confidence.get(0);
            System.out.println(x);
        }
        return prediction;
    }

    public static void take_in_Vector(String str,int id, int count) throws IOException {
        count = 0;
        OpenCV.loadLocally();
        CascadeClassifier faceDetector = new CascadeClassifier("haarcascade_frontalface_alt.xml");

        byte[] data = Base64.getDecoder().decode(
                str.substring(str.indexOf(",") + 1).getBytes()
        );
        org.opencv.core.Mat imgFrame = Imgcodecs.imdecode(new MatOfByte(data), IMREAD_UNCHANGED);

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(imgFrame, faceDetections);

        if (faceDetections.toList().isEmpty()) {
            System.out.println("Training failed. No faces.");
            return;
        }
        for (Rect rect : faceDetections.toArray()) {
            rectangle(imgFrame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 0));
            Rect face = new Rect (rect.x, rect.y, rect.width, rect.height);

            org.opencv.core.Mat faceCaptured = new org.opencv.core.Mat(imgFrame, face);
            cvtColor(faceCaptured, faceCaptured, Imgproc.COLOR_BGRA2GRAY);
            Imgproc.resize(faceCaptured, faceCaptured, new org.opencv.core.Size(160, 160));

            org.bytedeco.opencv.opencv_core.Mat mat2 = new org.bytedeco.opencv.opencv_core.Mat() { { address = faceCaptured.getNativeObjAddr(); } };

            imwrite(String.format("%d+input.jpg", count), mat2);
            images.put(count,mat2);
            labelsBuf.put(count,id);
            break;
        }
        faceRecognizer.update(images, labels);
        faceRecognizer.write("mymodel.xml");
    }
}
