package location.builder;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;


public class Row {
    private HBox row;


    public  Row(Label label){
        label.setPrefWidth(90);

     /*   HBox latitudeRow = new HBox(15, label);
        latitudeRow.setAlignment(Pos.CENTER);

      */
        row = new HBox(15, label);
        row.setAlignment(Pos.CENTER);
    }

    public  Row(Label label, ComboBox<String> comboBox){
        label.setPrefWidth(90);

       /* HBox latitudeRow = new HBox(15, label, comboBox);
        latitudeRow.setAlignment(Pos.CENTER);

        */
        row = new HBox(15, label, comboBox);
        row.setAlignment(Pos.CENTER);
    }

    public Row(Label label, TextField textField){
        label.setPrefWidth(90);

        /*HBox latitudeRow = new HBox(15, label, textField);
        latitudeRow.setAlignment(Pos.CENTER);

         */
        row = new HBox(15, label, textField);
        row.setAlignment(Pos.CENTER);
    }    

    public Row(Button buttonA, Button buttonB){
        buttonA.setPrefWidth(90);

       /* HBox latitudeRow = new HBox(15, buttonA, buttonB);
        latitudeRow.setAlignment(Pos.CENTER);

        */
        row = new HBox(15, buttonA, buttonB);
        row.setAlignment(Pos.CENTER);
    }

    public Row(TextField textFieldA, TextField textFieldB, Button button) {

        row = new HBox(
                10,
                textFieldA,
                textFieldB,
                button
        );

        row.setAlignment(Pos.CENTER);
    }
    public Row(
            Label nameLabel,
            Label wattageLabel,
            CheckBox checkBox,
            Button removeButton
    ) {

        row = new HBox(
                20,
                nameLabel,
                wattageLabel,
                checkBox,
                removeButton
        );

        row.setAlignment(Pos.CENTER);
    }


    public HBox getRow() {
        return row;
    }
}
