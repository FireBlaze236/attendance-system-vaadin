package com.packagename.prototype1.backend.facerec;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.DoublePointer;

import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_face.*;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

public class FaceRecognizerModule {
    Size scaleSize = new Size(300,200);

    public String compareimage (String str) throws IOException {

        byte[] imagedata = java.util.Base64.getDecoder().decode(str.substring(str.indexOf(",") + 1));
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagedata));
        ImageIO.write(bufferedImage, "png", new File("img.png"));

        String trainingDir = "training_image_dir"; // where the ref images are
        Mat testImage = imread("img.png", IMREAD_GRAYSCALE); // the image you want to check

        Mat resizedimage = new Mat();
        resize(testImage, resizedimage, scaleSize , 0, 0, INTER_AREA);
        FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();
        faceRecognizer.read("mymodel.xml");

        IntPointer label = new IntPointer(1);
        DoublePointer confidence = new DoublePointer(1);
        faceRecognizer.predict(resizedimage, label, confidence);

        String predictedLabel = String.valueOf(label.get(0));
        String predictedLabeldbl = String.valueOf(label.get(1));
        System.out.println(predictedLabeldbl);

        return predictedLabel;
    }

    public void train ()
    {

//        BytePointer b = new BytePointer(bt);
//        Mat testImage = imread(b, IMREAD_GRAYSCALE);
//        Mat resizedimage = new Mat();
//        resize(testImage, resizedimage, scaleSize , 0, 0, INTER_AREA);
//
//        MatVector images = new MatVector(resizedimage);
//
//        Mat labels = new Mat(1, id, CV_32SC1);
        String trainingDir = "training_image_dir"; // where the ref images are
        //Mat testImage = imread("Predict_image\\check.png", IMREAD_GRAYSCALE); // the image you want to check

        //Mat resizedimage = new Mat();
        Size scaleSize = new Size(300,200);
        //resize(testImage, resizedimage, scaleSize , 0, 0, INTER_AREA);

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

        FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();

        faceRecognizer.train(images, labels);
        faceRecognizer.save("mymodel.xml");
    }
}
