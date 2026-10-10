package other;

import java.util.Hashtable;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class ApplianceData implements SavableData{
    private Hashtable<String,Double> appliances;
    private Hashtable<String, Boolean> inUseStates;


    public ApplianceData(){
        appliances = new Hashtable<>();
        inUseStates = new Hashtable<>();
    }
    public ApplianceData(JsonObject obj) {

        appliances = new Hashtable<>();
        inUseStates = new Hashtable<>();

        for (String key : obj.keySet()) {

            // Support old save format
            if (obj.get(key).getValueType() == javax.json.JsonValue.ValueType.NUMBER) {

                appliances.put(
                        key,
                        obj.getJsonNumber(key).doubleValue()
                );

                inUseStates.put(key, false);

            } else {

                // New save format
                JsonObject applianceObj = obj.getJsonObject(key);

                double wattage =
                        applianceObj.getJsonNumber("wattage").doubleValue();

                boolean inUse =
                        applianceObj.getBoolean("inUse", false);

                appliances.put(key, wattage);
                inUseStates.put(key, inUse);
            }
        }
    }

    public void Put(String key, double value){
        appliances.put(key, value);
        inUseStates.putIfAbsent(key, false);
    }


    public void Remove(String key, double value){

        if (appliances.remove(key, value)) {
            inUseStates.remove(key);
        }
    }
    public void SetInUse(String key, boolean inUse) {
        if (appliances.containsKey(key)) {
            inUseStates.put(key, inUse);
        }
    }

    public boolean IsInUse(String key) {
        return inUseStates.getOrDefault(key, false);
    }
    public Boolean hasKey(String key){
        return appliances.containsKey(key);
    }
    public Hashtable<String, Double> GetAppliances() {
        return appliances;
    }
    @Override
    public JsonObject ToJsonObj(String key){
        JsonObjectBuilder builder = Json.createObjectBuilder();

        for (HashMap.Entry<String, Double> en : appliances.entrySet()) {

            JsonObjectBuilder applianceBuilder =
                    Json.createObjectBuilder();

            applianceBuilder.add(
                    "wattage",
                    en.getValue()
            );

            applianceBuilder.add(
                    "inUse",
                    IsInUse(en.getKey())
            );

            builder.add(
                    en.getKey(),
                    applianceBuilder
            );
        }
        JsonObject obj = builder.build();
        builder = Json.createObjectBuilder();
        builder.add(key,obj);
        return builder.build();
    }
}