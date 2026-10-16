package other;

import java.util.Hashtable;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import saveload.SaveLoadInterface;

public class Model {
    private Hashtable<String, SavableData> datatable;
    private SaveLoadInterface saveLoad;
    private Hashtable<String, Integer> profiles;
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

    private void SetCurrentProfile(int profile){
        this.currentProfile = profile;
    }
    
    private class InternalData implements SavableData{
        private Hashtable<String,Integer> profiles;
        private int HighestProfile;

        public InternalData(JsonObject obj){

        }
        @Override
        public JsonObject ToJsonObj(String key) {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            
            JsonObject val = builder.build();
            builder = Json.createObjectBuilder();
            builder.add(key, val);
            return builder.build();
        }
        
    }
}
/* 
public interface SaveLoadInterface {
    public void save(Hashtable<String,Object> values);
    
    public Hashtable<String,Object> load();
}*/