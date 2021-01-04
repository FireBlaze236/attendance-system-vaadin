package com.packagename.prototype1.backend.facerec;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Base64;

import static org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_UNCHANGED;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.opencv.core.CvType.CV_32SC1;
import static org.opencv.imgproc.Imgproc.*;
import nu.pattern.OpenCV;

public class FaceRecognizerModule {

    public int compareimage (String str) throws IOException {
        OpenCV.loadLocally();
        CascadeClassifier faceDetector = new CascadeClassifier("haarcascade_frontalface_alt.xml");
        FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();
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

            int type = 0;
            if (faceCaptured.channels() == 1) {
                type = BufferedImage.TYPE_BYTE_GRAY;
            } else if (faceCaptured.channels() == 3) {
                type = BufferedImage.TYPE_3BYTE_BGR;
            }
            BufferedImage image = new BufferedImage(faceCaptured.width() ,faceCaptured.height(), type);
            WritableRaster raster = image.getRaster();
            DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
            byte[] data2 = dataBuffer.getData();
            faceCaptured.get(0, 0, data2);

            OpenCVFrameConverter.ToMat cv = new OpenCVFrameConverter.ToMat();
            org.bytedeco.opencv.opencv_core.Mat bufface =  cv.convertToMat(new Java2DFrameConverter().convert(image));

            faceRecognizer.read("mymodel.xml");
            faceRecognizer.predict (bufface, label, confidence);

            prediction = label.get(0);
        }
        return prediction;
    }

    public void take_in_Vector(String str,int id, int count) throws IOException {
        MatVector images = new MatVector(1);
        org.bytedeco.opencv.opencv_core.Mat labels = new org.bytedeco.opencv.opencv_core.Mat(1, 1, CV_32SC1);
        IntBuffer labelsBuf = labels.createBuffer();
        OpenCV.loadLocally();
        CascadeClassifier faceDetector = new CascadeClassifier("haarcascade_frontalface_alt.xml");

        byte[] data = Base64.getDecoder().decode(
                str.substring(str.indexOf(",") + 1).getBytes()
        );
        org.opencv.core.Mat imgFrame = Imgcodecs.imdecode(new MatOfByte(data), IMREAD_UNCHANGED);

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(imgFrame, faceDetections);

        for (Rect rect : faceDetections.toArray()) {
            //System.out.println("fff");

            rectangle(imgFrame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 0));
            Rect face = new Rect (rect.x, rect.y, rect.width, rect.height);

            org.opencv.core.Mat faceCaptured = new org.opencv.core.Mat(imgFrame, face);
            cvtColor(faceCaptured, faceCaptured, Imgproc.COLOR_BGRA2GRAY);
            Imgproc.resize(faceCaptured, faceCaptured, new org.opencv.core.Size(160, 160));

            org.bytedeco.opencv.opencv_core.Mat mat2 = new org.bytedeco.opencv.opencv_core.Mat() { { address = faceCaptured.getNativeObjAddr(); } };

            //imwrite(String.format("%d+input.jpg", count), mat2);
            images.put(count, mat2);
            labelsBuf.put(count, id);
        }
        FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();
        faceRecognizer.train(images, labels);
        faceRecognizer.save("mymodel.xml");
    }
}
