
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
import javafx.util.StringConverter;

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
        // Load previously saved settings
        loadSavedData();
        director = new LabelDirector();
        // PAGE TITLE
        Label title = createLabel(LanguageManager.get("location.title"));
        title.getStyleClass().addAll("home-welcome", "app-page-title");

        // INPUT PLACEHOLDERS
        String[] prompts = {
                LanguageManager.get("input.longitude"), LanguageManager.get("input.latitude"),
                LanguageManager.get("input.area"), LanguageManager.get("input.tilt"), LanguageManager.get("input.efficiency")};
        // PREVIOUSLY SAVED VALUES
        String[] savedValues = {
                locationData == null ? null : String.valueOf(locationData.Longitude()),
                locationData == null ? null : String.valueOf(locationData.Latitude()),
                solarPanel == null ? null : String.valueOf(solarPanel.Area()),
                solarPanel == null ? null : String.valueOf(solarPanel.Tilt()),
                solarPanel == null ? null : String.valueOf(solarPanel.Efficiency())
        };

        // CREATE TEXT FIELDS
        TextField[] fields = createTextFields(prompts, savedValues);
        TextField longitudeField = fields[0];TextField latitudeField = fields[1];TextField areaField = fields[2];
        TextField tiltField = fields[3];TextField efficiencyField = fields[4];

        // FIELD LABELS
        String[] rowNames = {LanguageManager.get("longitude"), LanguageManager.get("latitude"),
                LanguageManager.get("area"), LanguageManager.get("tilt"), LanguageManager.get("efficiency")};

        // CREATE INPUT ROWS
        HBox[] rows = createInputRows(rowNames, fields);

        for (HBox row : rows) {row.setAlignment(Pos.CENTER_LEFT);
            Label rowLabel = (Label) row.getChildren().get(0);
            TextField rowField = (TextField) row.getChildren().get(1);
            rowLabel.setPrefWidth(160);
            rowField.setPrefWidth(270);
            row.getStyleClass().add("app-form-row");
        }

        // ELECTRICITY ZONES
        String[] zones = {"SE1", "SE2", "SE3", "SE4"};

        ComboBox<String> zoneList = createComboBox(LanguageManager.get("input.zone"),
         185, zoneData == null ? null : zoneData.Zone(), zones);

        // PANEL DIRECTIONS
        String[] directions = {"South", "Southwest", "West", "Northwest", "North", "Northeast", "East", "Southeast"};

        ComboBox<String> directionList = createComboBox(LanguageManager.get("input.direction"), 185, solarPanel == null ? null
            : directionText(solarPanel.Direction()), directions);

        // TRANSLATE DIRECTION NAMES
        directionList.setConverter(new StringConverter<String>() {
         @Override
            public String toString(String direction) {if (direction == null) {return "";}
            return LanguageManager.get("direction." + direction.toLowerCase(java.util.Locale.ROOT));}
         @Override
             public String fromString(String text) {return text;}
        });

        // MESSAGE LABEL
        Label allMessage = createLabel("");
        allMessage.getStyleClass().addAll("text", "app-message");

        // CHECK ALL BUTTON
        Button checkAllButton = createButton(LanguageManager.get("check.all"), e -> validateAndSave(
         longitudeField, latitudeField, areaField, tiltField, efficiencyField, zoneList, directionList, allMessage)
        );

        // GET FORECAST BUTTON
        Button getForecastButton = createButton(LanguageManager.get("get.forecast"), e -> loadForecast(allMessage));

        // ZONE ROW
        HBox zoneRow = createComboRow(LanguageManager.get("zone"), zoneList);
        zoneRow.setAlignment(Pos.CENTER_LEFT);
        ((Label) zoneRow.getChildren().get(0)).setPrefWidth(160);

        // DIRECTION ROW
        HBox directionRow = createComboRow(LanguageManager.get("direction"), directionList);
        directionRow.setAlignment(Pos.CENTER_LEFT);
        ((Label) directionRow.getChildren().get(0)).setPrefWidth(160);

        zoneList.setPrefWidth(270);
        directionList.setPrefWidth(270);

        // BUTTON ROW
        HBox buttonRow = new Row(checkAllButton, getForecastButton).getRow();
        buttonRow.setAlignment(Pos.CENTER_LEFT);
        // BUTTON STYLING
        checkAllButton.getStyleClass().add("app-secondary-button");

        checkAllButton.setPrefWidth(170);
        checkAllButton.setMinWidth(170);
        getForecastButton.getStyleClass().add("app-primary-button");
        getForecastButton.setPrefWidth(170);
        getForecastButton.setMinWidth(170);
        // LOCATION CARD
        Label locationHeading = createLabel(LanguageManager.get("location"));
        locationHeading.getStyleClass().addAll("solar-chart-title", "app-section-heading");

        VBox locationCard = new VBox(14, locationHeading, zoneRow, rows[0], rows[1]);

        locationCard.setPadding(new Insets(24));
        locationCard.getStyleClass().add("app-card");
        locationCard.setMaxWidth(Double.MAX_VALUE);

        // SOLAR PANEL CARD
        Label panelsHeading = createLabel(LanguageManager.get("panel.details"));
        panelsHeading.getStyleClass().addAll("solar-chart-title", "app-section-heading");

        VBox solarCard = new VBox(14, panelsHeading, rows[2], rows[3], rows[4], directionRow);

        solarCard.setPadding(new Insets(24));
        solarCard.getStyleClass().add("app-card");
        solarCard.setMaxWidth(Double.MAX_VALUE);

        // MAIN PAGE LAYOUT
        VBox layout = new VBox(22, title, locationCard, solarCard, buttonRow, allMessage);

        layout.setPadding(new Insets(25, 32, 35, 32));
        layout.setAlignment(Pos.TOP_LEFT);
        layout.getStyleClass().addAll("layout", "home-content", "app-settings-page");

        // SCROLLING
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("home-scroll");

        // DISPLAY SETTINGS PAGE
        PageNavigator.show(stage, root, scrollPane, "settings", null);
    }
    // VALIDATION AND SAVE
    private void validateAndSave(TextField longitudeField, TextField latitudeField, TextField areaField, TextField tiltField,
            TextField efficiencyField, ComboBox<String> zoneList, ComboBox<String> directionList, Label message) {
        // CHECK EMPTY FIELDS
        if (hasBlank(longitudeField, latitudeField, areaField, tiltField, efficiencyField)
                || zoneList.getValue() == null || directionList.getValue() == null) {
            message.setText(LanguageManager.get("validation.empty"));
            return;
        }

        // VALIDATE LONGITUDE
        Double longitude = readDouble(longitudeField, message, LanguageManager.get("validation.longitude"));
        if (longitude == null) {return;}
        if (!check(longitude >= -180 && longitude <= 180 && Double.isFinite(longitude), message,
           LanguageManager.get("validation.longitude.range"))) {
            return;}

        // VALIDATE LATITUDE
        Double latitude = readDouble(latitudeField, message, LanguageManager.get("validation.latitude")
        );
        if (latitude == null) {return;}
        if (!check(latitude >= -90 && latitude <= 90 && Double.isFinite(latitude), message, LanguageManager.get("validation.latitude.range")
        )) {return;
        }

        // VALIDATE PANEL AREA
        Double area = readDouble(areaField, message, LanguageManager.get("validation.area"));

        if (area == null) {return;}

        if (!check(area > 0 && Double.isFinite(area), message, LanguageManager.get("validation.area.range"))) {
            return;}
        // VALIDATE PANEL TILT
        Integer tilt = readInt(tiltField, message, LanguageManager.get("validation.tilt"));
        if (tilt == null) {return;}
        if (!check(tilt >= 0 && tilt <= 90, message, LanguageManager.get("validation.tilt.range"))) {
            return;}

        // VALIDATE PANEL EFFICIENCY
        Double efficiency = readDouble(efficiencyField, message, LanguageManager.get("validation.efficiency"));

        if (efficiency == null) {return;}
        if (!check(efficiency > 0 && efficiency <= 1 && Double.isFinite(efficiency), message, LanguageManager.get("validation.efficiency.range")
        )) {return;}

        // CREATE DATA OBJECTS
        locationData = new LocationData(longitude, latitude);
        zoneData = new ZoneData(zoneList.getValue());
        solarPanel = new SolarPanel(area, tilt, SolarPanel.directionConverter(directionList.getValue().toUpperCase(
                java.util.Locale.ROOT)), efficiency);

        // STORE DATA
        appData.Store("LocationData", locationData);
        appData.Store("ZoneData", zoneData);
        appData.Store("SolarPanel", solarPanel);
        appData.SaveToDisc();
        // SUCCESS MESSAGE
        message.setText(LanguageManager.get("validation.success"));}
    // FORECAST

    private void loadForecast(Label message) {
        if (locationData == null || solarPanel == null) {message.setText(LanguageManager.get("forecast.save.first"));
            return;}

        HTTPSolarRequest weatherRequest = new HTTPSolarRequest(locationData.Latitude(), locationData.Longitude(),
        solarPanel.Tilt(), 2, solarPanel.Direction());
        Thread apiThread = new Thread(() -> {SolarRadiationRetreiver retriever = new SolarRadiationRetreiver(weatherRequest,
          new WeatherParser(), appData);
            if (!retriever.GetWeatherInfo()) {
                Platform.runLater(() -> message.setText(LanguageManager.get("forecast.failed")));
                return;}

            TimestampData timestampData = (TimestampData) appData.Get("TimestampData");
            if (timestampData == null) {Platform.runLater(() -> message.setText(LanguageManager.get("forecast.empty"))
            );
                return;}

            boolean saved = appData.SaveToDisc();
            if (saved) {System.out.println("Forecast saved successfully");
            } else {System.err.println("Could not save forecast");}
            ArrayList<EstimationTimestamp> timestamps = new ArrayList<>();
            for (HashMap.Entry<String, Double> entry : timestampData.GetTimestamps().entrySet()) {
                timestamps.add(new EstimationTimestamp(LocalDateTime.parse(entry.getKey()), entry.getValue()));
            }
            timestamps.sort(null);
            Platform.runLater(() -> {StartMenu.setForecastData(timestamps);message.setText(LanguageManager.get("forecast.success"
               ));
            });
        });

        apiThread.setDaemon(true);
        apiThread.start();
    }
    // CREATE TEXT FIELDS

    private TextField[] createTextFields(String[] prompts, String[] values) {
        TextField[] fields = new TextField[prompts.length];
        for (int i = 0; i < prompts.length; i++) {
            fields[i] = createTextField(prompts[i], values[i]);}
        return fields;
    }
    // CREATE INPUT ROWS
    private HBox[] createInputRows(String[] names, TextField[] fields) {
        HBox[] rows = new HBox[names.length];
        for (int i = 0; i < names.length; i++) {
            rows[i] = createInputRow(names[i], fields[i]);
        }
        return rows;
    }
    // CHECK EMPTY FIELDS
    private boolean hasBlank(TextField... fields) {for (TextField field : fields) {
        if (field.getText().isBlank()) {return true;}
        }
        return false;
    }
    // LABEL BUILDER
    private Label createLabel(String text) {LabelBuilder builder = new LabelBuilder();
        director.constructName(builder, new String[]{text});
        return builder.getLabel();}

    // TEXT INPUT BUILDER
    private TextField createTextField(String prompt, String value) {
        TextInputBuilder builder = new TextInputBuilder();
        // Create the TextField first
        director.constructName(builder, new String[]{""});
        // Set placeholder using temporary method
       builder.setPromptText(prompt);
       TextField field = builder.getTextField();
       // Restore saved input
        if (value != null) {field.setText(value);}
        return field;
    }
    // BUTTON BUILDER
    private Button createButton(String text, EventHandler<ActionEvent> event) {
        ButtonBuilder builder = new ButtonBuilder();
        // Create button using LabelDirector
        director.constructName(builder, new String[]{text});

        // BBuilder provides the event handler
        builder.setEvent(() -> event);
        // Temporary method added to ButtonBuilder
        return builder.getButton();
    }
    // COMBO BOX BUILDER
    private ComboBox<String> createComboBox(String prompt, double width, String value, String... items) {
        ComboBoxBuilder builder = new ComboBoxBuilder();
        builder.setItems(items);
        builder.setPromptText(prompt);
        builder.setWidth(width);

        if (value != null) {builder.setValue(value);}
        return builder.getComboBox();
    }
    // INPUT ROW
    private HBox createInputRow(String name, TextField field) {
        LabelBuilder builder = new LabelBuilder();
        director.constructW90(builder, new String[]{name});
        Label label = builder.getLabel();
        label.getStyleClass().add("text");
        return new Row(label, field).getRow();
    }

    // COMBO BOX ROW

    private HBox createComboRow(String name, ComboBox<String> comboBox) {
        LabelBuilder builder = new LabelBuilder();
        director.constructW90(builder, new String[]{name});
        Label label = builder.getLabel();
        label.getStyleClass().add("text");

        return new Row(label, comboBox).getRow();
    }

    // VALIDATION HELPERS


    private Double readDouble(TextField field, Label message, String error) {

        try {return Double.parseDouble(field.getText());}
        catch (NumberFormatException e) {message.setText(error);
            return null;}
    }

    private Integer readInt(TextField field, Label message, String error) {
        try {return Integer.parseInt(field.getText());}
        catch (NumberFormatException e) {message.setText(error);return null;}
    }

    private boolean check(boolean condition, Label message, String error) {
        if (!condition) {message.setText(error);return false;}
        return true;}
    // LOAD SAVED DATA
    private void loadSavedData() {solarPanel = (SolarPanel) appData.Get("SolarPanel");
        locationData = (LocationData) appData.Get("LocationData");
        zoneData = (ZoneData) appData.Get("ZoneData");}
    // DIRECTION TEXT
    private String directionText(Direction direction) {
        String text = direction.toString().toLowerCase(java.util.Locale.ROOT);
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
    // LINKS
    @Override
    public void linkAppData(Model model) {this.appData = model;}
    @Override public void linkRoot(AppInterface root) {this.root = root;}
}
