package other;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import java.util.HashMap;
import java.util.Hashtable;

public class TimestampData implements SavableData {
    private Hashtable<String, Double> timestamps;

    public TimestampData(){
        timestamps = new Hashtable<>();
    }

    public void Put(String key, double value){
        timestamps.put(key, value);
    }
    public void Remove(String key, double value){
        timestamps.remove(key, value);
    }
    
    public Boolean hasKey(String key){
        return timestamps.containsKey(key);
    }

    public Hashtable<String, Double> GetTimestamps(){return timestamps;}

    public JsonObject ToJsonObj(String key){
        JsonObjectBuilder builder = Json.createObjectBuilder();

        for (HashMap.Entry<String, Double> en : timestamps.entrySet()) {
            builder.add(en.getKey(), en.getValue());
        }
        JsonObject obj = builder.build();
        builder = Json.createObjectBuilder();
        builder.add(key,obj);
        return builder.build();
    }
}
