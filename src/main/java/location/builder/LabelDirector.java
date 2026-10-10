package location.builder;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import location.builder.LBuilder;

//Wene only a text is wanted.
public class LabelDirector {

    public void constructNameOfLabelWidth(LBuilder lBuilder, String string){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(90);
    }
    public void constructNameOfLabel(LBuilder lBuilder, String string){
        lBuilder.setLabelName(string);
    }
    
    public void constructRowLabelTextfild(LBuilder lBuilder, String string, TextField textField){
        lBuilder.setLabelName(string);
        //lBuilder.setRow(new Row(this.title, textField));
        lBuilder.setWidth(90);
    }

    public void constructSolarDetails(LBuilder lBuilder){
        lBuilder.setLabelName("Solar panel details");
        //lBuilder.setWidth(90);
    }

    public void constructArea(LBuilder builder){
        /*builder.reset();
        builder.setLabelName("Area");
        builder.setInput(new TextField("Area (m²)"));
        
        Conditions[] con = {new NumberNotZero()}
        builder.setConditions(con);
        */
    }

    public void constructBackButton(BBuilder bBuilder){
        /*
        Button backButton = new Button("← Back to main page");

        backButton.setOnAction(event -> {
            root.viewStart(stage);
        });

        HBox backRow = new HBox(backButton);

        setAlignment(Pos.CENTER_LEFT);
        backRow.setPadding(new Insets(0, 0, 0, 20));
        backRow.setMaxWidth(Double.MAX_VALUE);
        */
    }
    public void constructZone(LBuilder lBuilder){
        lBuilder.setLabelName( "Zone" );

    }
}
