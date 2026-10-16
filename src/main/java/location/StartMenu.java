package location;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import other.AppInterface;
import other.ApplianceData;
import other.Model;
import other.SubAppInterface;
import other.TimestampData;
import powermarketdata.PowerMarketParser;
import powermarketdata.PowerMarketRetriver;
import weatherdata.EstimationTimestamp;
import location.builder.LabelBuilder;
import location.builder.LabelDirector;
import location.builder.Row;
import i18n.LanguageManager;
import ui.PageNavigator;
import location.builder.VBoxBuilder;
public class StartMenu extends Application implements SubAppInterface{
    private static ArrayList<EstimationTimestamp> forecast;

    public static void setForecastData(ArrayList<EstimationTimestamp> f){
        forecast = f;
    }

    private Model appData;
    public void linkAppData(Model m){
        this.appData=m;
    }
    private AppInterface root;
    public void linkRoot(AppInterface root){
        this.root=root;
    }

    @Override
    public void start(Stage stage) {
        restoreSavedForecast();
        LabelDirector labelDirector = new LabelDirector();

        TimestampData prodData = (TimestampData) appData.Get("TimestampData");

        double prodVal = 0;

        if (prodData != null) {
            LocalDateTime now = LocalDateTime.now();
            String currentHour = LocalDateTime.of(
                    now.getYear(),
                    now.getMonth(),
                    now.getDayOfMonth(),
                    now.getHour(),
                    0).toString();

            Double value = prodData.GetTimestamps().get(currentHour);
            if (value != null) {prodVal = value;}
        }
        LabelBuilder productionBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(productionBuilder, LanguageManager.get("production"));
        Label production = productionBuilder.getLabel();
        production.getStyleClass().add("text");


        LabelBuilder productionValueBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(productionValueBuilder, (int) prodVal + " W"
        );
        Label productionValue = productionValueBuilder.getLabel();
        productionValue.getStyleClass().add("text");


        LabelBuilder usageBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(usageBuilder, LanguageManager.get("usage")
        );
        Label usage = usageBuilder.getLabel();
        usage.getStyleClass().add("text");


        LabelBuilder usageValueBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(usageValueBuilder, formatCurrentUsage());
        Label usageValue = usageValueBuilder.getLabel();
        usageValue.getStyleClass().add("text");


        LabelBuilder priceBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(priceBuilder, LanguageManager.get("price") );
        Label price = priceBuilder.getLabel();
        price.getStyleClass().add("text");

        LabelBuilder priceValueBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(priceValueBuilder, " SEK/kWh");
        Label priceValue = priceValueBuilder.getLabel();
        priceValue.getStyleClass().add("text");


        // Fetch electricity price without freezing the UI.
        priceValue.setText("— SEK/kWh");
        Runnable refreshPrice = () -> {Thread worker = new Thread(() -> {
            try {PowerMarketRetriver provider = new PowerMarketRetriver();
                ArrayList<Double> prices = new ArrayList<>(PowerMarketParser.ParseToJson(provider.GetPowerMarketInfo()));
                int index = LocalTime.now().getHour() * 4;
                if (index >= 0 && index < prices.size()) {double value = prices.get(index);
                    javafx.application.Platform.runLater(() -> priceValue.setText(value + " SEK/kWh"));}
            } catch (Exception exception) {System.err.println("Could not update electricity price: " + exception.getMessage());
            }
        });
            worker.setDaemon(true);
            worker.start();
        };
        refreshPrice.run();
        Timeline priceClock = new Timeline(new KeyFrame(Duration.minutes(15), e -> refreshPrice.run()));
        priceClock.setCycleCount(Timeline.INDEFINITE);
        priceClock.play();

        productionValue.getStyleClass().add("boxText");
        usageValue.getStyleClass().add("boxText");
        priceValue.getStyleClass().add("boxText");
        priceValue.setStyle("-fx-font-size: 18px;");
        priceValue.setWrapText(true);


        // Create three information boxes

        VBoxBuilder productionBoxBuilder = new VBoxBuilder();
        productionBoxBuilder.setSpacing(8);
        productionBoxBuilder.addChildren(production, productionValue);

        VBox productionBox = productionBoxBuilder.getVBox();


        VBoxBuilder usageBoxBuilder = new VBoxBuilder();
        usageBoxBuilder.setSpacing(8);
        usageBoxBuilder.addChildren(usage, usageValue
        );

        VBox usageBox = usageBoxBuilder.getVBox();


        VBoxBuilder priceBoxBuilder = new VBoxBuilder();
        priceBoxBuilder.setSpacing(8);
        priceBoxBuilder.addChildren(price, priceValue
        );

        VBox priceBox = priceBoxBuilder.getVBox();

        // Style the boxes
        for (VBox box : new VBox[]{productionBox, usageBox, priceBox}) {
            box.setPrefWidth(220);
            box.setMinHeight(95);
            box.setPadding(new Insets(20));
            box.setAlignment(Pos.CENTER_LEFT);
            box.getStyleClass().add("infoBox");
        }

        // Put the boxes next to each other
        Row informationRowBuilder = new Row(15, productionBox, usageBox, priceBox);
        HBox information = informationRowBuilder.getRow();
        information.setPadding(new Insets(20));

        // Modern interactive chart
        VBox chartBox = SolarChartView.create(forecast);

        // MODERN HOME CONTENT
        Label welcome = new Label(LanguageManager.get("home.welcome"));

        welcome.getStyleClass().add("home-welcome");

        Label subtitle = new Label(LanguageManager.get("home.subtitle"));

        subtitle.getStyleClass().add("home-subtitle");
        subtitle.setWrapText(true);
        VBox welcomeBox = new VBox(6, welcome, subtitle);

        // Make information cards flexible
        for (VBox box : new VBox[]{productionBox, usageBox, priceBox}) {
            HBox.setHgrow(box, Priority.ALWAYS);
            box.setMaxWidth(Double.MAX_VALUE);
        }

        information.setPadding(new Insets(0));
        information.setMaxWidth(Double.MAX_VALUE);

        // Main vertical content
        VBox layout = new VBox(20, welcomeBox, information, chartBox);
        layout.setPadding(new Insets(24));
        layout.setAlignment(Pos.TOP_LEFT);
        layout.getStyleClass().addAll("layout", "home-content"
        );

        // Allow scrolling on small windows
        ScrollPane homeScroll = new ScrollPane(layout);

        homeScroll.setFitToWidth(true);
        homeScroll.getStyleClass().add("home-scroll");

        // Reuse the existing window and fixed sidebar/header.
        PageNavigator.show(stage, root, homeScroll, "home", priceClock::stop);

// Smooth chart appearance
        FadeTransition chartFade = new FadeTransition(Duration.millis(350), chartBox);
        chartFade.setFromValue(0);
        chartFade.setToValue(1);
        chartFade.play();

    }

    public static void main(String[] args) {
        launch(args);
    }

    // Use the same saved appliance data as Appliances, never a fixed "500 W".
    private String formatCurrentUsage() {
        ApplianceData devices = (ApplianceData) appData.Get("ApplianceData");
        if (devices == null) return "0 W";double total = 0;
        for (Map.Entry<String, Double> entry : devices.GetAppliances().entrySet()) {
            if (devices.IsInUse(entry.getKey())) {total += entry.getValue();
            }
        }
        return String.format(java.util.Locale.US, "%.1f W", total);
    }
    private void restoreSavedForecast() {

        // If forecast is already in memory, keep it
        if (forecast != null && !forecast.isEmpty()) {return;}
        // Read saved forecast from Model
        TimestampData savedData = (TimestampData) appData.Get("TimestampData");

        if (savedData == null) {System.out.println("No saved forecast found");return;}
        // Convert saved data into chart data
        ArrayList<EstimationTimestamp> restored = new ArrayList<>();

        for (Map.Entry<String, Double> entry : savedData.GetTimestamps().entrySet()) {

            try {LocalDateTime time = LocalDateTime.parse(entry.getKey());Double watts = entry.getValue();

                if (watts != null && Double.isFinite(watts)) {restored.add(new EstimationTimestamp(time, watts));}
            } catch (Exception e) {System.err.println("Invalid saved timestamp: " + entry.getKey()
            );
            }
        }

        // Sort by time
        restored.sort(null);
        // Restore forecast in memory
        forecast = restored;
        System.out.println("Restored forecast points: " + forecast.size()
        );
    }

}