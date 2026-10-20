package location.builder;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;

public interface VBuilder {

    void setSpacing(double spacing);

    void setAlignment(Pos alignment);
    void setPrefWidth(int W);
    void setMinHeight(int H);
    void setPadding(Insets insets);
    void setStyle(String string); 
    void addChildren(Node... nodes);

}
/*Example
    VBox layout = new VBox(8);
    layout.setPrefWidth(220);
    layout.setMinHeight(95);
    layout.setPadding(new Insets(20));
    layout.setAlignment(Pos.CENTER_LEFT);
    layout.getStyleClass().add("infoBox");*/