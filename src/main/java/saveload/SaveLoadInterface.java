package saveload;

import java.util.Hashtable;

import other.SavableData;
 /** used by JsonSaveLoad as an implementation
* @pram save values only accepts wraper typerna av Integer, Double & String
*/
public interface SaveLoadInterface {
    public boolean save(Hashtable<String,SavableData> values);
    
    public Hashtable<String,SavableData> load();
}