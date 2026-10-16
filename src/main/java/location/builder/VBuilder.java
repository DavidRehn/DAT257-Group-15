package location.builder;

import javafx.geometry.Pos;
import javafx.scene.Node;

public interface VBuilder {

    void setSpacing(double spacing);

    void setAlignment(Pos alignment);

    void addChildren(Node... nodes);
}