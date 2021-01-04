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
    private VideoComponent vComponent = new VideoComponent();
    public CameraTestView() {
        /* 
         * $0 = video element
         * $1 = canvas element
         * $2 = div element
         * $3 = timeout
         */
        String jsinit = "if (navigator.mediaDevices.getUserMedia) { " +
        "	navigator.mediaDevices.getUserMedia({ video: true })" +
        "		.then( function(stream) { $0.srcObject = stream; }); " +
        "}" +
        "$0.addEventListener('canplay', function(ev) {" +
        "$1.setAttribute('width', $0.videoWidth);" +
        "$1.setAttribute('height', $0.videoHeight);" +
        "}, false);" +
        "const snap = new Event('snapshot');" +
        "function takepic() {" +
        "var context = $1.getContext('2d');" +
        "context.drawImage($0, 0, 0, $1.width, $1.height);" +
        "$2.dispatchEvent(snap);" +
        "}" +
        "setInterval(takepic, $3);";

        String jstakepic = "return $0.toDataURL();";
        
        add(vComponent);
        Integer timeoutDurationms = 2000;
        
        vComponent.addAttachListener(cl -> {
            UI.getCurrent().getPage().executeJs(
                jsinit,
                vComponent.getElement().getChild(0),
                vComponent.getElement().getChild(1),
                vComponent.getElement(),
                timeoutDurationms
            );
        });

        vComponent.addSnapshotListener(cl -> {
            PendingJavaScriptResult res = UI.getCurrent().getPage().executeJs(
                jstakepic,
                vComponent.getElement().getChild(1)
            );
            res.then(String.class, dataURL -> {
                Image img = new Image(dataURL, "snapshot");
                add(img);
            });
        });

        //vComponent.setVisible(false);
    }
}
