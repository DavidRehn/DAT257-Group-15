package location.builder;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class VBoxBuilder implements VBuilder {

    private VBox vBox = new VBox();

    @Override
    public void setSpacing(double spacing) {
        vBox.setSpacing(spacing);
    }

    @Override
    public void setAlignment(Pos alignment) {
        vBox.setAlignment(alignment);
    }

    @Override
    public void addChildren(Node... nodes) {
        vBox.getChildren().addAll(nodes);
    }

    public VBox getVBox() {
        return vBox;
    }
}