//BINGUS
package location.builder;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import other.LocationData;
import other.Model;
import other.SolarPanel;
import other.TimestampData;
import other.ZoneData;

public class TextInputBuilder implements LBuilder {
    
    private TextField defultTitle;
    private RowMaker rowMaker;

    @Override 
    public void setLabelName(String[] defultTitle){
        //this should be fixt
        if(defultTitle.length <= 1 ){
            this.defultTitle = new TextField("" + defultTitle[0]);
        }
        else if(defultTitle[2] != null){
            this.defultTitle = new TextField("" + defultTitle[1]);
        }else{
            this.defultTitle = new TextField("" + defultTitle[0]);
        }
     
    }
    
    @Override
    public void setWidth(int width){
        defultTitle.setMaxWidth(width);

    }

    @Override
    public void setRow(RowMaker rowMaker){
        this.rowMaker = rowMaker;
    }
        //interface functions
//--------------------------------
        //get functions
    public TextField getTextField(){
        return defultTitle;
    }

    public HBox getHBox(){
        return this.rowMaker.getRow15();
    }
    public ArrayList<LBuilder> getRowList(){
        return this.rowMaker.getListOfRow();
    }
}
