package location.builder;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class LabelBuilder implements LBuilder{

    private Label label;  // private Label title;
    //private int width;
    //private Row row;

    @Override 
    public void setLabelName(String title){
        this.label  = new Label("" + title);
    }
    
    @Override
    public void setWidth(int width){
        label.setMaxWidth(width);
    }

    /*@Override
    public void setwidth(Row row){
        this.row = row;
    }*/

    public Label getLabel(){
        return label;
    }
}
