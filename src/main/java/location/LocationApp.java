package location;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import location.builder.ButtonBuilder;
import location.builder.ComboBoxBuilder;
import location.builder.LabelBuilder;
import location.builder.LabelDirector;
import location.builder.Row;
import location.builder.ScrollPaneBuilder;
import location.builder.TextInputBuilder;
import location.builder.VBoxBuilder;
import other.AppInterface;
import other.Direction;
import other.LocationData;
import other.Model;
import other.SolarPanel;
import other.SubAppInterface;
import other.TimestampData;
import other.ZoneData;
import weatherdata.EstimationTimestamp;
import weatherdata.HTTPSolarRequest;
import weatherdata.SolarRadiationRetreiver;
import weatherdata.WeatherParser;
public class LocationApp extends Application
        implements SubAppInterface {
    private Model appData;
    private AppInterface root;
    private SolarPanel solarPanel;
    private LocationData locationData;
    private ZoneData zoneData;
    private LabelDirector director;
    @Override
    public void start(Stage stage) {
        // Load saved data
        solarPanel = (SolarPanel) appData.Get("SolarPanel");
        locationData = (LocationData) appData.Get("LocationData");
        zoneData = (ZoneData) appData.Get("ZoneData");
        director = new LabelDirector();
        //  TITLE
        Label title = createLabel("Location and Solar Panel Info");
        //  BACK BUTTON
        Button backButton = createButton("← Back to main page", e -> root.viewStart(stage));
        //  INPUT FIELDS
        TextField longitudeField = createTextField("Enter longitude",
                locationData == null ? null : String.valueOf(locationData.Longitude()));
        TextField latitudeField = createTextField("Enter latitude",
                locationData == null ? null : String.valueOf(locationData.Latitude()));
        TextField areaField = createTextField("Area (m²)",
                solarPanel == null ? null : String.valueOf(solarPanel.Area()));
        TextField tiltField = createTextField("Tilt (degrees)",
                solarPanel == null ? null : String.valueOf(solarPanel.Tilt()));
        TextField efficiencyField = createTextField("Efficiency (0.0 to 1.0)",
                solarPanel == null ? null : String.valueOf(solarPanel.Efficiency()));
        // ZONE
        ComboBox<String> zoneList = createComboBox("Choose zone", 185,
                zoneData == null ? null : zoneData.Zone(), "SE1", "SE2", "SE3", "SE4");
        // DIRECTION
        ComboBox<String> directionList = createComboBox("Choose direction", 185,
                solarPanel == null ? null : directionText(solarPanel.Direction()),
                "South", "Southwest", "West", "Northwest", "North", "Northeast", "East", "Southeast");
        // MESSAGE
        Label allMessage = createLabel("");
        // CHECK ALL
        Button checkAllButton = createButton("Check All", event -> {
            // Empty fields
            if (longitudeField.getText().isBlank()  || latitudeField.getText().isBlank()
                    || areaField.getText().isBlank()       || tiltField.getText().isBlank()
                    || efficiencyField.getText().isBlank() || directionList.getValue() == null
                    || zoneList.getValue() == null) {allMessage.setText("Please fill in all fields");
                return;}
            // Longitude
            Double longitude = readDouble(longitudeField, allMessage, "Please enter a valid longitude");
            if (longitude == null) return;
            if (!check(longitude >= -180 && longitude <= 180 && Double.isFinite(longitude),
                    allMessage, "Longitude must be between -180 and 180")) return;
            // Latitude
            Double latitude = readDouble(latitudeField, allMessage, "Please enter a valid latitude");
            if (latitude == null) return;
            if (!check(latitude >= -90 && latitude <= 90 && Double.isFinite(latitude),
                    allMessage, "Latitude must be between -90 and 90")) return;
            // Area
            Double area = readDouble(areaField, allMessage, "Please enter a valid area");
            if (area == null) return;
            if (!check(area > 0 && Double.isFinite(area),
                    allMessage, "Area must be greater than 0")) return;
            // Tilt
            Integer tilt = readInt(tiltField, allMessage, "Please enter a valid whole number for tilt");
            if (tilt == null) return;
            if (!check(tilt >= 0 && tilt <= 90,
                    allMessage, "Tilt must be between 0 and 90")) return;
            // Efficiency
            Double efficiency = readDouble(efficiencyField, allMessage, "Please enter a valid number for efficiency");
            if (efficiency == null) return;
            if (!check(efficiency > 0 && efficiency <= 1 && Double.isFinite(efficiency),
                    allMessage, "Efficiency must be between 0.0 and 1.0")) return;
            // Save values
            locationData = new LocationData(longitude, latitude);
            zoneData     = new ZoneData(zoneList.getValue());
            solarPanel   = new SolarPanel(area, tilt, SolarPanel.directionConverter(
                    directionList.getValue().toUpperCase()), efficiency);
            appData.Store("LocationData", locationData);
            appData.Store("ZoneData", zoneData);
            appData.Store("SolarPanel", solarPanel);
            appData.SaveToDisc();
            allMessage.setText("All inputs are valid");});
        // GET FORECAST
        Button getForecastButton = createButton("Get Forecast", event -> {
            if (locationData == null || solarPanel == null) {allMessage.setText("Please check and save your inputs first");
                return;}
            Direction selectedDirection = solarPanel.Direction();
            HTTPSolarRequest weatherRequest = new HTTPSolarRequest(
                    // Constructor expects latitude first
                    locationData.Latitude(), locationData.Longitude(),  solarPanel.Tilt(), 1, selectedDirection);
            Thread apiThread =new Thread(() -> {SolarRadiationRetreiver retriever =
                    new SolarRadiationRetreiver(weatherRequest, new WeatherParser(), appData);
                boolean success = retriever.GetWeatherInfo();
                if (!success) {Platform.runLater(() -> allMessage.setText("Failed to receive API data"));return;}
                TimestampData timestampData = (TimestampData) appData.Get("TimestampData");
                if (timestampData == null) {return;}
                ArrayList<EstimationTimestamp> timestamps = new ArrayList<>();
                for (HashMap.Entry<String, Double> entry : timestampData.GetTimestamps().entrySet()) {
                    timestamps.add(new EstimationTimestamp(LocalDateTime.parse(entry.getKey()),
                            entry.getValue()));}
                timestamps.sort(null);
                Platform.runLater(() -> {StartMenu.setForecastData(timestamps);
                    allMessage.setText("Forecast loaded! Back to main page.");   });
            });
            apiThread.setDaemon(true);apiThread.start();     });
        // ROWS
        HBox backRow = new Row(backButton).getRow();
        backRow.setPadding(new Insets(0, 0, 0, 20));
        backRow.setMaxWidth(Double.MAX_VALUE);
        HBox zoneRow      = createComboRow("Zone", zoneList);
        HBox longitudeRow = createInputRow("Longitude", longitudeField);
        HBox latitudeRow  = createInputRow("Latitude", latitudeField);
        HBox areaRow      = createInputRow("Area", areaField);
        HBox tiltRow      = createInputRow("Tilt", tiltField);
        HBox efficiencyRow = createInputRow("Efficiency", efficiencyField);
        HBox directionRow  = createComboRow("Direction", directionList);
        HBox buttonRow     = new Row(checkAllButton, getForecastButton).getRow();
        // LAYOUT
        VBoxBuilder layoutBuilder = new VBoxBuilder();
        layoutBuilder.setSpacing(15);
        layoutBuilder.setAlignment(Pos.CENTER);
        layoutBuilder.addChildren(title, backRow, zoneRow,
                createLabel("Location"), longitudeRow, latitudeRow,
                createLabel("Solar panel details"), areaRow, tiltRow, efficiencyRow, directionRow, buttonRow, allMessage);
        VBox layout = layoutBuilder.getVBox();
        // SCROLL
        ScrollPaneBuilder scrollBuilder = new ScrollPaneBuilder();
        scrollBuilder.setContent(layout);
        scrollBuilder.setFitToWidth(true);
        ScrollPane scrollPane = scrollBuilder.getScrollPane();
        Scene scene = new Scene(scrollPane, 520, 650);
        stage.setTitle("Solar App");stage.setScene(scene);stage.show();
    }
    // HELPER METHODS
    private Label createLabel(String text) {
        LabelBuilder builder = new LabelBuilder();
        director.constructNameOfLabel(builder, text);
        return builder.getLabel();
    }
    private TextField createTextField(String prompt, String value) {
        TextInputBuilder builder = new TextInputBuilder();
        builder.setPromptText(prompt);
        TextField field = builder.getTextField();
        if (value != null) {field.setText(value);}
        return field;
    }
    private Button createButton(String text, EventHandler<ActionEvent> event) {
        ButtonBuilder builder = new ButtonBuilder();
        builder.setLabelName(text);
        builder.setEvent(event);
        return builder.getButton();
    }
    private ComboBox<String> createComboBox(String prompt, double width, String value, String... items) {
        ComboBoxBuilder builder = new ComboBoxBuilder();
        builder.setItems(items);
        builder.setPromptText(prompt);
        builder.setWidth(width);
        if (value != null) {builder.setValue(value);}
        return builder.getComboBox();
    }
    private HBox createInputRow(String text, TextField field) {
        LabelBuilder labelBuilder = new LabelBuilder();
        director.constructNameOfLabelWidth(labelBuilder, text);
        return new Row(labelBuilder.getLabel(), field).getRow();
    }
    private HBox createComboRow(String text, ComboBox<String> comboBox) {
        LabelBuilder labelBuilder = new LabelBuilder();
        director.constructNameOfLabelWidth(labelBuilder, text);
        return new Row(labelBuilder.getLabel(), comboBox).getRow();
    }
    private Double readDouble(TextField field, Label message, String error) {
        try {return Double.parseDouble(field.getText());
        } catch (NumberFormatException e) {message.setText(error);
            return null;}
    }
    private Integer readInt(TextField field, Label message, String error) {
        try {return Integer.parseInt(field.getText());
        } catch (NumberFormatException e) {message.setText(error);
            return null;}
    }
    private boolean check(boolean condition, Label message, String error) {
        if (!condition) {message.setText(error);return false;}
        return true;}
    private String directionText(Direction direction) {
        String text = direction.toString().toLowerCase();
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);}
    // LINKS
    @Override
    public void linkAppData(Model model) {this.appData = model;}
    @Override
    public void linkRoot(AppInterface root) {this.root = root;}
}