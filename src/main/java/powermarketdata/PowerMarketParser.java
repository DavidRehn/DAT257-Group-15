package powermarketdata;

import java.io.StringReader;
//import java.time.LocalDateTime;
//import java.util.ArrayList;

import javax.json.Json;
//import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Class to handle parsing of the api response.
 */
public class PowerMarketParser{

    /** Constructor to create a new PowerMarketParser
     */
    public PowerMarketParser(){

    }

    /** Parses the api response to a list of ForecastTimestamps and writes it to timestamps.
     * @param json The string received from the response body.
     */
    public void ParseToJson(String json){
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject jsonObj = reader.readObject();
        reader.close();
        jsonObj = jsonObj.getJsonObject("time_start");  // "hourly" contains time and value arrays
        //JsonArray timeArray = jsonObj.getJsonArray("time");
        //JsonArray GTIArray = jsonObj.getJsonArray("global_tilted_irradiance");
        //BuildTimestampList(timeArray, GTIArray);
    }
}
