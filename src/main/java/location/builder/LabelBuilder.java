package location.builder;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class LabelBuilder implements LBuilder{

    //private Label label;  // private Label title;
    private BBuilder a;
    private Label defultTitle;
    private RowMaker rowMaker;

    @Override 
    public void setLabelName(String[] title){
        this.defultTitle = new Label("" + title[0]);
    }
    
    @Override
    public void setWidth(int width){
        defultTitle.setMaxWidth(width);
    }

     @Override
    public void setRow(RowMaker rowMaker){
        this.rowMaker = rowMaker;
    }
    @Override 
    public void setStyl(String[] styl){
        this.defultTitle.getStyleClass().addAll("" + styl);
    }
    @Override 
    public void setEvent(BBuilder bBuilder){
        //label can't have events.
    }
        //interface functions
//--------------------------------
        //get functions
    public Label getLabel(){
        return defultTitle;
    }
    public HBox getHBox(){
        return this.rowMaker.getRow15();
    }
     public ArrayList<LBuilder> getRowList(){
        return this.rowMaker.getListOfRow();
    }
}
