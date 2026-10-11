package location.builder;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

public class ScrollPaneBuilder implements ScrollBuilder {

    private ScrollPane scrollPane = new ScrollPane();

    @Override
    public void setContent(Node content) {
        scrollPane.setContent(content);
    }

    @Override
    public void setFitToWidth(boolean fitToWidth) {
        scrollPane.setFitToWidth(fitToWidth);
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }
}