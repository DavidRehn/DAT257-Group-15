package location.builder;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class LabelBuilder implements LBuilder{

    private Label title;
    private RowMaker rowMaker;

    @Override 
    public void setLabelName(String[] title){
        this.title = new Label("" + title[0]);
    }
    
    @Override
    public void setWidth(int width){
        title.setMaxWidth(width); 
    }

     @Override
    public void setRow(RowMaker rowMaker){
        this.rowMaker = rowMaker;
    }


    /*@Override
    public void setwidth(Row row){
        this.row = row;
    }*/
        //interface functions
//--------------------------------
        //get functions
    public Label getLabel(){
        return title;
    }
    public HBox getHBox(){
        return this.rowMaker.getRow15();
    }
     public ArrayList<LBuilder> getRowList(){
        return this.rowMaker.getListOfRow();
    }
}
