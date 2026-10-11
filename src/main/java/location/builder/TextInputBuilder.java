package location.builder;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class TextInputBuilder implements TBuilder {

    private Label title;
  //  private TextField defult;
  private TextField textField = new TextField();
    @Override 
    public void setLabelName(String titlee){
        this.title = new Label("" + titlee);
    }




   public void setPromptText(String promptText) {
       textField.setPromptText(promptText);
       textField.setMaxWidth(220);
   }

    public Label getLabel(){
        return title;
    }
    public TextField getTextField() {
        return textField;
    }
}
