package location.builder;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class TextInputBuilder implements TBuilder {

    private Label title;
    private TextField defult;

    @Override 
    public void setLabelName(String titlee){
        this.title = new Label("" + titlee);
    }

    @Override
    public void setRow(Label label){
        label.setPrefWidth(90);

        HBox latitudeRow = new HBox(15, title, defult);
        latitudeRow.setAlignment(Pos.CENTER);
    }

    public void ssetLabelName(String labekName) {
        TextField tiltField = new TextField();
        tiltField.setPromptText("Tilt (degrees)");
        tiltField.setMaxWidth(220);
        /*if(appData.Get("SolarPanel")!=null){
            tiltField.setText(""+solarPanel.Tilt());
        }*/
    }

    public Label getLabel(){
        return title;
    }
}
