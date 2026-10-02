package other;

import java.util.Hashtable;

import saveload.SaveLoadInterface;

public class Model {
    private Hashtable<String, SavableData> datatable;
    private SaveLoadInterface saveLoad;

    public Model(SaveLoadInterface saveLoad){
        this.saveLoad = saveLoad;
    }

    public SavableData Get(String key){
        return datatable.get(key);
    }

    public void Store(String key, SavableData data){
        datatable.put(key, data);
    }

    public boolean SaveToDisc(){
        return saveLoad.save(datatable);
    }
}
/* 
public interface SaveLoadInterface {
    public void save(Hashtable<String,Object> values);
    
    public Hashtable<String,Object> load();
}*/