package com.packagename.prototype1;

import java.util.Base64;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;


public class FaceDetector {
    CascadeClassifier faceDetector = new CascadeClassifier("haarcascade_frontalface_alt.xml");
    Mat imgFrame = new Mat();
    private void setFrame(String dataURL) {
        byte[] data = Base64.getDecoder().decode(
            dataURL.substring(dataURL.indexOf(",") + 1).getBytes()
        );
        imgFrame = Imgcodecs.imdecode(new MatOfByte(data), Imgcodecs.IMREAD_UNCHANGED);
    }
    public boolean detect(String dataURL) {
        setFrame(dataURL);
        MatOfRect detections = new MatOfRect();
        faceDetector.detectMultiScale(imgFrame, detections);
        return (detections.toArray().length != 0);
    }
}
