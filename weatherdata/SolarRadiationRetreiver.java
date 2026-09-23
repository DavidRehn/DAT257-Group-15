package weatherdata;

import java.net.http.HttpClient;

public class SolarRadiationRetreiver{
    private String apiUrl = "https://api.open-meteo.com/v1/forecast";     // Open meteo api
    private final HttpClient httpClient;

    public SolarRadiationRetreiver(HTTPSolarRequest request){
        httpClient = HttpClient.newHttpClient();
        apiUrl += String.format("?latitude=%f&longitude=%f&hourly=global_tilted_irradiance&tilt=%d&azimuth=%d&forecast_days=%d", 
                                request.GetLatitude(), request.GetLongitude(), request.GetTilt(), request.GetAzimuth(), request.GetDays());
                                
        System.out.println(apiUrl);
    }


}