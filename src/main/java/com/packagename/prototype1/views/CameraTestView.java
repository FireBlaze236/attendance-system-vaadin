package com.packagename.prototype1.views;

import com.packagename.prototype1.VideoComponent;
import com.packagename.prototype1.backend.facerec.FaceRecognizerModule;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.bytedeco.javacpp.BytePointer;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;

import static com.packagename.prototype1.backend.facerec.FaceRecognizerModule.train;

@Route("camera")
public class CameraTestView extends VerticalLayout {
    private Button cameraButton = new Button("Access Camera");
    private Button snapButton = new Button("Predict");
    private Button trainButton = new Button("Train");
    private VideoComponent vComponent = new VideoComponent();
    private int id = 101;
    private Text tm = new Text("Training Done");

    public CameraTestView() {
        /* 
         * JavaScript Snippets:
         * jsinit = initializes media device and resizes canvas
         * jstakepic = retrieves image data url from canvas
         * $0 = video element, $1 = canvas element
         */
        String jsinit = "if (navigator.mediaDevices.getUserMedia) { " +
        "	navigator.mediaDevices.getUserMedia({ video: true })" +
        "		.then( function(stream) { $0.srcObject = stream; }); " +
        "}" +
        "$0.addEventListener('canplay', function(ev) {" +
        "$1.setAttribute('width', $0.videoWidth);" +
        "$1.setAttribute('height', $0.videoHeight);" +
        "}, false);";
        String jstakepic = "var context = $1.getContext('2d');" +
        "context.drawImage($0, 0, 0, $1.width, $1.height);" +
        "return $1.toDataURL('image/png');";
        
        add(cameraButton);
        cameraButton.addClickListener(cl -> {
            add(snapButton);
            add(trainButton);
            add(vComponent);
            UI.getCurrent().getPage().executeJs(
                jsinit,
                vComponent.getElement().getChild(0),
                vComponent.getElement().getChild(1)
            );
            //fr.train();
            //System.out.println("Model Trained");
        });


        snapButton.addClickListener(c -> {
            FaceRecognizerModule fr = new FaceRecognizerModule();
            PendingJavaScriptResult res = UI.getCurrent().getPage().executeJs(
                jstakepic,
                vComponent.getElement().getChild(0),
                vComponent.getElement().getChild(1)
            );
            res.then(String.class, dataURL -> {
                Image snappedImage = new Image(dataURL, "snapshot");
                //add (snappedImage);
                String str = dataURL;
                int level = -1;
                try {
                    level = fr.compareimage (str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(level);
                Text tf = new Text(String.valueOf(level));
                add (tf);
            });
        });

        trainButton.addClickListener(c -> {
            FaceRecognizerModule fr = new FaceRecognizerModule();
            for (int i = 0; i < 10; i++) {
                PendingJavaScriptResult res = UI.getCurrent().getPage().executeJs(
                        jstakepic,
                        vComponent.getElement().getChild(0),
                        vComponent.getElement().getChild(1)
                );
                int temp = i;
                res.then(String.class, dataURL -> {
                    String str = dataURL;
                    try {
                        fr.take_in_Vector(str, id, temp);
                    } catch (IOException e) {

                    }
                });
            }
            train();
            id++;
            add(tm);
        });
    }
}
