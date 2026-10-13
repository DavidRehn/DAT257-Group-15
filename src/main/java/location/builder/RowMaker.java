package location.builder;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;


public class RowMaker {
    private HBox rowFormat;
    private ArrayList<LBuilder> rows;

    /** Called by The first LBuilder in the row to creat a list of all LBuilder objects that are desierd to be on the same row.
     * @param firstRow is the firs element of the row.
     */
    public RowMaker(LBuilder firstRow){
        this.rows = new ArrayList<>();
        this.rows.add(firstRow);
    }

    public RowMaker( ArrayList<LBuilder> rows, LBuilder newColume){
        this.rows = rows;
        this.rows.add(newColume);
    }

    public ArrayList<LBuilder> getListOfRow(){
        return rows;
    }

    /** Makes a HBox of all LBuilder elements from this instance of "rows" and places theme in order as a row.
     * Only the last element in the row evocs this method.
     * preset distens of 15 between Objects.
     * @return HBox of "rows" as a row.
     */
    public HBox getRow15(){
        if (rows.get(0) instanceof LabelBuilder){
            Label temp = ((LabelBuilder)rows.get(0)).getLabel();
            this.rowFormat = new HBox(15, temp);
        }
        else if (rows.get(0) instanceof TextInputBuilder){
            TextField temp = ((TextInputBuilder)rows.get(0)).getTextField();
            this.rowFormat = new HBox(15, temp);
        }
        else if (rows.get(0) instanceof MultiChoiceBuilder){
            ComboBox<String> temp = ((MultiChoiceBuilder)rows.get(0)).getComboBox();
            this.rowFormat = new HBox(15, temp);
        }else{
            //only for error checking.
            System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
        }
        //this.rowFormat = new HBox(15, temp);  getComboBox MultiChoiceBuilder
        //System.out.println("IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");

        for(int i=1; i< rows.size(); i++){
            //int s = rows.size();
            //System.out.println("Size : " + s);

            if (rows.get(i) instanceof LabelBuilder){
                Label temp = ((LabelBuilder)rows.get(i)).getLabel();
                this.rowFormat.getChildren().add(temp);
            }
            else if (rows.get(i) instanceof TextInputBuilder){
                TextField temp = ((TextInputBuilder)rows.get(i)).getTextField();
                this.rowFormat.getChildren().add(temp);
            }
            else if (rows.get(i) instanceof MultiChoiceBuilder){
                ComboBox<String> temp = ((MultiChoiceBuilder)rows.get(i)).getComboBox();
                this.rowFormat.getChildren().add(temp);
            }else{
                //only for error checking.
                System.out.println("UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
            }

        }
        //System.out.println("TTTTTTTTTTTTTTTTTTTTTTUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");

        rowFormat.setAlignment(Pos.CENTER);
        return rowFormat;
    }
}
