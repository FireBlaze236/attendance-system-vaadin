package com.packagename.prototype1.views;

import com.packagename.prototype1.VideoComponent;
import com.packagename.prototype1.backend.facerec.FaceRecognizerModule;
import com.packagename.prototype1.backend.model.UserData;
import com.packagename.prototype1.backend.repository.UserRepository;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@Route("camera")
public class CameraTestView extends VerticalLayout {

    @Autowired
    UserRepository userRepository;

    private Button cameraButton = new Button("Access Camera");
    private Button snapButton = new Button("Predict");
    private Button trainButton = new Button("Train");
    private IntegerField idField = new IntegerField("Id: ");
    private VideoComponent vComponent = new VideoComponent();
    private int id = 101;
    private Text tm = new Text("");

    public CameraTestView() {
        //FaceRecognizerModule.init();
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
            add(idField);
            add(trainButton);
            add(tm);
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
                    level = FaceRecognizerModule.compareimage (str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(level);
                //new code
                if(level == -1 || !userRepository.findById((long)level).isPresent())
                {
                    tm.setText("Face not recognized");
                }
                else
                {
                    UserData user = userRepository.findById((long) level).get();
                    tm.setText(user.getUsername());
                }
            });
        });

        trainButton.addClickListener(c -> {
            tm.setText("Trainining Initiated!");

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if (userRepository.findByUsername(username).isPresent())
            {
                UserData user = userRepository.findByUsername(username).get();
                id = user.getId().intValue();
            }
            else
            {
                id = -1;
            }

            for (int i = 0; i < 6; i++) {
                PendingJavaScriptResult res = UI.getCurrent().getPage().executeJs(
                        jstakepic,
                        vComponent.getElement().getChild(0),
                        vComponent.getElement().getChild(1)
                );
                int temp = i;


                res.then(String.class, dataURL -> {
                    String str = dataURL;
                    try {
                        FaceRecognizerModule.take_in_Vector(str, id, temp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    tm.setText("Training Done");
                });

            }

        });

    }
}
