package location;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Platform;
import other.LocationData;
import other.SolarPanel;
import other.TimestampData;
import ui.LanguageManager;
import weatherdata.EstimationTimestamp;
import weatherdata.HTTPSolarRequest;
import weatherdata.SolarRadiationRetreiver;
import weatherdata.WeatherParser;
import other.Model;
import javafx.scene.control.Label;



public class GetNewForcast {

    private LocationData locationData;
    private SolarPanel solarPanel;
    private Model appData;
    
    public GetNewForcast(Model appData){
        this.appData = appData;
    }
   
    public void loadTheForecast(int key) {
    
        locationData=((LocationData)appData.Get("LocationData",key));
        solarPanel = ((SolarPanel)appData.Get("SolarPanel",key));

        //last line of defense.
        if(locationData==null || solarPanel==null){
            return; 
        }

        HTTPSolarRequest weatherRequest = new HTTPSolarRequest(
            locationData.Latitude(), 
            locationData.Longitude(),
            solarPanel.Tilt(),
            10, 
            solarPanel.Direction()
        );
                
        Thread apiThread = new Thread(() -> {
            SolarRadiationRetreiver retriever = new SolarRadiationRetreiver(
                weatherRequest,
                new WeatherParser(), 
                appData
            );

            TimestampData timestampData = (TimestampData) appData.Get("TimestampData");

            boolean saved = appData.SaveToDisc();
            if (saved) {
                System.out.println("Forecast saved successfully");
            } else {
                System.err.println("Could not save forecast");
            }

            ArrayList<EstimationTimestamp> timestamps = new ArrayList<>();
            for (HashMap.Entry<String, Double> entry : timestampData.GetTimestamps().entrySet()) {
                timestamps.add(new EstimationTimestamp(LocalDateTime.parse(entry.getKey()), entry.getValue()));
            }
            timestamps.sort(null);
            
        });

        apiThread.setDaemon(true);
        apiThread.start();
    }
}
