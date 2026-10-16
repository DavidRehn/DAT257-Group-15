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
import location.builder.TextInputBuilder;
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
import i18n.LanguageManager;
import javafx.util.StringConverter;
import ui.PageNavigator;
public class LocationApp extends Application implements SubAppInterface {
    private Model appData;
    private AppInterface root;
    private SolarPanel solarPanel;
    private LocationData locationData;
    private ZoneData zoneData;
    private LabelDirector director;
    @Override
    public void start(Stage stage) {
        loadSavedData();
        director = new LabelDirector();
        Label title = createLabel(LanguageManager.get("location.title"));                                                     //  TITLE
        String[] prompts = {
                LanguageManager.get("input.longitude"),
                LanguageManager.get("input.latitude"),
                LanguageManager.get("input.area"),
                LanguageManager.get("input.tilt"),
                LanguageManager.get("input.efficiency")};  //  TEXT FIELDS
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
        ComboBox<String> zoneList = createComboBox(LanguageManager.get("input.zone"), 185, zoneData == null ? null : zoneData.Zone(), zones);
        //  DIRECTION
        String[] directions = {"South", "Southwest", "West", "Northwest", "North", "Northeast", "East", "Southeast"};
        ComboBox<String> directionList = createComboBox(LanguageManager.get("input.direction"), 185,
                solarPanel == null ? null : directionText(solarPanel.Direction()), directions);

        directionList.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String direction) {if (direction == null) {return "";}
                return LanguageManager.get("direction." + direction.toLowerCase(java.util.Locale.ROOT));}
            @Override public String fromString(String text) {return text;}});

        //  MESSAGE
        Label allMessage = createLabel("");
        allMessage.getStyleClass().add("text");
        //  CHECK ALL
        Button checkAllButton = createButton(LanguageManager.get("check.all"), e -> validateAndSave(
                longitudeField, latitudeField, areaField, tiltField, efficiencyField, zoneList, directionList, allMessage));
        //  FORECAST
        Button getForecastButton = createButton(LanguageManager.get("get.forecast"), e -> loadForecast(allMessage));
        checkAllButton.setPrefWidth(160);
        getForecastButton.setPrefWidth(160);
        //  ROWS
        String[] rowNames = {LanguageManager.get("longitude"), LanguageManager.get("latitude"),
                LanguageManager.get("area"), LanguageManager.get("tilt"), LanguageManager.get("efficiency")};

        HBox[] rows = createInputRows(rowNames, fields);
        HBox zoneRow = createComboRow(LanguageManager.get("zone"), zoneList);
        HBox directionRow = createComboRow(LanguageManager.get("direction"), directionList);
        for (HBox row : rows) {
            row.setAlignment(Pos.CENTER_LEFT);
            ((Label) row.getChildren().get(0)).setPrefWidth(160);
            ((TextField) row.getChildren().get(1)).setPrefWidth(270);
            row.getStyleClass().add("app-form-row");
        }
        zoneRow.setAlignment(Pos.CENTER_LEFT);
        directionRow.setAlignment(Pos.CENTER_LEFT);
        ((Label) zoneRow.getChildren().get(0)).setPrefWidth(160);
        ((Label) directionRow.getChildren().get(0)).setPrefWidth(160);
        zoneList.setPrefWidth(270);
        directionList.setPrefWidth(270);
        HBox buttonRow = new Row(checkAllButton, getForecastButton).getRow();
        buttonRow.setAlignment(Pos.CENTER_LEFT);
        checkAllButton.getStyleClass().add("app-secondary-button");
        getForecastButton.getStyleClass().add("app-primary-button");
        // Fix button widths
        checkAllButton.setPrefWidth(170);  checkAllButton.setMinWidth(170);
        getForecastButton.setPrefWidth(170); getForecastButton.setMinWidth(170);
        // Settings within the unified dashboard: two modern cards.
        Label locationHeading = createLabel(LanguageManager.get("location"));
        locationHeading.getStyleClass().addAll("solar-chart-title", "app-section-heading");
        VBox locationCard = new VBox(14, locationHeading, zoneRow, rows[0], rows[1]);
        locationCard.setPadding(new Insets(24));
        locationCard.getStyleClass().add("app-card");
        locationCard.setMaxWidth(Double.MAX_VALUE);

        Label panelsHeading = createLabel(LanguageManager.get("panel.details"));
        panelsHeading.getStyleClass().addAll("solar-chart-title", "app-section-heading");
        VBox solarCard = new VBox(14, panelsHeading, rows[2], rows[3], rows[4], directionRow);
        solarCard.setPadding(new Insets(24));
        solarCard.getStyleClass().add("app-card");
        solarCard.setMaxWidth(Double.MAX_VALUE);

        title.getStyleClass().addAll("home-welcome", "app-page-title");
        allMessage.getStyleClass().add("app-message");
        VBox layout = new VBox(22, title, locationCard, solarCard, buttonRow, allMessage);
        layout.setPadding(new Insets(25, 32, 35, 32));
        layout.setAlignment(Pos.TOP_LEFT);
        layout.getStyleClass().addAll("layout", "home-content", "app-settings-page");

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("home-scroll");
        PageNavigator.show(stage, root, scrollPane, "settings", null);
    }

    // VALIDATION + SAVE

    private void validateAndSave(
            TextField longitudeField, TextField latitudeField, TextField areaField, TextField tiltField,
            TextField efficiencyField, ComboBox<String> zoneList, ComboBox<String> directionList, Label message)
    {

        // 1. Check empty fields
        if (hasBlank(longitudeField, latitudeField, areaField, tiltField, efficiencyField)  || zoneList.getValue() == null || directionList.getValue() == null) {message.setText(LanguageManager.get("validation.empty"));return;
        }
        // 2. Validate longitude
        Double longitude = readDouble(longitudeField, message, LanguageManager.get("validation.longitude"));
        if (longitude == null) {return;}

        if (!check(longitude >= -180 && longitude <= 180 && Double.isFinite(longitude),
                message, LanguageManager.get("validation.longitude.range"))) {return;}

        // 3. Validate latitude
        Double latitude = readDouble(latitudeField, message, LanguageManager.get("validation.latitude"));
        if (latitude == null) {return;}
        if (!check(latitude >= -90 && latitude <= 90 && Double.isFinite(latitude),
                message, LanguageManager.get("validation.latitude.range"))) {return;}

        // 4. Validate panel area
        Double area = readDouble(areaField, message, LanguageManager.get("validation.area"));
        if (area == null) {return;}

        if (!check(area > 0 && Double.isFinite(area), message, LanguageManager.get("validation.area.range")))
        {return;
        }

        // 5. Validate panel tilt
        Integer tilt = readInt(tiltField, message, LanguageManager.get("validation.tilt"));
        if (tilt == null) {return;}
        if (!check(tilt >= 0 && tilt <= 90, message, LanguageManager.get("validation.tilt.range"))) {return;
        }
        // 6. Validate panel efficiency
        Double efficiency = readDouble(efficiencyField, message, LanguageManager.get("validation.efficiency"));
        if (efficiency == null) {return;
        }
        if (!check(efficiency > 0 && efficiency <= 1 && Double.isFinite(efficiency),
                message, LanguageManager.get("validation.efficiency.range"))) {return;
        }

        // 7. Create valid data
        locationData = new LocationData(longitude, latitude);
        zoneData = new ZoneData(zoneList.getValue());
        solarPanel = new SolarPanel(area, tilt, SolarPanel.directionConverter(directionList.getValue().toUpperCase(
                java.util.Locale.ROOT)), efficiency);

        // 8. Save data
        appData.Store("LocationData", locationData);
        appData.Store("ZoneData", zoneData);
        appData.Store("SolarPanel", solarPanel);
        appData.SaveToDisc();

        // 9. Show success message
        message.setText(LanguageManager.get("validation.success"));
    }

    // FORECAST
    private void loadForecast(Label message) {
        if (locationData == null || solarPanel == null) {message.setText(LanguageManager.get("forecast.save.first")
        );  return;}
        HTTPSolarRequest weatherRequest = new HTTPSolarRequest(
                // Latitude first
                locationData.Latitude(),  locationData.Longitude(), solarPanel.Tilt(), 2, solarPanel.Direction());
        Thread apiThread = new Thread(() -> {SolarRadiationRetreiver retriever = new SolarRadiationRetreiver(weatherRequest, new WeatherParser(), appData);
            if (!retriever.GetWeatherInfo()) {Platform.runLater(() -> message.setText(  LanguageManager.get("forecast.failed")));return;}
            TimestampData timestampData = (TimestampData) appData.Get("TimestampData");
            if (timestampData == null) {Platform.runLater(() -> message.setText( LanguageManager.get("forecast.empty")));return;}
            boolean saved = appData.SaveToDisc();
            if (saved) {System.out.println("Forecast saved successfully");}
            else {System.err.println("Could not save forecast");}

            ArrayList<EstimationTimestamp> timestamps = new ArrayList<>();
            for (HashMap.Entry<String, Double> entry : timestampData.GetTimestamps().entrySet()) {
                timestamps.add(new EstimationTimestamp(LocalDateTime.parse(entry.getKey()), entry.getValue())
                );}
            timestamps.sort(null);
            Platform.runLater(() -> {StartMenu.setForecastData(timestamps);
                message.setText( LanguageManager.get("forecast.success"));   });
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
        Label label = builder.getLabel();
        label.getStyleClass().add("text");
        return new Row(label, field).getRow();
    }
    private HBox createComboRow(String name, ComboBox<String> comboBox) {
        LabelBuilder builder = new LabelBuilder();
        director.constructNameOfLabelWidth(builder, name);
        Label label = builder.getLabel();
        label.getStyleClass().add("text");
        return new Row(label, comboBox).getRow();
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