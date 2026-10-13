
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
import location.builder.LBuilder;
import location.builder.LabelBuilder;
import location.builder.LabelDirector;
import location.builder.MultiChoiceBuilder;
import location.builder.TextInputBuilder;
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

    LabelDirector director;


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

// Create the Directory
        director = new LabelDirector();

 // Create the title
    
        Label title = new Label("Location and Solar Panel Info");

//Back to main page

        Button backButton = new Button("← Back to main page");

        backButton.setOnAction(event -> {
            root.viewStart(stage);
        });

// Zone 
    MultiChoiceBuilder builderZoneM = new MultiChoiceBuilder();
    String[] tempa = {"SE1", "SE2", "SE3", "SE4"};
    String[] tempa2 ={ "Choose zone",""+zoneData.Zone(), "ZoneData"};
    director.constructMCW(builderZoneM,tempa2 , tempa );

// Solar panel direction
    MultiChoiceBuilder builderDirectionM = new MultiChoiceBuilder();
    String[] tempb = {"South", "Southwest", "West", "Northwest", "North", "Northeast", "East", "Southeast"};
    String[] tempb2={"Choose direction", ""+solarPanel.Direction().toString().toLowerCase(), "SolarPanel"};
    director.constructMCW(builderDirectionM, tempb2, tempb );

 // Check all user inputs
        Button checkAllButton = new Button("Check All");
        Label allMessage = new Label("");
/*
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
        */

     
//WetherAPI Call button
        Button getForecastButton = new Button("Get Forecast");
        /* 
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

        // Create the layout
        
        VBox layout = new VBox(15);
        
// Put the back button on the left
        HBox backRow = new HBox(backButton);

        backRow.setAlignment(Pos.CENTER_LEFT);
        backRow.setPadding(new Insets(0, 0, 0, 20));
        backRow.setMaxWidth(Double.MAX_VALUE);

// Longitude row
    LabelBuilder builderLongitudeL = new LabelBuilder();
    //String[] a = {"Longitude"};
    String[] aq = { "Enter longitude", ""+locationData.Longitude(), "LocationData"};
    director.constructFirstRowW(builderLongitudeL, new String[]{"Longitude"} );
    TextInputBuilder builderLongitudeTL = new TextInputBuilder();    
    director.constructAddingRowTF(builderLongitudeTL, aq, builderLongitudeL.getRowList());
    HBox longitudeRow =builderLongitudeTL.getHBox();
        //HBox longitudeRow = new HBox(15, builderLongitudeL.getLabel(), builderLongitudeTL.getTextField());
        //longitudeRow.setAlignment(Pos.CENTER);

// Latitude row
       
    LabelBuilder builderLatitudeL = new LabelBuilder();
    String[] ads = {"Latitude"};
    String[] tempd={ "Enter latitude", ""+locationData.Latitude(), "LocationData"};
    director.constructFirstRowW(builderLatitudeL, ads); 
    TextInputBuilder builderLatitudeTL = new TextInputBuilder();
    director.constructAddingRowTF(builderLatitudeTL, tempd, builderLatitudeL.getRowList());

    HBox latitudeRow = builderLatitudeTL.getHBox();
    //new HBox(15, builderLatitudeL.getLabel(), builderLatitudeTL.getTextField());
    //latitudeRow.setAlignment(Pos.CENTER);


// Area row 
    LabelBuilder builderAreaL = new LabelBuilder();
    String[] ars = {""+solarPanel.Area(),  "Area"};
    String[] ar = {  "Area (m²)", ""+solarPanel.Area(), "SolarPanel"};
    director.constructFirstRowW(builderAreaL, ars); 

    TextInputBuilder builderAreaTL = new TextInputBuilder();
        director.constructAddingRowTF(builderAreaTL, ar,builderAreaL.getRowList());
    HBox areaRow = builderAreaTL.getHBox();
      
// Tilt row
    LabelBuilder builderTiltL = new LabelBuilder();
        String[] afs = {"Tilt"};
        String[] af = {"Tilt (degrees)", ""+solarPanel.Tilt(), "SolarPanel"};
    director.constructFirstRowW(builderTiltL, afs); 
    TextInputBuilder builderTiltTL = new TextInputBuilder();
    director.constructAddingRowTF(builderTiltTL, af, builderTiltL.getRowList()); 
    HBox tiltRow = builderTiltTL.getHBox();

// Efficiency row
    String[] aww = {"Efficiency"};
    String[] aw = {"efficiency (0.0 to 1.0)", ""+solarPanel.Efficiency(), "SolarPanel"};
    LabelBuilder builderEfficiencyL = new LabelBuilder(); 
    director.constructFirstRowW(builderEfficiencyL, aww);

    TextInputBuilder builderEfficiencyTL = new TextInputBuilder();
    director.constructAddingRowTF(builderEfficiencyTL, aw, builderEfficiencyL.getRowList());
  
        HBox efficiencyRow = new HBox(15, builderEfficiencyL.getLabel()); 
        efficiencyRow.getChildren().add(builderEfficiencyTL.getTextField()); //DDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
        efficiencyRow.setAlignment(Pos.CENTER);

// Direction row
        String[] ad = { "Direction"};
        LabelBuilder builder3 = new LabelBuilder();
        director.constructNameW(builder3, ad); 

        HBox zoneRow3 = new HBox(15, builder3.getLabel(), builderDirectionM.getComboBox());
        zoneRow3.setAlignment(Pos.CENTER);

        layout.getChildren().add(title);
        layout.getChildren().add(backRow);
        
// Zone row
        String[] as = {"Zone" };
        LabelBuilder buildera = new LabelBuilder();
        director.constructNameW(buildera, as); 

        HBox zoneRowe = new HBox(15, buildera.getLabel(), builderZoneM.getComboBox());
        zoneRowe.setAlignment(Pos.CENTER);
        layout.getChildren().add(zoneRowe);               


//Button row
    HBox buttonRow = new HBox(15, checkAllButton, getForecastButton);
        buttonRow.setAlignment(Pos.CENTER);

// Location section
        //layout.getChildren().add(locationTitle); //old
        LabelBuilder builder2 = new LabelBuilder(); 
            String[] ag = {"Location" };

        director.constructName(builder2, ag);
        layout.getChildren().add(builder2.getLabel());  //

        layout.getChildren().add(longitudeRow);

        layout.getChildren().add(latitudeRow);


//Test of consept
        //LBuilder a[] = new LBuilder[2];

        LabelBuilder builder = new LabelBuilder();
        String[] oo={"Solar panel details"};
        director.constructName(builder, oo);
        layout.getChildren().add(builder.getLabel());

// Solar panel section
        layout.getChildren().add(areaRow);
        layout.getChildren().add(tiltRow);
        layout.getChildren().add(efficiencyRow);
        //layout.getChildren().add(directionRow); old
        layout.getChildren().add(zoneRow3); //new B

        // Check everything
        layout.getChildren().add(buttonRow);
        layout.getChildren().add(allMessage);

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