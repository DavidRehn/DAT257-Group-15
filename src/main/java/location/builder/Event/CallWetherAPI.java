package location.builder.Event;

import javafx.scene.control.Button;
import location.builder.BBuilder;

public class CallWetherAPI implements BBuilder{

    private int temp;

    public CallWetherAPI(){

    }

    public void setEvent(BBuilder bBuilder){
        /* 
        Button getForecastButton = new Button("Get Forecast");
        
        getForecastButton.setOnAction(event -> {
            Direction selectedDirection = Direction.valueOf((solarPanel.Direction().toString()).toUpperCase());
                       
            locationData = (LocationData)appData.Get("LocationData");
            HTTPSolarRequest weatherRequest = new HTTPSolarRequest(
                locationData.Longitude(),
                locationData.Latitude(),
                solarPanel.Tilt(),
                1,
                selectedDirection
                );

            System.out.println(
                "Latitude: " + weatherRequest.Latitude()
                );

            Thread apiThread;
            apiThread = new Thread(() -> {
                SolarRadiationRetreiver retriever =
                    new SolarRadiationRetreiver(weatherRequest, new WeatherParser(), appData);

                boolean success = retriever.GetWeatherInfo();

                if (success) {
                    System.out.println("API data received successfully!");

                    timestampData = (TimestampData)appData.Get("TimestampData");
                    ArrayList<EstimationTimestamp> timestamps = new ArrayList<>();

                    for (HashMap.Entry<String, Double> en : timestampData.GetTimestamps().entrySet()) {
                        timestamps.add(new EstimationTimestamp(LocalDateTime.parse(en.getKey()), en.getValue()));
                    }
                    timestamps.sort(null);

                    Platform.runLater(() -> {
                        StartMenu.setForecastData(timestamps);
                        allMessage.setText(
                            "Forecast loaded! Back to main page."
                        );
                    });

                } else {
                    System.out.println("Failed to receive API data.");
                }

            }); // End of Thread

            apiThread.setDaemon(true);
            apiThread.start();
                                                
        });
    */
    }

}
