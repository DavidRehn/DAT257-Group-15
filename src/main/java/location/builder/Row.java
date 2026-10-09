package location.builder;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;


public class Row {

    public  Row(Label label){
        label.setPrefWidth(90);

        HBox latitudeRow = new HBox(15, label);
        latitudeRow.setAlignment(Pos.CENTER);
    }

    public  Row(Label label, ComboBox<String> comboBox){
        label.setPrefWidth(90);

        HBox latitudeRow = new HBox(15, label, comboBox);
        latitudeRow.setAlignment(Pos.CENTER);
    }

    public Row(Label label, TextField textField){
        label.setPrefWidth(90);

        HBox latitudeRow = new HBox(15, label, textField);
        latitudeRow.setAlignment(Pos.CENTER);
    }    

    public Row(Button buttonA, Button buttonB){
        buttonA.setPrefWidth(90);

        HBox latitudeRow = new HBox(15, buttonA, buttonB);
        latitudeRow.setAlignment(Pos.CENTER);
    }
}
