package location.builder;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ButtonBuilder implements LBuilder {
    private Button defultTitle;
    private RowMaker rowMaker;
    private BBuilder a;
    private EventHandler<ActionEvent> event;

    @Override 
    public void setLabelName(String labelName ){
        this.defultTitle = new Button(labelName);
    }

    @Override 
    public void setWidth(int width){
        this.defultTitle.setPrefWidth(width);        
    }

    /*@Override
    public void setRow(RowMaker rowMaker){
        this.rowMaker = rowMaker;
    }*/
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

    public Button getButton() {
        return defultTitle;//
    }
}