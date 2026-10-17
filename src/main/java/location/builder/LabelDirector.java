package location.builder;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import location.builder.LBuilder;

//Wene only a text is wanted.
public class LabelDirector {
    /**Naming of Directori constructor
     (constructor made for a Specifific Concreat Builder type. The absens of this means the constructor woks for allConcreat Builder) [Concreat Builder= LabelBuilder, TextInputBuilder ...]
     * L : LabelBuilder
     * TF: TextInputBuilder
     * MC: MultiChoiceBuilder
     (Indicates unique functunalitis/propetis of any given constructor)
     * W : Width is set in constructor.
     * FR: First Row.       for the first element of a row.
     * AR: Added/ing Row.   for any aditional element row (NOT the first one) 
     * /
    
    /* 
     * @param lBuilder
     * @param string
     */

 
    public void constructW90(LBuilder lBuilder, String[] string){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(90);
    }

    
    public void constructName(LBuilder lBuilder, String[] string){
        lBuilder.setLabelName(string);
    }
    public void constructS(LBuilder lBuilder, String[] string, String[] styl){
        lBuilder.setLabelName(string);
        lBuilder.setStyl(styl);

    }

        // universal
    //-------------------------
        // event & actions
    public void constructEventB(LBuilder lBuilder, String[] string){
        lBuilder.setLabelName(string);
        //((ButtonBuilder)lBuilder).setEvent(new CallWetherAPI());
    }
// event & actions
    //-------------------------
        //MultiChoise construktor
    public void constructMCWFR(LBuilder lBuilder, String[] string, String[] Alternativs){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(185);
        ((MultiChoiceBuilder)lBuilder).setAlternativs(Alternativs);
        lBuilder.setRow(new RowMaker(lBuilder));
    }

    public void constructMCWAR(LBuilder lBuilder, String[] string, String[] Alternativs, ArrayList<LBuilder> rowList){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(185);
        ((MultiChoiceBuilder)lBuilder).setAlternativs(Alternativs);
        lBuilder.setRow(new RowMaker(rowList, lBuilder));
    }

        //MultiChoise construktor
    //-------------------------
        // constructors with Row's
    public void constructWFR(LBuilder lBuilder, String[] string){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(90);
        lBuilder.setRow(new RowMaker(lBuilder));
    }
    public void constructSFR(LBuilder lBuilder, String[] string, String[] styl){
        lBuilder.setLabelName(string);
        lBuilder.setStyl(styl);
        lBuilder.setRow(new RowMaker(lBuilder));
    }
    public void constructWSFR(LBuilder lBuilder, String[] string, String[] styl){
        lBuilder.setLabelName(string);
        lBuilder.setStyl(styl);
        lBuilder.setWidth(90);
        lBuilder.setRow(new RowMaker(lBuilder));
    }

    //Perset comon TextField width of (90)
    public void constructW220AR(LBuilder lBuilder, String[] string, ArrayList<LBuilder> rowList){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(220);
        lBuilder.setRow(new RowMaker(rowList, lBuilder));
    }
    public void constructW220SAR(LBuilder lBuilder, String[] string, String[] styl, ArrayList<LBuilder> rowList){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(220);
        lBuilder.setStyl(styl);
        lBuilder.setRow(new RowMaker(rowList, lBuilder));
    }
    //Perset comon Label width (90)
    public void constructW90AR(LBuilder lBuilder, String[] string, ArrayList<LBuilder> rowList){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(90);
        lBuilder.setRow(new RowMaker(rowList, lBuilder));
    } 
    public void constructW90SAR(LBuilder lBuilder, String[] string, String[] styl, ArrayList<LBuilder> rowList){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(90);
        lBuilder.setStyl(styl);
        lBuilder.setRow(new RowMaker(rowList, lBuilder));
    } 
    
        // constructors with Row's
    //------------------------------------------------
        // Hard coded constructors 

    
    
    public void constructRowLabelTextfild(LBuilder lBuilder, String[] string, TextField textField){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(90);
    }

    public void constructSolarDetails(LBuilder lBuilder){
        lBuilder.setLabelName(new String[]{"Solar panel details"});
        //lBuilder.setWidth(90);
    }

    public void constructArea(LBuilder builder){
        /*builder.reset();
        builder.setLabelName("Area");
        builder.setInput(new TextField("Area (m²)"));
        
        Conditions[] con = {new NumberNotZero()}
        builder.setConditions(con);
      /؟؟؟؟؟؟؟؟؟؟؟؟؟؟؟؟؟؟؟
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
       //؟؟؟؟؟؟؟؟؟؟؟؟؟؟؟؟؟؟؟؟؟
       */
    }
    public void constructZone(LBuilder lBuilder){
        lBuilder.setLabelName( new String[]{"Zone"});

    }
    


}
