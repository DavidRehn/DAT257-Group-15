package powermarketdata;

import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Locale;
import java.util.Set;

import javax.json.stream.JsonParser;  
import javax.json.Json;  

import java.time.LocalDateTime;

/**
 *  Class to handle the interaction with the Open Elpris Api.
 */
public class PowerMarketRetriver{
    //private String apiUrl = "https://www.elprisetjustnu.se/api/v1/prices/2026/10-01_SE3.json";     // Open meteo api
    private LocalDateTime time;
    private final HttpClient httpClient;
    
    public PowerMarketRetriver(){
        httpClient = HttpClient.newHttpClient();
    }

    public String GetPowerMarketInfo(){
        time = LocalDateTime.now();

        // this is for the api dayformat. it has to be 2 characters long.
        String a = "" + time.getDayOfMonth();
        if(time.getDayOfMonth() < 10){
            a = "0" + time.getDayOfMonth();
        }

        // should be a option to switch zones.       
        String swedenZone =  "SE3";

        String requestUrl = 
            "https://www.elprisetjustnu.se/api/v1/prices/" //2026/10-01_SE3.json
                    + time.getYear() +"/"
                    + time.getMonthValue()+"-"
                    + a+"_"
                    + swedenZone+ ".json";
           
    

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(requestUrl)).GET().build();
        //System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

        try{
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            // Print the response for testing
            //System.out.println(response.body() +"&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

            return(response.body());

        }catch (Exception e){
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxCatch");
            e.printStackTrace();
            return null;
        }
    } 
}
/*
SE1 = Luleå / Norra Sverige
SE2 = Sundsvall / Norra Mellansverige
SE3 = Stockholm / Södra Mellansverige
SE4 = Malmö / Södra Sverige
 */