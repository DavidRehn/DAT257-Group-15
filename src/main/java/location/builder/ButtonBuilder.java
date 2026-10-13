package location.builder;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class ButtonBuilder implements LBuilder {
    private Button defultTitle;
    private RowMaker rowMaker;
    private BBuilder a;

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
        //interface functions
//--------------------------------
    public void setEvent(BBuilder bBuilder){
        this.a = bBuilder;
    }


}
