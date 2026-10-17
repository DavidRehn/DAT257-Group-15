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
    public void setLabelName(String[] labelName ){
        this.defultTitle = new Button(labelName[0]);
    }

    @Override 
    public void setWidth(int width){
        this.defultTitle.setPrefWidth(width);        
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
        this.a = bBuilder;
        this.defultTitle.setOnAction(a.getEvent());
    }

        //interface functions
//--------------------------------
   
    /*
    private Button button = new Button();
 
    @Override
    public void setEvent(EventHandler<ActionEvent> eventHandler) {
        button.setOnAction(eventHandler);
    }

    public Button getButton() {
        return button;
    }*/
}