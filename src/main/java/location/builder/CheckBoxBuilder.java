package location.builder;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class CheckBoxBuilder implements CBuilder,LBuilder {

    //private CheckBox checkBox = new CheckBox();
    private CheckBox checkBox;

    @Override
    public void setSelected(boolean selected) {
        checkBox.setSelected(selected);
    }

    @Override 
    public void setLabelName(String title){
        this.checkBox = new CheckBox(title);
    }
    /*@Override
    public void setLabelName(String[] labelName) {
        checkBox.setText(labelName[0]);
    }*/
    
    @Override
    public void setWidth(int width){
        this.checkBox.setMaxWidth(width);
    }

    /*@Override
    public void setRow(RowMaker rowMaker){
        this.checkBox = rowMaker;
    }*/
    @Override 
    public void setStyl(String styl){
        this.checkBox.getStyleClass().addAll(styl);
    }
    @Override 
    public void setEvent(BBuilder bBuilder){
        //label can't have events.
    }
    
     //interface functions
//--------------------------------
        //get functions

    public CheckBox getCheckBox() {
        return checkBox;
    }
}