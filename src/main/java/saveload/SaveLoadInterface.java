package saveload;

import java.util.Hashtable;
public interface SaveLoadInterface {
    void save(Hashtable<String,Object> values);
    Hashtable<String,String> load();
}