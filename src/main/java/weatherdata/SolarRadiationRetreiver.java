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

public class SolarRadiationRetreiver{
    private String apiUrl = "https://api.open-meteo.com/v1/forecast";     // Open meteo api
    private final HttpClient httpClient;

    public SolarRadiationRetreiver(HTTPSolarRequest request){
        httpClient = HttpClient.newHttpClient();
        apiUrl += String.format(Locale.US,  // So it uses . instead of , when formatting
                                "?latitude=%f&longitude=%f&hourly=global_tilted_irradiance&tilt=%d&azimuth=%d&forecast_days=%d", 
                                request.Latitude(), request.Longitude(), request.Tilt(), request.Azimuth(), request.Days());
    }


    public String GetWeatherInfo() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        try {
            HttpResponse<String> response =
                    httpClient.send(request, BodyHandlers.ofString());

            // Print the response for testing
            System.out.println(response.body());

            // Return the JSON data to the caller
            return response.body();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}