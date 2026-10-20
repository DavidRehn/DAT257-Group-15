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
    private BBuilder a;

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
    @Override 
    public void setStyl(String styl){
        this.defultTitle.getStyleClass().addAll(styl);
    }
    @Override 
    public void setEvent(BBuilder bBuilder){
        this.a = bBuilder;
        this.defultTitle.setOnAction(a.getEvent());
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



  public void setPromptText(String promptText) {/// // ////// تحويل الى تعليق
        if (defultTitle != null) {/// //
            defultTitle.setPromptText(promptText);//////   /////
        }
    }


/// ///   //////   / / / //
/*
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
    }public TextField getTextField() {
        return textField;
    }*/

}
