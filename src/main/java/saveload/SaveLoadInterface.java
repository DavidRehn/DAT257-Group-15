package saveload;

import java.util.Hashtable;
 /** used by JsonSaveLoad as an implementation
* @pram save values only accepts wraper typerna av Integer, Double & String
*/
public interface SaveLoadInterface {
    public void save(Hashtable<String,Object> values);
    
    public Hashtable<String,Object> load();
}