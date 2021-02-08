package com.packagename.prototype1;

import java.util.concurrent.CompletableFuture;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;

@Tag("div")
public class VideoComponent extends Component {
    @DomEvent("snapshot")
    static public class ImageSnapEvent extends ComponentEvent<VideoComponent> {
        public ImageSnapEvent(VideoComponent source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    Element videoElement = new Element("video");
    Element canvasElement = new Element("canvas");

    //String imgDataURL = new String();
    Integer intervalID = null;

    public VideoComponent() {
        videoElement.setProperty("autoplay", true);
        //canvasElement.getStyle().set("display", "none");
        //getElement().getStyle().set("display", "none");
        getElement().appendChild(
            videoElement,
            canvasElement
        );
        String jsinit = "if (navigator.mediaDevices.getUserMedia) { " +
        "	navigator.mediaDevices.getUserMedia({ video: true })" +
        "		.then( function(stream) { $0.srcObject = stream; }); " +
        "}" +
        "$0.addEventListener('canplay', function(ev) {" +
        "$1.setAttribute('width', $0.videoWidth);" +
        "$1.setAttribute('height', $0.videoHeight);" +
        "}, false);";
        addAttachListener(cl -> {
            UI.getCurrent().getPage().executeJs(
                jsinit,
                videoElement,
                canvasElement,
                getElement()
            );
        });
    }
    /*
    public String getImageDataURL() {
        CompletableFuture<String> result = new CompletableFuture<>();
        UI.getCurrent().access( () -> {
            canvasElement.executeJs(
                "var context = this.getContext('2d');" +
                "context.drawImage($0, 0, 0, this.width, this.height);" +
                "return this.toDataURL();",
                videoElement
            ).then(String.class, r -> { result.complete(r); });
        } );
        try {
            return result.get();
        }
        catch (Exception e) {
            // TODO: Handle Error
            return "";
        }
    }
    */

    /**
     * Start the interval snapping for taking pics in timed intervals
     * @param timems time in ms
     */
    public void startIntervalSnap(int timems) {
        if (intervalID == null) {
            UI.getCurrent().getPage().executeJs(
                "const ev = new Event('snapshot');" +
                "var intervalID = setInterval(function() {$0.dispatchEvent(ev)}, $1);" +
                "return intervalID",
                getElement(),
                timems
            ).then(Integer.class, id -> { intervalID = id; });
        }
    }

    /**
     * Stops the interval snapping
     */
    public void stopIntervalSnap() {
        if (intervalID != null) {
            UI.getCurrent().getPage().executeJs(
                "clearInterval($0);", 
                intervalID
            );
            intervalID = null;
        }
    }

    /**
     * Take snap
     */
    public void singleSnap() {
        UI.getCurrent().getPage().executeJs("const ev = new Event('snapshot'); $0.dispatchEvent(ev);", getElement());
        //fireEvent(new ImageSnapEvent(this, false));
    }

    /**
     * Check for taken snaps
     * @param listener
     * @return
     */
    public Registration addSnapshotListener(ComponentEventListener<ImageSnapEvent> listener) {
        return addListener(ImageSnapEvent.class, listener);
    }
}
