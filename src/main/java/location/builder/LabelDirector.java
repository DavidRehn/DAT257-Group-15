package location.builder;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import location.builder.LBuilder;

//Wene only a text is wanted.
public class LabelDirector {

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
    public void constructMCW(LBuilder lBuilder, String[] string, String[] Alternativs){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(185);
        ((MultiChoiceBuilder)lBuilder).setAlternativs(Alternativs);
    }

        //MultiChoise construktor
    //-------------------------
        // constructors with Row's
    public void constructFirstRowW(LBuilder lBuilder, String[] string){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(90);
        lBuilder.setRow(new RowMaker(lBuilder));
    }

    //Perset comon TextField width of (90)
    public void constructAddingRowTF(LBuilder lBuilder, String[] string, ArrayList<LBuilder> rowList){
        lBuilder.setLabelName(string);
        lBuilder.setWidth(220);
        lBuilder.setRow(new RowMaker(rowList, lBuilder));
    }
    //Perset comon Label width (90)
    public void constructAddingRowL(LBuilder lBuilder, String[] string, ArrayList<LBuilder> rowList){
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