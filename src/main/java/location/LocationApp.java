
package location;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import other.AppInterface;
import other.Direction;
import other.LocationData;
import other.Model;
import other.SolarPanel;
import other.SubAppInterface;
import other.TimestampData;
import other.ZoneData;
import saveload.JsonSaveLoad;
import saveload.SaveLoadInterface;
import weatherdata.EstimationTimestamp;
import weatherdata.HTTPSolarRequest;
import weatherdata.SolarRadiationRetreiver;
import weatherdata.WeatherParser;
//import weatherdata.CallWeatherAPI;
public  class LocationApp extends Application implements SubAppInterface {

    private Model appData;
    public void linkAppData(Model m){
        this.appData=m;
    }

    private AppInterface root;
    public void linkRoot(AppInterface root){
        this.root=root;
    }


    //CallWeatherAPI apiAccess = new CallWeatherAPI();
    private SaveLoadInterface saveLoad = new JsonSaveLoad();
    private Hashtable<String, Object> saveData = new Hashtable<>();
    private SolarPanel solarPanel;
    private LocationData locationData;
    private ZoneData zoneData;
    private TimestampData timestampData;
    @Override
    public void start(Stage stage) {
        if(appData.Get("SolarPanel")!=null){
            solarPanel = (SolarPanel)appData.Get("SolarPanel");
        }
        if(appData.Get("LocationData")!=null){
            locationData = (LocationData)appData.Get("LocationData");
        }
        if(appData.Get("ZoneData")!=null){
            zoneData = (ZoneData)appData.Get("ZoneData");
        }
        if(appData.Get("TimestampData")!=null){
            timestampData = (TimestampData)appData.Get("TimestampData");
        }
        
 // Create the title

        Label title = new Label("Location and Solar Panel Info");

        Label locationTitle = new Label("Location");

        Label solarPanelTitle = new Label("Solar panel details");
//Back to main page

        Button backButton = new Button("← Back to main page");


        backButton.setOnAction(event -> {
            root.viewStart(stage);
        });

// Longitude
        TextField longitudeField = new TextField();
        longitudeField.setPromptText("Enter longitude");
        longitudeField.setMaxWidth(220);
        if(appData.Get("LocationData")!=null){
            longitudeField.setText(""+locationData.Longitude());
        }
        

// Check longitude
        Button checkButton = new Button("Check longitude");

        Label errorMessage = new Label("");

        checkButton.setOnAction(event -> {

            try {
                double longitude =
                        Double.parseDouble(longitudeField.getText());

                if (longitude >= -180 && longitude <= 180) {
                    errorMessage.setText("Valid longitude");
                } else {
                    errorMessage.setText("Longitude must be between -180 and 180");
                }

            } catch (NumberFormatException e) {
                errorMessage.setText("Please enter a valid number");
            }

        });


// Latitude
        TextField latitudeField = new TextField();
        latitudeField.setPromptText("Enter latitude");
        latitudeField.setMaxWidth(220);
        if(appData.Get("LocationData")!=null){
            latitudeField.setText(""+locationData.Latitude());
        }

// Check latitude
        Button latitudeButton = new Button("Check latitude");

        Label latitudeMessage = new Label("");

        latitudeButton.setOnAction(event -> {

            try {
                double latitude =
                        Double.parseDouble(latitudeField.getText());

                if (latitude >= -90 && latitude <= 90) {
                    latitudeMessage.setText("Valid latitude");
                } else {
                    latitudeMessage.setText(
                            "Latitude must be between -90 and 90"
                    );
                }

            } catch (NumberFormatException e) {
                latitudeMessage.setText(
                        "Please enter a valid number"
                );
            }

        });
//
ComboBox<String> zoneList = new ComboBox<>();

        zoneList.getItems().addAll(
                "SE1",
                "SE2",
                "SE3",
                "SE4"
        );

        zoneList.setPromptText("Choose zone");

        zoneList.setPrefWidth(185);
        zoneList.setMaxWidth(185);
        if(appData.Get("ZoneData")!=null){
            zoneList.setValue(zoneData.Zone());
        }

// Solar panel area
        TextField areaField = new TextField();
        areaField.setPromptText("Area (m²)");
        areaField.setMaxWidth(220);
        if(appData.Get("SolarPanel")!=null){
            areaField.setText(""+solarPanel.Area());
        }

// Check solar panel area
        Button areaButton = new Button("Check area");

        Label areaMessage = new Label("");

        areaButton.setOnAction(event -> {

            try {
                double area =
                        Double.parseDouble(areaField.getText());

                if (area > 0 && Double.isFinite(area)) {
                    areaMessage.setText("Valid area");
                } else {
                    areaMessage.setText("Area must be greater than 0");
                }

            } catch (NumberFormatException e) {
                areaMessage.setText("Please enter a valid number");
            }

        });

// Solar panel tilt
        TextField tiltField = new TextField();
        tiltField.setPromptText("Tilt (degrees)");
        tiltField.setMaxWidth(220);
        if(appData.Get("SolarPanel")!=null){
            tiltField.setText(""+solarPanel.Tilt());
        }

// Check solar panel tilt
        Button tiltButton = new Button("Check tilt");

        Label tiltMessage = new Label("");

        tiltButton.setOnAction(event -> {

            try {
                int tilt = Integer.parseInt(tiltField.getText());

                if (tilt >= 0 && tilt <= 90) {
                    tiltMessage.setText("Valid tilt");
                } else {
                    tiltMessage.setText(
                            "Tilt must be between 0 and 90"
                    );
                }

            } catch (NumberFormatException e) {
                tiltMessage.setText(
                        "Please enter a valid whole number"
                );
            }

        });
// Solar panel efficiency
        TextField efficiencyField = new TextField();
        efficiencyField.setPromptText("efficiency (0.0 to 1.0)");
        efficiencyField.setMaxWidth(220);
        if(appData.Get("SolarPanel")!=null){
            efficiencyField.setText(""+solarPanel.Efficiency());
        }
// Solar panel direction
        ComboBox<String> directionList = new ComboBox<>();

        directionList.getItems().addAll(
                "South",
                "Southwest",
                "West",
                "Northwest",
                "North",
                "Northeast",
                "East",
                "Southeast"
        );

        directionList.setPromptText("Choose direction");

        directionList.setPrefWidth(185);
        directionList.setMaxWidth(185);
        if(appData.Get("SolarPanel")!=null){
            directionList.setValue(solarPanel.Direction().toString().toLowerCase());
        }

 // Check solar panel direction
        Button directionButton = new Button("Check direction");

        Label directionMessage = new Label("");

        directionButton.setOnAction(event -> {

            String direction = directionList.getValue();

            if (direction == null) {
                directionMessage.setText("Please choose a direction");
            } else {
                directionMessage.setText("Valid direction: " + direction);
            }

        });


 // Check all user inputs
 
        Button checkAllButton = new Button("Check All");

        Label allMessage = new Label("");

        checkAllButton.setOnAction(event -> {

            if (longitudeField.getText().isBlank()
                    || latitudeField.getText().isBlank()
                    || areaField.getText().isBlank()
                    || tiltField.getText().isBlank()
                    || efficiencyField.getText().isBlank()
                    || directionList.getValue() == null
                    || zoneList.getValue() == null) {

                allMessage.setText("Please fill in all fields");

            } else {

//Check Longitude
                double longitude=0;
                try {

                    longitude = Double.parseDouble(longitudeField.getText());

                    if (longitude < -180 || longitude > 180 || !Double.isFinite(longitude)) {
                        //Sets message at the bottom of the setting screen
                        allMessage.setText("Longitude must be between -180 and 180");
                        
                        //Exit early
                        return;


                    } 
                } catch (NumberFormatException e) {

                    allMessage.setText("Please enter a valid longitude");

                }
                    
 // Check Latitude
                double latitude=0;
                try {

                    latitude = Double.parseDouble(latitudeField.getText());

                    if (latitude < -90 || latitude > 90 || !Double.isFinite(latitude)) {

                        allMessage.setText(
                            "Latitude must be between -90 and 90"
                        );
                        return;

                    }
                } catch (NumberFormatException e) {

                    allMessage.setText("Please enter a valid latitude");

                }

 // Check solar panel area
                double area=0;
                try {
                    area = Double.parseDouble(areaField.getText());

                    if (area <= 0 || !Double.isFinite(area)) {

                        allMessage.setText("Area must be greater than 0");
                        return;
                    }
                } catch (NumberFormatException e) {

                    allMessage.setText("Please enter a valid area");

                }

// Check solar panel tilt
                int tilt=0;
                try {

                    tilt = Integer.parseInt(tiltField.getText());

                    if (tilt < 0 || tilt > 90) {

                        allMessage.setText("Tilt must be between 0 and 90");
                        return;
                    }
                } catch (NumberFormatException e) {

                    allMessage.setText("Please enter a valid whole number for tilt");

                }
                    
// Check solar panel efficiency
                double efficiency=0;
                try {
                    efficiency = Double.parseDouble(efficiencyField.getText());

                    if (efficiency <= 0.0||efficiency > 1.0|| !Double.isFinite(efficiency)) {

                        allMessage.setText("Efficiency must be between 0.0 and 1.0");
                        return;
                    }
                    
                } catch (NumberFormatException e) {

                    allMessage.setText("Please enter a valid number for tilt");
                    
                }

                allMessage.setText("All inputs are valid");

                                                
// Save the user's information
                
                appData.Store("LocationData",new LocationData(longitude, latitude));
                appData.Store("ZoneData",new ZoneData(zoneList.getValue()));
                appData.Store("SolarPanel", new SolarPanel(area,tilt,SolarPanel.directionConverter(directionList.getValue().toUpperCase()),efficiency));
                
                appData.SaveToDisc();

            }//end of else

            
        });

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

                    /* 
                        for (;;){
                            String time = times.getString(i);

                            double irradiance = radiation
                                .getJsonNumber(i)
                                .doubleValue();

                            System.out.println(
                                time + " -> " + irradiance + " W/m²"
                            );
                        }*/      // End of for loop

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
        // Create a list of cities
        /*
        ComboBox<String> cityList = new ComboBox<>();

        cityList.getItems().addAll(
                "Göteborg",
                "Stockholm",
                "Malmö",
                "Uppsala"
        );

        cityList.setPromptText("Choose your city");

        // Create the save button
        Button saveButton = new Button("Save Location");

        // Create a message for the user
        Label message = new Label("");

        // Create the location manager
        LocationManager location = new LocationManager();

        String savedCity = location.getCity();

        if (cityList.getItems().contains(savedCity)) {

            cityList.setValue(savedCity);

        }

        // What happens when the user clicks Save
        saveButton.setOnAction(event -> {

            String city = cityList.getValue();

            if (city != null) {

                location.saveCity(city);

                message.setText("Saved: " + city);

            } else {

                message.setText("Please choose a city first.");

            }
        });

        // Create the layout
        */
        VBox layout = new VBox(15);

// Put the back button on the left
        HBox backRow = new HBox(backButton);

        backRow.setAlignment(Pos.CENTER_LEFT);
        backRow.setPadding(new Insets(0, 0, 0, 20));
        backRow.setMaxWidth(Double.MAX_VALUE);

// Longitude row
        Label longitudeLabel = new Label("Longitude");
        longitudeLabel.setPrefWidth(90);

        HBox longitudeRow = new HBox(15, longitudeLabel, longitudeField);
        longitudeRow.setAlignment(Pos.CENTER);

// Latitude row
        Label latitudeLabel = new Label("Latitude");
        latitudeLabel.setPrefWidth(90);

        HBox latitudeRow = new HBox(15, latitudeLabel, latitudeField);
        latitudeRow.setAlignment(Pos.CENTER);


// Area row
        Label areaLabel = new Label("Area");
        areaLabel.setPrefWidth(90);

        HBox areaRow = new HBox(15, areaLabel, areaField);

// Tilt row
        Label tiltLabel = new Label("Tilt");
        tiltLabel.setPrefWidth(90);

        HBox tiltRow = new HBox(15, tiltLabel, tiltField);
        tiltRow.setAlignment(Pos.CENTER);

// Efficiency row
        Label efficiencyLabel = new Label("Efficiency");
        efficiencyLabel.setPrefWidth(90);

        HBox efficiencyRow = new HBox(15, efficiencyLabel, efficiencyField);
        efficiencyRow.setAlignment(Pos.CENTER);

// Direction row
        Label directionLabel = new Label("Direction");
        directionLabel.setPrefWidth(90);

        HBox directionRow = new HBox(15, directionLabel, directionList);
        directionRow.setAlignment(Pos.CENTER);

        areaRow.setAlignment(Pos.CENTER);

        layout.getChildren().add(title);
        layout.getChildren().add(backRow);
        
// Zone row
        Label zoneLabel = new Label("Zone");
        zoneLabel.setPrefWidth(90);

        HBox zoneRow = new HBox(15, zoneLabel, zoneList);
        zoneRow.setAlignment(Pos.CENTER);


//Button row
    HBox buttonRow = new HBox(15, checkAllButton, getForecastButton);
        buttonRow.setAlignment(Pos.CENTER);

// Location section
        layout.getChildren().add(locationTitle);

        layout.getChildren().add(longitudeRow);
       // layout.getChildren().add(checkButton);
       // layout.getChildren().add(errorMessage);

        layout.getChildren().add(latitudeRow);
       // layout.getChildren().add(latitudeButton);
       // layout.getChildren().add(latitudeMessage);

// Solar panel section
        layout.getChildren().add(solarPanelTitle);
        layout.getChildren().add(areaRow);
       // layout.getChildren().add(areaButton);
       // layout.getChildren().add(areaMessage);

        layout.getChildren().add(tiltRow);
        //layout.getChildren().add(tiltButton);
        // layout.getChildren().add(tiltMessage);
        
        layout.getChildren().add(efficiencyRow);
        
        layout.getChildren().add(directionRow);
        // layout.getChildren().add(directionButton);
        //layout.getChildren().add(directionMessage);

        // Check everything
        layout.getChildren().add(buttonRow);
        //layout.getChildren().add(checkAllButton);
        //layout.getChildren().add(getForecastButton);
        layout.getChildren().add(allMessage);
        // layout.getChildren().add(cityList);
        // layout.getChildren().add(saveButton);
        // layout.getChildren().add(message);

        layout.setAlignment(Pos.CENTER);

// Create and display the window

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane, 520, 650);

        stage.setTitle("Solar App");
        stage.setScene(scene);
        stage.show();
    }


}