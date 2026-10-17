//BINGUS
package location.builder;

import java.util.ArrayList;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class MultiChoiceBuilder  implements LBuilder{

    private ComboBox<String> defultTitle;
    private RowMaker rowMaker;
    private BBuilder a;

    @Override 
    public void setLabelName(String[] labelName ){
        this.defultTitle = new ComboBox<>();
        this.defultTitle.setPromptText(labelName[0]);
    }

    @Override 
    public void setWidth(int width){
        //should be a klass that alows for more optemisation.
        this.defultTitle.setPrefWidth(width);
        this.defultTitle.setMaxWidth(width);
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
    public void setAlternativs(String[] string){
        //for(int i =0; i < length(string)
         defultTitle.getItems().addAll(string);
    }
//--------------------------------
        //get functions

    public ComboBox<String> getComboBox(){
        return defultTitle;
    }
    /*ComboBox<String> defultTitle = new ComboBox<>();

        defultTitle.getItems().addAll(
                "South",
                "Southwest",
                "West",
                "Northwest",
                "North",
                "Northeast",
                "East",
                "Southeast"
        );

        defultTitle.setPromptText("Choose direction");

        defultTitle.setPrefWidth(185);
        defultTitle.setMaxWidth(185);
        if(appData.Get("SolarPanel")!=null){
            defultTitle.setValue(solarPanel.Direction().toString().toLowerCase());
        } */
    public HBox getHBox(){
        return this.rowMaker.getRow15();
    }
     public ArrayList<LBuilder> getRowList(){
        return this.rowMaker.getListOfRow();
    }
}
