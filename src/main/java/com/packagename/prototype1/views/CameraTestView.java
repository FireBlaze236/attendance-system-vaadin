package com.packagename.prototype1.views;

import com.packagename.prototype1.VideoComponent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.router.Route;

@Route("camera")
public class CameraTestView extends VerticalLayout {
    private Button cameraButton = new Button("Access Camera");
    private Button snapButton = new Button("Snap");
    private VideoComponent vComponent = new VideoComponent();
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
            add(vComponent);
            UI.getCurrent().getPage().executeJs(
                jsinit,
                vComponent.getElement().getChild(0),
                vComponent.getElement().getChild(1)
            );
        });

        snapButton.addClickListener(c -> {
            PendingJavaScriptResult res = UI.getCurrent().getPage().executeJs(
                jstakepic,
                vComponent.getElement().getChild(0),
                vComponent.getElement().getChild(1)
            );
            res.then(String.class, dataURL -> {
                Image snappedImage = new Image(dataURL, "snapshot");
                add(snappedImage);
            });
        });
    }
}
