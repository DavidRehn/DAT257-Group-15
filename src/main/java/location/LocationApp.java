
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
import location.builder.LabelBuilder;
import location.builder.LabelDirector;
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
import location.builder.ComboBoxBuilder;
import location.builder.TextInputBuilder;
import location.builder.ButtonBuilder;
import location.builder.Row;
import location.builder.VBoxBuilder;
import location.builder.ScrollPaneBuilder;


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


        LabelBuilder titleBuilder = new LabelBuilder();
        director.constructNameOfLabel(
                titleBuilder,
                "Location and Solar Panel Info"
        ); Label title = titleBuilder.getLabel();

        //Label locationTitle = new Label("Location");

//Back to main page

        ButtonBuilder backButtonBuilder = new ButtonBuilder();
        backButtonBuilder.setLabelName("← Back to main page");

        Button backButton = backButtonBuilder.getButton();

        backButtonBuilder.setEvent(event -> {
            root.viewStart(stage);
        });

// Longitude
        TextInputBuilder longitudeFieldBuilder = new TextInputBuilder();
        longitudeFieldBuilder.setPromptText("Enter longitude");

        TextField longitudeField = longitudeFieldBuilder.getTextField();
        longitudeField.setMaxWidth(220);
        if(appData.Get("LocationData")!=null){
            longitudeField.setText(""+locationData.Longitude());
        }
        

// Check longitude
        ButtonBuilder checkButtonBuilder = new ButtonBuilder();
        checkButtonBuilder.setLabelName("Check longitude");

        Button checkButton = checkButtonBuilder.getButton();

        LabelBuilder errorMessageBuilder = new LabelBuilder();
        director.constructNameOfLabel(errorMessageBuilder, "");
        Label errorMessage = errorMessageBuilder.getLabel();

        checkButtonBuilder.setEvent(event -> {
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
        TextInputBuilder latitudeFieldBuilder = new TextInputBuilder();
        latitudeFieldBuilder.setPromptText("Enter latitude");

        TextField latitudeField = latitudeFieldBuilder.getTextField();
        latitudeField.setMaxWidth(220);

        if(appData.Get("LocationData")!=null){
            latitudeField.setText(""+locationData.Latitude());
        }

// Check latitude
        ButtonBuilder latitudeButtonBuilder = new ButtonBuilder();
        latitudeButtonBuilder.setLabelName("Check latitude");

        Button latitudeButton = latitudeButtonBuilder.getButton();

        LabelBuilder latitudeMessageBuilder = new LabelBuilder();
        director.constructNameOfLabel(latitudeMessageBuilder, "");
        Label latitudeMessage = latitudeMessageBuilder.getLabel();

        latitudeButtonBuilder.setEvent(event -> {

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
        ComboBoxBuilder zoneListBuilder = new ComboBoxBuilder();

        zoneListBuilder.setItems(
                "SE1",
                "SE2",
                "SE3",
                "SE4"
        );

        zoneListBuilder.setPromptText("Choose zone");
        zoneListBuilder.setWidth(185);

        if (appData.Get("ZoneData") != null) {
            zoneListBuilder.setValue(zoneData.Zone());
        }

        ComboBox<String> zoneList =
                zoneListBuilder.getComboBox();

// Solar panel area
        TextInputBuilder areaFieldBuilder = new TextInputBuilder();
        areaFieldBuilder.setPromptText("Area (m²)");

        TextField areaField = areaFieldBuilder.getTextField();
        areaField.setMaxWidth(220);

        if(appData.Get("SolarPanel")!=null){
            areaField.setText(""+solarPanel.Area());
        }

// Check solar panel area
        ButtonBuilder areaButtonBuilder = new ButtonBuilder();
        areaButtonBuilder.setLabelName("Check area");

        Button areaButton = areaButtonBuilder.getButton();

        LabelBuilder areaMessageBuilder = new LabelBuilder();
        director.constructNameOfLabel(areaMessageBuilder, "");
        Label areaMessage = areaMessageBuilder.getLabel();

        areaButtonBuilder.setEvent(event -> {

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
        TextInputBuilder tiltFieldBuilder = new TextInputBuilder();
        tiltFieldBuilder.setPromptText("Tilt (degrees)");

        TextField tiltField = tiltFieldBuilder.getTextField();
        tiltField.setMaxWidth(220);
        if(appData.Get("SolarPanel")!=null){
            tiltField.setText(""+solarPanel.Tilt());
        }

// Check solar panel tilt
        ButtonBuilder tiltButtonBuilder = new ButtonBuilder();
        tiltButtonBuilder.setLabelName("Check tilt");

        Button tiltButton = tiltButtonBuilder.getButton();

        LabelBuilder tiltMessageBuilder = new LabelBuilder();
        director.constructNameOfLabel(tiltMessageBuilder, "");
        Label tiltMessage = tiltMessageBuilder.getLabel();

        tiltButtonBuilder.setEvent(event -> {

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
        TextInputBuilder efficiencyFieldBuilder = new TextInputBuilder();
        efficiencyFieldBuilder.setPromptText("efficiency (0.0 to 1.0)");

        TextField efficiencyField = efficiencyFieldBuilder.getTextField();
        efficiencyField.setMaxWidth(220);

        if(appData.Get("SolarPanel")!=null){
            efficiencyField.setText(""+solarPanel.Efficiency());
        }
// Solar panel direction
        ComboBoxBuilder directionListBuilder = new ComboBoxBuilder();

        directionListBuilder.setItems(
                "South",
                "Southwest",
                "West",
                "Northwest",
                "North",
                "Northeast",
                "East",
                "Southeast"
        );

        directionListBuilder.setPromptText("Choose direction");
        directionListBuilder.setWidth(185);

        if (appData.Get("SolarPanel") != null) {
            directionListBuilder.setValue(
                    solarPanel.Direction().toString().toLowerCase()
            );
        }

        ComboBox<String> directionList =
                directionListBuilder.getComboBox();
 // Check solar panel direction
        ButtonBuilder directionButtonBuilder = new ButtonBuilder();
        directionButtonBuilder.setLabelName("Check direction");

        Button directionButton = directionButtonBuilder.getButton();

        LabelBuilder directionMessageBuilder = new LabelBuilder();
        director.constructNameOfLabel(directionMessageBuilder, "");
        Label directionMessage = directionMessageBuilder.getLabel();

        directionButtonBuilder.setEvent(event -> {

            String direction = directionList.getValue();

            if (direction == null) {
                directionMessage.setText("Please choose a direction");
            } else {
                directionMessage.setText("Valid direction: " + direction);
            }

        });


 // Check all user inputs

        ButtonBuilder checkAllButtonBuilder = new ButtonBuilder();
        checkAllButtonBuilder.setLabelName("Check All");

        Button checkAllButton = checkAllButtonBuilder.getButton();

        LabelBuilder allMessageBuilder = new LabelBuilder();
        director.constructNameOfLabel(allMessageBuilder, "");
        Label allMessage = allMessageBuilder.getLabel();

        checkAllButtonBuilder.setEvent(event -> {

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
                    return;
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
                    return;
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
                    return;
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

                    allMessage.setText("Please enter a valid whole number for efficiency");
                    return;
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
                    return;
                }

                allMessage.setText("All inputs are valid");

                                                
// Save the user's information
                
                appData.Store("LocationData",new LocationData(longitude, latitude));
                appData.Store("ZoneData",new ZoneData(zoneList.getValue()));
                appData.Store("SolarPanel", new SolarPanel(area,tilt,SolarPanel.directionConverter(directionList.getValue().toUpperCase()),efficiency));
                
                appData.SaveToDisc();

            }//end of else

            
        });

        ButtonBuilder getForecastButtonBuilder = new ButtonBuilder();
        getForecastButtonBuilder.setLabelName("Get Forecast");

        Button getForecastButton = getForecastButtonBuilder.getButton();
        getForecastButtonBuilder.setEvent(event -> {
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

                       // End of for loop

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
    

        // Create the layout

        VBoxBuilder layoutBuilder = new VBoxBuilder();
        layoutBuilder.setSpacing(15);

        VBox layout = layoutBuilder.getVBox();
        


// Put the back button on the left
        Row backRowBuilder = new Row(backButton);

        HBox backRow = backRowBuilder.getRow();

        backRow.setPadding(new Insets(0, 0, 0, 20));
        backRow.setMaxWidth(Double.MAX_VALUE);


// Longitude row
        LabelBuilder longitudeLabelBuilder = new LabelBuilder();

        director.constructNameOfLabelWidth(
                longitudeLabelBuilder,
                "Longitude"
        );

        Label longitudeLabel = longitudeLabelBuilder.getLabel();

        Row longitudeRowBuilder =
                new Row(longitudeLabel, longitudeField);

        HBox longitudeRow = longitudeRowBuilder.getRow();


// Latitude row
        LabelBuilder latitudeLabelBuilder = new LabelBuilder();

        director.constructNameOfLabelWidth(
                latitudeLabelBuilder,
                "Latitude"
        );

        Label latitudeLabel = latitudeLabelBuilder.getLabel();

        Row latitudeRowBuilder =
                new Row(latitudeLabel, latitudeField);

        HBox latitudeRow = latitudeRowBuilder.getRow();


// Area row
        LabelBuilder areaLabelBuilder = new LabelBuilder();

        director.constructNameOfLabelWidth(
                areaLabelBuilder,
                "Area"
        );

        Label areaLabel = areaLabelBuilder.getLabel();

        Row areaRowBuilder =
                new Row(areaLabel, areaField);

        HBox areaRow = areaRowBuilder.getRow();


// Tilt row
        LabelBuilder tiltLabelBuilder = new LabelBuilder();

        director.constructNameOfLabelWidth(
                tiltLabelBuilder,
                "Tilt"
        );

        Label tiltLabel = tiltLabelBuilder.getLabel();

        Row tiltRowBuilder =
                new Row(tiltLabel, tiltField);

        HBox tiltRow = tiltRowBuilder.getRow();


// Efficiency row
        LabelBuilder efficiencyLabelBuilder = new LabelBuilder();

        director.constructNameOfLabelWidth(
                efficiencyLabelBuilder,
                "Efficiency"
        );

        Label efficiencyLabel =
                efficiencyLabelBuilder.getLabel();

        Row efficiencyRowBuilder =
                new Row(efficiencyLabel, efficiencyField);

        HBox efficiencyRow =
                efficiencyRowBuilder.getRow();

// Direction row
        LabelBuilder builder3 = new LabelBuilder();
        director.constructNameOfLabelWidth(builder3, "Direction");

        Row directionRowBuilder =
                new Row(builder3.getLabel(), directionList);

        HBox zoneRow3 = directionRowBuilder.getRow();




        layoutBuilder.addChildren(title);
        layoutBuilder.addChildren(backRow);
        
// Zone row
        
        //Builder
        LabelBuilder buildera = new LabelBuilder();
        director.constructNameOfLabelWidth(buildera, "Zone"); //
        Row zoneRowBuilder =
                new Row(buildera.getLabel(), zoneList);

        HBox zoneRowe = zoneRowBuilder.getRow();
        layoutBuilder.addChildren(zoneRowe);             //

//Button row
        Row buttonRowBuilder =
                new Row(checkAllButton, getForecastButton);

        HBox buttonRow = buttonRowBuilder.getRow();
// Location section
        //layout.getChildren().add(locationTitle); //old
        LabelBuilder builder2 = new LabelBuilder(); 
        director.constructNameOfLabel(builder2, "Location");
        layoutBuilder.addChildren(builder2.getLabel());
        //

        layoutBuilder.addChildren(longitudeRow);
        layoutBuilder.addChildren(latitudeRow);


        LabelBuilder builder = new LabelBuilder();
        director.constructSolarDetails(builder);
        layoutBuilder.addChildren(builder.getLabel());

// Solar panel section
        layoutBuilder.addChildren(areaRow);
        layoutBuilder.addChildren(tiltRow);
        layoutBuilder.addChildren(efficiencyRow);
        //layout.getChildren().add(directionRow); old
        layoutBuilder.addChildren(zoneRow3); //new B

        // Check everything
        layoutBuilder.addChildren(buttonRow);
        layoutBuilder.addChildren(allMessage);

        layoutBuilder.setAlignment(Pos.CENTER);

// Create and display the window

        ScrollPaneBuilder scrollPaneBuilder = new ScrollPaneBuilder();

        scrollPaneBuilder.setContent(layout);
        scrollPaneBuilder.setFitToWidth(true);

        ScrollPane scrollPane = scrollPaneBuilder.getScrollPane();

        Scene scene = new Scene(scrollPane, 520, 650);

        stage.setTitle("Solar App");
        stage.setScene(scene);
        stage.show();
    }

    


}