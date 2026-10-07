package other;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/** Class to represent the parameters of a solar array.
 */
public class LocationData implements SavableData{
    private double longitude;
    private double latitude;
    


    public LocationData(double lon, double lat){
        this.longitude = lon;
        this.latitude = lat;
    }

    public LocationData(JsonObject obj){
        this.longitude = obj.getJsonNumber("longitude").doubleValue();
        this.latitude = obj.getJsonNumber("latitude").doubleValue();
    }

    /** Returns the area of the solar array.
     * @return Area (m^2).
     */
    public double Longitude(){return  longitude;}

    /** Returns the efficiency of the solar array.
     * @return Efficiency (0 - 1).
     */
    public double Latitude(){return latitude;}

    public JsonObject ToJsonObj(String key){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("longitude", longitude);
        builder.add("latitude", latitude);
        JsonObject val = builder.build();
        builder = Json.createObjectBuilder();
        builder.add(key, val);
        return builder.build();
    }
}
