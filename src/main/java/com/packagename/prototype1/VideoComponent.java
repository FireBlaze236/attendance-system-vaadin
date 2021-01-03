package com.packagename.prototype1;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.Tag;
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

    public VideoComponent() {
        videoElement.setProperty("autoplay", true);
        getElement().appendChild(
            videoElement,
            canvasElement
        );
    }
    public Registration addSnapshotListener(
         ComponentEventListener<ImageSnapEvent> listener) {
        return addListener(ImageSnapEvent.class, listener);
    }
}
