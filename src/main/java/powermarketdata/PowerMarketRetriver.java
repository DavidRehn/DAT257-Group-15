package powermarketdata;

import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import javax.json.stream.JsonParser;  
import javax.json.Json;  


/**
 *  Class to handle the interaction with the Open Elpris Api.
 */
public class PowerMarketRetriver{

    private String apiUrl = "https://www.elprisetjustnu.se/api/v1/prices/2026/10-01_SE3.json";     // Open meteo api
    private final HttpClient httpClient;
    //private PowerMarketParser powerMarketParser;

    //private PowerMarketParser a = new JsonParser();

    public PowerMarketRetriver(){
        httpClient = HttpClient.newHttpClient();
        //powerMarketParser = new PowerMarketParser();
    }

    public String GetPowerMarketInfo(){//HTTPSolarRequest requestParams){
        /*apiUrl += String.format(Locale.US,  // So it uses . instead of , when formatting
                                "2026/09-29_SE3.json?avg24"      // Returns the time in the local timezone (from coords), handles summer time automaticaly
                                //,requestParams.Latitude(), requestParams.Longitude(), requestParams.Tilt(), requestParams.Azimuth(), requestParams.Days());
        */
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).GET().build();
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        try{
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            // Print the response for testing
            System.out.println(response.body() +"&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            return(response.body());
        }catch (Exception e){
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxCatch");
            e.printStackTrace();
            return null;
        }
    } 
}
/*
            //JsonParser parser = Json.createParser(new StringReader(response.body()));
            //PowerMarketParser.ParseToJson(response.body());
            //PowerMarketParser.PrintTimestamps();    // temporary
 */