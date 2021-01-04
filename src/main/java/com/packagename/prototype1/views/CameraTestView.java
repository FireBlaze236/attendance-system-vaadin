package com.packagename.prototype1.views;

import com.packagename.prototype1.FaceDetector;
import com.packagename.prototype1.VideoComponent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.router.Route;

@Route("camera")
public class CameraTestView extends VerticalLayout {
    private VideoComponent vComponent = new VideoComponent();
    private FaceDetector fd = new FaceDetector();
    private Button snapStart = new Button("start");
    private Button snapEnd = new Button("end");
    private Button snapSingle = new Button("single");
    public CameraTestView() {
        Image img = new Image();
        add(vComponent);
        add(img);

        add(snapStart, snapEnd, snapSingle);

        snapStart.addClickListener(cl -> {
            vComponent.startIntervalSnap(2000);
        });
        snapEnd.addClickListener(cl -> {
            vComponent.stopIntervalSnap();
        });
        snapSingle.addClickListener(cl -> {
            vComponent.singleSnap();
        });

        vComponent.addSnapshotListener(cl -> {
            vComponent.getElement().getChild(1).executeJs(
                "var context = this.getContext('2d');" +
                "context.drawImage($0, 0, 0, this.width, this.height);" +
                "return this.toDataURL();",
                vComponent.getElement().getChild(0)
            ).then(String.class, dataURL -> {
                img.setSrc(dataURL);
            });
            //Image img = new Image(dataURL, "snapshot");
            /*
            boolean faceExists = fd.detect(dataURL);
            if (faceExists) { add(new H1("face")); }
            else { add(new H1("noface")); }
            */
        });

        //vComponent.setVisible(false);
    }
}
