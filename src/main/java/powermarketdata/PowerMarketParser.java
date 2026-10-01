package powermarketdata;

import java.io.StringReader;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonNumber;
//import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonArray;

/**
 * Class to handle parsing of the api response.
 */
public class PowerMarketParser{
private ArrayList<Double> priseStamps;

        
    /** Constructor to create a new PowerMarketParser
     */
    public PowerMarketParser(){
        priseStamps = new ArrayList<>();
    }

    
    /** Parses the api response to a list of ForecastPriseStamps and writes it to priseStamps.
     * @param json The string received from the response body.
     */
    public static ArrayList<Double> ParseToJson(String json){
        ArrayList<Double> prislista = new ArrayList<>();
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonArray jsonArr= reader.readArray();
        reader.close();

        for (JsonValue i : jsonArr) {
            if(i instanceof JsonObject ){
                JsonNumber num = ((JsonObject)i).getJsonNumber("SEK_per_kWh");
                prislista.add(num.doubleValue());                
            }
        }

        System.out.println(prislista +"tttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
        return prislista;
    
    }
}


/*
 "time_start": "2022-11-24 T03:00:00 +01:00",
 "time_end":   "2022-11-24 T04:00:00 +01:00"

 "time_start": "2022-11-24 T 04:00:00 +01:00",
 "time_end":   "2022-11-24 T 05:00:00 +01:00"
                             h:min:s + tidsson
 */