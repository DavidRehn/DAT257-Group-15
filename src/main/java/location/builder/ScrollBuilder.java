package location.builder;

import javafx.scene.Node;

public interface ScrollBuilder {

    void setContent(Node content);

    void setFitToWidth(boolean fitToWidth);
}