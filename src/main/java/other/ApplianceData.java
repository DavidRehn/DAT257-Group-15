package other;

import java.util.Hashtable;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class ApplianceData implements SavableData{
    private Hashtable<String,Double> appliances;

    public ApplianceData(){
        appliances = new Hashtable<>();
    }

    public void Put(String key, double value){
        appliances.put(key, value);
    }
    public void Remove(String key, double value){
        appliances.remove(key, value);
    }
    
    public Boolean hasKey(String key){
        return appliances.containsKey(key);
    }

    @Override
    public JsonObject ToJsonObj(String key){
        JsonObjectBuilder builder = Json.createObjectBuilder();

        for (HashMap.Entry<String, Double> en : appliances.entrySet()) {
            builder.add(en.getKey(), en.getValue());
        }
        JsonObject obj = builder.build();
        builder = Json.createObjectBuilder();
        builder.add(key,obj);
        return builder.build();
    }
}