package other;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/** Class to represent the parameters of a solar array.
 */
public class SolarPanel implements SavableData{
    private double area;
    private int tilt;
    private Direction direction;
    private double efficiency;

    public SolarPanel(double area, int tilt, Direction direction, double efficiency){
        this.area = area;
        this.tilt = tilt;
        this.direction = direction;
        this.efficiency = efficiency;
    }
    public SolarPanel(JsonObject obj){
        this.area = obj.getJsonNumber("area").doubleValue();
        this.tilt = obj.getInt("tilt");
        this.direction = directionConverter(obj.getString("direction"));
        this.efficiency = obj.getJsonNumber("efficiency").doubleValue();
    }

    /** Returns the area of the solar array.
     * @return Area (m^2).
     */
    public double Area(){return  area;}

    public int Tilt(){return  tilt;}

    public Direction Direction(){return direction;}

    /** Returns the efficiency of the solar array.
     * @return Efficiency (0 - 1).
     */
    public double Efficiency(){return efficiency;}

    /** {{"area" : val}, {"tilt" : val}, {"direction" : val}, {"efficiency" : val}}
     */
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


    public static Direction directionConverter(String direction){
        switch(direction){
            case "SOUTH" : return Direction.SOUTH;
            case "SOUTHWEST" : return Direction.SOUTHWEST;
            case "WEST" : return Direction.WEST;
            case "NORTHWEST" : return Direction.NORTHWEST;
            case "NORTH" : return Direction.NORTH;
            case "NORTHEAST" : return Direction.NORTHEAST;
            case "EAST" : return Direction.EAST;
            case "SOUTHEAST" : return Direction.SOUTHEAST;
        }
        return Direction.SOUTH;     // Will never happen
    }
}
