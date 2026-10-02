package other;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/** Class to represent the parameters of a solar array.
 */
public class SolarPanel implements SavableData{
    private double area;
    private int tilt;
    private Direction direction;
    private double efficiency;

    public SolarPanel(double area, double efficiency){
        this.area = area;
        this.efficiency = efficiency;
    }

    /** Returns the area of the solar array.
     * @return Area (m^2).
     */
    public double Area(){return  area;}

    /** Returns the efficiency of the solar array.
     * @return Efficiency (0 - 1).
     */
    public double Efficiency(){return efficiency;}

    public JsonObject ToJsonObj(String key){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("area", area);
        builder.add("tilt", tilt);
        builder.add("direction", direction.toString());
        builder.add("efficiency", efficiency);
        JsonObject val = builder.build();
        builder = Json.createObjectBuilder();
        builder.add(key, val);
        return builder.build();
    }
}
