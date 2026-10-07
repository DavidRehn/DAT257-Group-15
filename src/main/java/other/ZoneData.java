package other;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/** Class to represent the parameters of a solar array.
 */
public class ZoneData implements SavableData{
    private String zone;
    


    public ZoneData(String z){
        this.zone = z;
    }

    public String Zone(){return  zone;}


    public JsonObject ToJsonObj(String key){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("zone", zone);
        return builder.build();
    }
}
