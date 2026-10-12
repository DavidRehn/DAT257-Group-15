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
public class LocationApp extends Application implements SubAppInterface {
    private Model appData;
    private AppInterface root;
    private SolarPanel solarPanel;
    private LocationData locationData;
    private ZoneData zoneData;
    private LabelDirector director;
    @Override
    public void start(Stage stage) {  loadSavedData(); director = new LabelDirector();
        Label title = createLabel("Location and Solar Panel Info");                                                     //  TITLE
        Button backButton = createButton("← Back to main page", e -> root.viewStart(stage));                 //  BACK BUTTON
        String[] prompts = {"Enter longitude", "Enter latitude", "Area (m²)", "Tilt (degrees)", "Efficiency (0.0 to 1.0)"};  //  TEXT FIELDS
        String[] savedValues = {locationData == null ? null : String.valueOf(locationData.Longitude()),
              locationData == null ? null : String.valueOf(locationData.Latitude()),
                solarPanel == null ? null : String.valueOf(solarPanel.Area()),
                solarPanel == null ? null : String.valueOf(solarPanel.Tilt()),
                solarPanel == null ? null : String.valueOf(solarPanel.Efficiency())
     };
        TextField[] fields = createTextFields(prompts, savedValues);
        // Give the fields clear names
        TextField longitudeField = fields[0]; TextField latitudeField = fields[1]; TextField areaField = fields[2];
        TextField tiltField = fields[3];TextField efficiencyField = fields[4];
        //  ZONE
      String[] zones = {"SE1", "SE2", "SE3", "SE4"};
      ComboBox<String> zoneList = createComboBox("Choose zone", 185, zoneData == null ? null : zoneData.Zone(), zones);
        //  DIRECTION
        String[] directions = {"South", "Southwest", "West", "Northwest", "North", "Northeast", "East", "Southeast"};
        ComboBox<String> directionList = createComboBox("Choose direction", 185,
         solarPanel == null ? null : directionText(solarPanel.Direction()), directions);
        //  MESSAGE
        Label allMessage = createLabel("");
        //  CHECK ALL
        Button checkAllButton = createButton("Check All", e -> validateAndSave(
         longitudeField, latitudeField, areaField, tiltField, efficiencyField, zoneList, directionList, allMessage));
        //  FORECAST
        Button getForecastButton = createButton("Get Forecast", e -> loadForecast(allMessage));
        //  ROWS
        String[] rowNames = {"Longitude", "Latitude", "Area", "Tilt", "Efficiency"};
        HBox[] rows = createInputRows(rowNames, fields);
        HBox backRow = new Row(backButton).getRow();
        backRow.setPadding(new Insets(0, 0, 0, 20));
        backRow.setMaxWidth(Double.MAX_VALUE);
        HBox zoneRow = createComboRow("Zone", zoneList);
        HBox directionRow = createComboRow("Direction", directionList);
        HBox buttonRow = new Row(checkAllButton, getForecastButton).getRow();
        //  LAYOUT
        VBoxBuilder layoutBuilder = new VBoxBuilder();
        layoutBuilder.setSpacing(15);
        layoutBuilder.setAlignment(Pos.CENTER);
        layoutBuilder.addChildren(title, backRow, zoneRow, createLabel("Location"), rows[0], rows[1],
        createLabel("Solar panel details"), rows[2], rows[3], rows[4], directionRow, buttonRow, allMessage);
        VBox layout = layoutBuilder.getVBox();
        //  SCROLL
        ScrollPaneBuilder scrollBuilder = new ScrollPaneBuilder();
        scrollBuilder.setContent(layout);
        scrollBuilder.setFitToWidth(true);
        ScrollPane scrollPane = scrollBuilder.getScrollPane();
        Scene scene = new Scene(scrollPane, 520, 650);
        stage.setTitle("Solar App");
        stage.setScene(scene);
        stage.show();
    }
    // VALIDATION + SAVE
    private void validateAndSave(TextField longitudeField, TextField latitudeField, TextField areaField,
        TextField tiltField, TextField efficiencyField, ComboBox<String> zoneList, ComboBox<String> directionList, Label message) {
        // Check empty fields
        if (hasBlank(longitudeField, latitudeField, areaField, tiltField, efficiencyField)
        || zoneList.getValue() == null || directionList.getValue() == null) {message.setText("Please fill in all fields"); return;}
        // Longitude
        Double longitude = readDouble(longitudeField, message, "Please enter a valid longitude");
        if (longitude == null) return;
        if (!check(longitude >= -180 && longitude <= 180 && Double.isFinite(longitude), message, "Longitude must be between -180 and 180")) return;
        // Latitude
        Double latitude = readDouble(latitudeField, message, "Please enter a valid latitude");
        if (latitude == null) return;
        if (!check(latitude >= -90 && latitude <= 90 && Double.isFinite(latitude), message, "Latitude must be between -90 and 90")) return;
        // Area
        Double area = readDouble(areaField, message, "Please enter a valid area");
        if (area == null) return;
        if (!check(area > 0 && Double.isFinite(area), message, "Area must be greater than 0")) return;
        // Tilt
        Integer tilt = readInt(tiltField, message, "Please enter a valid whole number for tilt");  if (tilt == null) return;
        if (!check(tilt >= 0 && tilt <= 90, message, "Tilt must be between 0 and 90")) return;
        // Efficiency
        Double efficiency = readDouble(efficiencyField, message, "Please enter a valid number for efficiency");
        if (efficiency == null) return;
        if (!check(efficiency > 0 && efficiency <= 1 && Double.isFinite(efficiency), message, "Efficiency must be between 0.0 and 1.0")) return;
        // Create data
        locationData = new LocationData(longitude, latitude);
        zoneData     = new ZoneData(zoneList.getValue());
        solarPanel = new SolarPanel(area, tilt, SolarPanel.directionConverter(directionList.getValue().toUpperCase()), efficiency);
        // Save
        appData.Store("LocationData", locationData);
        appData.Store("ZoneData", zoneData);
        appData.Store("SolarPanel", solarPanel);
        appData.SaveToDisc();message.setText("All inputs are valid");
    }
    // FORECAST
    private void loadForecast(Label message) {
        if (locationData == null || solarPanel == null) {message.setText("Please check and save your inputs first");  return;}
        HTTPSolarRequest weatherRequest = new HTTPSolarRequest(
        // Latitude first // Latitude first
          locationData.Latitude(),  locationData.Longitude(), solarPanel.Tilt(), 1, solarPanel.Direction());
        Thread apiThread = new Thread(() -> {SolarRadiationRetreiver retriever = new SolarRadiationRetreiver(weatherRequest, new WeatherParser(), appData);
        if (!retriever.GetWeatherInfo()) {Platform.runLater(() -> message.setText("Failed to receive API data"));return;}
             TimestampData timestampData = (TimestampData) appData.Get("TimestampData");
        if (timestampData == null) {Platform.runLater(() -> message.setText("No forecast data found"));return;}
              ArrayList<EstimationTimestamp> timestamps = new ArrayList<>();
             for (HashMap.Entry<String, Double> entry : timestampData.GetTimestamps().entrySet()) {
                 timestamps.add(new EstimationTimestamp(LocalDateTime.parse(entry.getKey()), entry.getValue())
                 );}
                    timestamps.sort(null);
                    Platform.runLater(() -> {StartMenu.setForecastData(timestamps);
                        message.setText("Forecast loaded! Back to main page.");   });
                });     apiThread.setDaemon(true);apiThread.start();
    }
    // ARRAY HELPERS
    private TextField[] createTextFields(String[] prompts, String[] values) {
        TextField[] fields = new TextField[prompts.length];
        for (int i = 0; i < prompts.length; i++) {fields[i] = createTextField(prompts[i], values[i]);  }return fields;
    }
    private HBox[] createInputRows(String[] names, TextField[] fields) {
        HBox[] rows = new HBox[names.length];
        for (int i = 0; i < names.length; i++) { rows[i] = createInputRow(names[i], fields[i]);  }return rows;
    }
    private boolean hasBlank(TextField... fields) { for (TextField field : fields) {
       if (field.getText().isBlank()) {return true;}}  return false;
    }
    // UI HELPERS
    private Label createLabel(String text) {
        LabelBuilder builder = new LabelBuilder();
        director.constructNameOfLabel(builder, text);
        return builder.getLabel();
    }
    private TextField createTextField(String prompt, String value) {
        TextInputBuilder builder = new TextInputBuilder();
        builder.setPromptText(prompt);
        TextField field = builder.getTextField();
        if (value != null) {field.setText(value);}  return field;
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
    private HBox createInputRow(String name, TextField field) {
        LabelBuilder builder = new LabelBuilder();
        director.constructNameOfLabelWidth(builder, name);
        return new Row(builder.getLabel(), field).getRow();
    }
    private HBox createComboRow(String name, ComboBox<String> comboBox) {
        LabelBuilder builder = new LabelBuilder();
        director.constructNameOfLabelWidth(builder, name);
        return new Row(builder.getLabel(), comboBox).getRow();
    }
    // VALIDATION HELPERS
    private Double readDouble(TextField field, Label message, String error) {
        try {return Double.parseDouble(field.getText());}
        catch (NumberFormatException e) {message.setText(error);return null;}
    }
    private Integer readInt(TextField field, Label message, String error) {
        try {return Integer.parseInt(field.getText());}
        catch (NumberFormatException e) {message.setText(error);return null;}
    }
    private boolean check(boolean condition, Label message, String error) {
        if (!condition) {message.setText(error); return false;} return true;
    }
    // SAVED DATA
    private void loadSavedData() {
        solarPanel = (SolarPanel) appData.Get("SolarPanel");
        locationData = (LocationData) appData.Get("LocationData");
        zoneData = (ZoneData) appData.Get("ZoneData");
    }
    private String directionText(Direction direction) {
        String text = direction.toString().toLowerCase();
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
    // LINKS
    @Override
    public void linkAppData(Model model) {this.appData = model;}
    @Override
    public void linkRoot(AppInterface root) {this.root = root;}
}