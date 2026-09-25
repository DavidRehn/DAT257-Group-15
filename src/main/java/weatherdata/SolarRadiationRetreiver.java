package weatherdata;

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
 *  Class to handle the interaction with the Open Meteo Api.
 */
public class SolarRadiationRetreiver{
    private String apiUrl = "https://api.open-meteo.com/v1/forecast";     // Open meteo api
    private final HttpClient httpClient;
    private WeatherParser weatherParser;

    public SolarRadiationRetreiver(){
        httpClient = HttpClient.newHttpClient();
        weatherParser = new WeatherParser();
    }

    public void GetWeatherInfo(HTTPSolarRequest requestParams){
        apiUrl += String.format(Locale.US,  // So it uses . instead of , when formatting
                                "?latitude=%f&longitude=%f&hourly=global_tilted_irradiance&tilt=%d&azimuth=%d&forecast_days=%d&timezone=auto",      // Returns the time in the local timezone (from coords), handles summer time automaticaly
                                requestParams.Latitude(), requestParams.Longitude(), requestParams.Tilt(), requestParams.Azimuth(), requestParams.Days());
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).GET().build();
        HttpResponse<String> response;
        try{
            response = httpClient.send(request, BodyHandlers.ofString());
            JsonParser parser = Json.createParser(new StringReader(response.body()));
            weatherParser.ParseToJson(response.body());
            weatherParser.PrintTimestamps();    // temporary
        }catch (Exception e){
            e.printStackTrace();
        }
    }  
}