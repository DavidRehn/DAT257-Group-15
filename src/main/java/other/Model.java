package other;

import java.util.Hashtable;

import saveload.SaveLoadInterface;

public class Model {
    private Hashtable<String, SavableData> datatable;
    private SaveLoadInterface saveLoad;
    private int currentProfile=0;
    public Model(SaveLoadInterface saveLoad){
        this.saveLoad = saveLoad;
        datatable=new Hashtable<>();
    }

    public SavableData Get(String key){
        return datatable.get(key + "*" + currentProfile);
    }

    public void Store(String key, SavableData data){
        datatable.put(key+"*"+currentProfile, data);
    }

    public boolean SaveToDisc(){
        return saveLoad.save(datatable);
    }
    
    public void LoadFromDisc(){
        datatable.clear();
        datatable.putAll(saveLoad.load());
    }

    private void SetProfile(int profile){
        this.currentProfile = profile;
    }
}
/* 
public interface SaveLoadInterface {
    public void save(Hashtable<String,Object> values);
    
    public Hashtable<String,Object> load();
}*/