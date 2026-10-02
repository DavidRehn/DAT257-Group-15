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

public SolarRadiationRetreiver(HTTPSolarRequest request) {
    this();

    apiUrl += String.format(
            Locale.US,
            "?latitude=%f&longitude=%f"
                    + "&hourly=global_tilted_irradiance"
                    + "&tilt=%d&azimuth=%d"
                    + "&forecast_days=%d"
                    + "&timezone=auto",
            request.Latitude(),
            request.Longitude(),
            request.Tilt(),
            request.Azimuth(),
            request.Days()
        );
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
            //System.out.println(response.body());

            // Return the JSON data to the caller
            return response.body();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
  
public void GetWeatherInfo(HTTPSolarRequest requestParams) {

    String requestUrl = String.format(
            Locale.US,
            "https://api.open-meteo.com/v1/forecast"
                    + "?latitude=%f&longitude=%f"
                    + "&hourly=global_tilted_irradiance, temperature_2m"
                    + "&tilt=%d&azimuth=%d"
                    + "&forecast_days=%d&timezone=auto",
            requestParams.Latitude(),
            requestParams.Longitude(),
            requestParams.Tilt(),
            requestParams.Azimuth(),
            requestParams.Days()
    );

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(requestUrl))
            .GET()
            .build();

    try {
        HttpResponse<String> response =
                httpClient.send(request, BodyHandlers.ofString());

        weatherParser.ParseToJson(response.body());
        //weatherParser.PrintTimestamps();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}