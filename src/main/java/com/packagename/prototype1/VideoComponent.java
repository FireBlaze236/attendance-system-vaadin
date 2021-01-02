package com.packagename.prototype1;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.dom.Element;

@Tag("div")
public class VideoComponent extends Component {
    Element videoElement = new Element("video");
    Element canvasElement = new Element("canvas");
    Element imageElement = new Element("img");

    public VideoComponent() {
        videoElement.setProperty("autoplay", true);
        getElement().appendChild(
            videoElement,
            canvasElement,
            imageElement
        );
    }
}
