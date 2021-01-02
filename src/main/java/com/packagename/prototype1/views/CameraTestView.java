package com.packagename.prototype1.views;

import com.packagename.prototype1.VideoComponent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("camera")
public class CameraTestView extends VerticalLayout {
    private Button cameraButton = new Button("Access Camera");
    private Button snapButton = new Button("Snap");
    private VideoComponent vComponent = new VideoComponent();
    public CameraTestView() {
        String jsinit = "if (navigator.mediaDevices.getUserMedia) { " +
        "	navigator.mediaDevices.getUserMedia({ video: true })" +
        "		.then( function(stream) { $0.srcObject = stream; }); " +
        "}";

        String jstakepic = "var context = $1.getContext('2d');" +
        "context.drawImage($0, 0, 0, $1.width, $1.height);" +
        "var data = $1.toDataURL('image/png');" +
        "$2.setAttribute('src', data); console.log(data)";
        
        add(cameraButton);
        cameraButton.addClickListener(cl -> {
            add(snapButton);
            add(vComponent);
            // $0 = video, $1 = canvas, $2 = image
            UI.getCurrent().getPage().executeJs(
                jsinit,
                vComponent.getElement().getChild(0),
                vComponent.getElement().getChild(1),
                vComponent.getElement().getChild(2)
            );
        });
        snapButton.addClickListener(c -> {
            UI.getCurrent().getPage().executeJs(
                jstakepic,
                vComponent.getElement().getChild(0),
                vComponent.getElement().getChild(1),
                vComponent.getElement().getChild(2)
            );
        });
    }
}
