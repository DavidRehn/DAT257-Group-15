package weatherdata;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Class to handle parsing of the api response.
 */
public class WeatherParser {
    private ArrayList<ForecastTimestamp> timestamps;

    /** Constructor to create a new WeatherParser
     */
    public WeatherParser(){
        timestamps = new ArrayList<>();
    }

    /** Parses the api response to a list of ForecastTimestamps and writes it to timestamps.
     * @param json The string received from the response body.
     */
    public void ParseToJson(String json){
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject jsonObj = reader.readObject();
        reader.close();
        jsonObj = jsonObj.getJsonObject("hourly");  // "hourly" contains time and value arrays
        JsonArray timeArray = jsonObj.getJsonArray("time");
        JsonArray GTIArray = jsonObj.getJsonArray("global_tilted_irradiance");
        BuildTimestampList(timeArray, GTIArray);
    }

    /** Responsible for building the list from the parsed JsonArrays.
     * @param times Received array with the timestamps.
     * @param GTI Received array with the values.
     */
    private void BuildTimestampList(JsonArray times, JsonArray GTI){
        for (int i = 0; i < times.size(); i++){    // size is the same for both arrays
            LocalDateTime time = LocalDateTime.parse(times.getString(i));
            double number = GTI.getJsonNumber(i).doubleValue();
            timestamps.add(new ForecastTimestamp(time, number));
        }
    }

    /** For debugging, prints out the parsed list.
     */
    public void PrintTimestamps(){
        String out = "[";
        out += timestamps.get(0);
        for (int i = 1; i < timestamps.size(); i++) {
            out += (", " + timestamps.get(i));
        }
        out += "]";
        System.out.println(out);
    }
}
