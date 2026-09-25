package saveload;

import java.util.Hashtable;
 /** used by JsonSaveLoad as an implementation
* @pram save values only accepts wraper typerna av Integer, Double & String
*/
public interface SaveLoadInterface {
    void save(Hashtable<String,Object> values);
    Hashtable<String,Object> load();
}