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

    /** Builds a Label with input value and a preset width.
     * @param lBuilder the Concreat Builder were the data is stored.
     * @param string the text on the Label.
     */
    public void constructNameW(LBuilder lBuilder, String[] string){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(90);
    }

    /** Builds a Label with input value.
     * @param lBuilder the Concreat Builder were the data is stored.
     * @param string the text on the Label.
     */
    public void constructName(LBuilder lBuilder, String[] string){
        lBuilder.setLabelName(string);
    }
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

    //Perset comon TextField width of (90)
    public void constructTFWAR(LBuilder lBuilder, String[] string, ArrayList<LBuilder> rowList){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(220);
        lBuilder.setRow(new RowMaker(rowList, lBuilder));
    }
    //Perset comon Label width (90)
    public void constructLWAR(LBuilder lBuilder, String[] string, ArrayList<LBuilder> rowList){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(90);
        lBuilder.setRow(new RowMaker(rowList, lBuilder));
    } 
    
        // constructors with Row's
    //------------------------------------------------
        // Hard coded constructors 

}
/*
TextInputBuilder.setLabelName -> if sats är fucked,
    -needs fixing.
*/