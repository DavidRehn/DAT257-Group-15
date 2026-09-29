package powermarketdata;

import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Locale;
import javax.json.stream.JsonParser;  
import javax.json.Json;  


/**
 *  Class to handle the interaction with the Open Elpris Api.
 */
public class PowerMarketRetriver{

    private String apiUrl = "https://se.elprisetjustnu.eu/api/v1/prices/2026/09-29_SE3.json?avg24";     // Open meteo api
    private final HttpClient httpClient;
    //private PowerMarketParser powerMarketParser;

    public PowerMarketRetriver(){
        httpClient = HttpClient.newHttpClient();
        //powerMarketParser = new PowerMarketParser();
    }

    public void GetPowerMarketInfo(){//HTTPSolarRequest requestParams){
        /*apiUrl += String.format(Locale.US,  // So it uses . instead of , when formatting
                                "2026/09-29_SE3.json?avg24"      // Returns the time in the local timezone (from coords), handles summer time automaticaly
                                //,requestParams.Latitude(), requestParams.Longitude(), requestParams.Tilt(), requestParams.Azimuth(), requestParams.Days());
        */
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).GET().build();
        HttpResponse<String> response;
        try{
            response = httpClient.send(request, BodyHandlers.ofString());
            JsonParser parser = Json.createParser(new StringReader(response.body()));
            //PowerMarketParser.ParseToJson(response.body());
            //PowerMarketParser.PrintTimestamps();    // temporary
        }catch (Exception e){
            e.printStackTrace();
        }
    } 
}