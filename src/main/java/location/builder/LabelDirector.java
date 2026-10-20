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
     * /
    
    /* 
     * @param lBuilder
     * @param string
     */

    public void constructName(LBuilder lBuilder, String[] string){
        lBuilder.setLabelName(string);
    }

    public void constructS(LBuilder lBuilder, String[] string, String styl){
        lBuilder.setLabelName(string);
        lBuilder.setStyl(styl);
    }

    public void constructW(LBuilder lBuilder, String[] string, int a){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(a);
    }
    public void constructWS(LBuilder lBuilder, String[] string, int a, String styl){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(a);
        lBuilder.setStyl(styl);
    }

        // universal
    //-------------------------
        //MultiChoise construktor
    public void constructM185CWFR(LBuilder lBuilder, String[] string, int W, String[] Alternativs){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(185);
        ((MultiChoiceBuilder)lBuilder).setAlternativs(Alternativs);
        lBuilder.setRow(new RowMaker(lBuilder));
    }

    public void constructMCW185AR(LBuilder lBuilder, String[] string, String[] Alternativs, ArrayList<LBuilder> rowList){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(185);
        ((MultiChoiceBuilder)lBuilder).setAlternativs(Alternativs);
        lBuilder.setRow(new RowMaker(rowList, lBuilder));
    }

        //MultiChoise construktor
    
    //------------------------------------------------
    //------------------------------------------------
//VBOX

    public void constructVboxAWHIS(VBuilder vBuilder,double spacing,Pos alignment,int W,int H, int insets, String styl){
        vBuilder.setSpacing(spacing);
        vBuilder.setAlignment(alignment);
        vBuilder.setPrefWidth(W);
        vBuilder.setMinHeight(H);
        vBuilder.setPadding(new Insets (insets));
        vBuilder.setStyle(styl); 
    }
 
    //------------------------------------------------
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
