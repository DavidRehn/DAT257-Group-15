package location.builder;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class VBoxBuilder implements VBuilder {

    private VBox vBox = new VBox();
    private Insets insets;

    @Override
    public void setSpacing(double spacing) {
        this.vBox.setSpacing(spacing);
    }
    @Override
    public void setAlignment(Pos alignment) {
        this.vBox.setAlignment(alignment);
    }
    @Override
    public void setPrefWidth(int W){
        this.vBox.setPrefWidth(W);
    }
    @Override
    public void setMinHeight(int H){
        this.vBox.setMinHeight(H);
    }
    @Override
    public void setPadding(Insets insets){
        this.vBox.setPadding(insets);
        //this.insets = insets;
    }
    @Override
    public void setStyle(String string){
        this.vBox.getStyleClass().add(string);
      //layout.getStyleClass().add("infoBox");
    }
    @Override
    public void addChildren(Node... nodes) {
        this.vBox.getChildren().addAll(nodes);
    }

        //interface functions
//--------------------------------
        //get functions
    public VBox getVBox() {
        return vBox;
    }
}