package com.packagename.prototype1.backend.facerec;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.DoublePointer;

import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_face.*;
import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

public class Compare {
    public static void main(String[] args) {
        String trainingDir = "training_image_dir"; // where the ref images are
        Mat testImage = imread("Predict_image\\check.png", IMREAD_GRAYSCALE); // the image you want to check

        Mat resizedimage = new Mat();
        Size scaleSize = new Size(300,200);
        resize(testImage, resizedimage, scaleSize , 0, 0, INTER_AREA);

        File root = new File(trainingDir);

        FilenameFilter imgFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");
            }
        };

        File[] imageFiles = root.listFiles(imgFilter);

        MatVector images = new MatVector(imageFiles.length);

        Mat labels = new Mat(imageFiles.length, 1, CV_32SC1);
        IntBuffer labelsBuf = labels.createBuffer();

        int counter = 0;

        for (File image : imageFiles) {
            Mat img = imread(image.getAbsolutePath(), IMREAD_GRAYSCALE);
            Mat resizeimage = new Mat();
            resize(img, resizeimage, scaleSize , 0, 0, INTER_AREA);

            int label = Integer.parseInt(image.getName().split("\\-")[0]);

            images.put(counter, resizeimage);

            labelsBuf.put(counter, label);

            counter++;
        }

        // FaceRecognizer faceRecognizer = FisherFaceRecognizer.create();
        // FaceRecognizer faceRecognizer = EigenFaceRecognizer.create();
        FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();

        // If you have new face to add in the model then uncomment next 2 line
        //faceRecognizer.train(images, labels);
        //faceRecognizer.save("mymodel.xml");

        // If you want to use exinting model then use next line other wise comment it
        faceRecognizer.read("mymodel.xml");

        IntPointer label = new IntPointer(1);
        DoublePointer confidence = new DoublePointer(1);
        faceRecognizer.predict(resizedimage, label, confidence);
        int predictedLabel = label.get(0);

        System.out.println("Predicted label: " + predictedLabel);
    }
}
