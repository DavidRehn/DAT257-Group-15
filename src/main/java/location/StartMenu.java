
package location;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import location.builder.LabelBuilder;
import location.builder.LabelDirector;
import location.builder.Row;
import location.builder.VBoxBuilder;
import other.AppInterface;
import other.ApplianceData;
import other.Model;
import other.SubAppInterface;
import other.TimestampData;

import powermarketdata.PowerMarketParser;
import powermarketdata.PowerMarketRetriver;
import ui.LanguageManager;
import ui.PageNavigator;
import weatherdata.EstimationTimestamp;

import location.builder.LabelBuilder;
import location.builder.LabelDirector;
import location.builder.Row;
import location.builder.VBoxBuilder;

import i18n.LanguageManager;
import ui.PageNavigator;

public class StartMenu extends Application implements SubAppInterface {

    private static ArrayList<EstimationTimestamp> forecast;

    private Model appData;
    private AppInterface root;

    public static void setForecastData(ArrayList<EstimationTimestamp> f) {
        forecast = f;
    }

    @Override
    public void linkAppData(Model m) {
        this.appData = m;
    }

    @Override
    public void linkRoot(AppInterface root) {
        this.root = root;
    }

    @Override
    public void start(Stage stage) {

        restoreSavedForecast();

        LabelDirector labelDirector = new LabelDirector();

        // Get current solar production
        TimestampData prodData =
                (TimestampData) appData.Get("TimestampData");

        double prodVal = 0;

        if (prodData != null) {

            LocalDateTime now = LocalDateTime.now();

            String currentHour = LocalDateTime.of(
                    now.getYear(),
                    now.getMonth(),
                    now.getDayOfMonth(),
                    now.getHour(),
                    0
            ).toString();

            Double value = prodData.GetTimestamps().get(currentHour);

            if (value != null) {
                prodVal = value;
            }
        }

        // SOLAR PRODUCTION LABEL
        LabelBuilder productionBuilder = new LabelBuilder();

        labelDirector.constructName(
                productionBuilder,
                new String[]{LanguageManager.get("production")}
        );

        Label production = productionBuilder.getLabel();
        production.getStyleClass().add("text");

        // SOLAR PRODUCTION VALUE
        LabelBuilder productionValueBuilder = new LabelBuilder();

        labelDirector.constructName(
                productionValueBuilder,
                new String[]{(int) prodVal + " W"}
        );

        Label productionValue = productionValueBuilder.getLabel();
        productionValue.getStyleClass().add("text");

        // ELECTRICITY USAGE LABEL
        LabelBuilder usageBuilder = new LabelBuilder();

        labelDirector.constructName(
                usageBuilder,
                new String[]{LanguageManager.get("usage")}
        );

        Label usage = usageBuilder.getLabel();
        usage.getStyleClass().add("text");

        // ELECTRICITY USAGE VALUE
        LabelBuilder usageValueBuilder = new LabelBuilder();

        labelDirector.constructName(
                usageValueBuilder,
                new String[]{formatCurrentUsage()}
        );

        Label usageValue = usageValueBuilder.getLabel();
        usageValue.getStyleClass().add("text");

        // ELECTRICITY PRICE LABEL
        LabelBuilder priceBuilder = new LabelBuilder();

        labelDirector.constructName(
                priceBuilder,
                new String[]{LanguageManager.get("price")}
        );

        Label price = priceBuilder.getLabel();
        price.getStyleClass().add("text");

        // ELECTRICITY PRICE VALUE
        LabelBuilder priceValueBuilder = new LabelBuilder();

        labelDirector.constructName(
                priceValueBuilder,
                new String[]{" SEK/kWh"}
        );

        Label priceValue = priceValueBuilder.getLabel();
        priceValue.getStyleClass().add("text");

        // Load electricity price in background
        priceValue.setText("— SEK/kWh");

        Runnable refreshPrice = () -> {

            Thread worker = new Thread(() -> {

                try {
                    PowerMarketRetriver provider =
                            new PowerMarketRetriver();

                    ArrayList<Double> prices = new ArrayList<>(
                            PowerMarketParser.ParseToJson(
                                    provider.GetPowerMarketInfo()
                            )
                    );

                    int index = LocalTime.now().getHour() * 4;

                    if (index >= 0 && index < prices.size()) {

                        double value = prices.get(index);

                        Platform.runLater(() ->
                                priceValue.setText(value + " SEK/kWh")
                        );
                    }

                } catch (Exception exception) {

                    System.err.println(
                            "Could not update electricity price: "
                                    + exception.getMessage()
                    );
                }
            });

            worker.setDaemon(true);
            worker.start();
        };

        refreshPrice.run();

        Timeline priceClock = new Timeline(
                new KeyFrame(
                        Duration.minutes(15),
                        e -> refreshPrice.run()
                )
        );

        priceClock.setCycleCount(Timeline.INDEFINITE);
        priceClock.play();

        // VALUE STYLING
        productionValue.getStyleClass().add("boxText");
        usageValue.getStyleClass().add("boxText");
        priceValue.getStyleClass().add("boxText");

        priceValue.setStyle("-fx-font-size: 18px;");
        priceValue.setWrapText(true);

        // PRODUCTION BOX
        VBoxBuilder productionBoxBuilder = new VBoxBuilder();

        productionBoxBuilder.setSpacing(8);
        productionBoxBuilder.addChildren(
                production,
                productionValue
        );

        VBox productionBox = productionBoxBuilder.getVBox();

        // USAGE BOX
        VBoxBuilder usageBoxBuilder = new VBoxBuilder();

        usageBoxBuilder.setSpacing(8);
        usageBoxBuilder.addChildren(
                usage,
                usageValue
        );

        VBox usageBox = usageBoxBuilder.getVBox();

        // PRICE BOX
        VBoxBuilder priceBoxBuilder = new VBoxBuilder();

        priceBoxBuilder.setSpacing(8);
        priceBoxBuilder.addChildren(
                price,
                priceValue
        );

        VBox priceBox = priceBoxBuilder.getVBox();

        // Style information boxes
        for (VBox box : new VBox[]{
                productionBox,
                usageBox,
                priceBox
        }) {

            box.setPrefWidth(220);
            box.setMinHeight(95);
            box.setPadding(new Insets(20));
            box.setAlignment(Pos.CENTER_LEFT);
            box.getStyleClass().add("infoBox");
        }

        // Information row
        Row informationRowBuilder = new Row(
                15,
                productionBox,
                usageBox,
                priceBox
        );

        HBox information = informationRowBuilder.getRow();
        information.setPadding(new Insets(20));

        // Solar chart
        VBox chartBox = SolarChartView.create(forecast);

        // Welcome text
        Label welcome = new Label(
                LanguageManager.get("home.welcome")
        );

        welcome.getStyleClass().add("home-welcome");

        // Subtitle
        Label subtitle = new Label(
                LanguageManager.get("home.subtitle")
        );

        subtitle.getStyleClass().add("home-subtitle");
        subtitle.setWrapText(true);

        VBox welcomeBox = new VBox(
                6,
                welcome,
                subtitle
        );

        // Flexible information cards
        for (VBox box : new VBox[]{
                productionBox,
                usageBox,
                priceBox
        }) {

            HBox.setHgrow(box, Priority.ALWAYS);
            box.setMaxWidth(Double.MAX_VALUE);
        }

        information.setPadding(new Insets(0));
        information.setMaxWidth(Double.MAX_VALUE);

        // Main content
        VBox layout = new VBox(
                20,
                welcomeBox,
                information,
                chartBox
        );

        layout.setPadding(new Insets(24));
        layout.setAlignment(Pos.TOP_LEFT);

        layout.getStyleClass().addAll(
                "layout",
                "home-content"
        );

        // Scroll pane
        ScrollPane homeScroll = new ScrollPane(layout);

        homeScroll.setFitToWidth(true);
        homeScroll.getStyleClass().add("home-scroll");

        // Display page using PageNavigator
        PageNavigator.show(
                stage,
                root,
                homeScroll,
                "home",
                priceClock::stop
        );

        // Chart animation
        FadeTransition chartFade = new FadeTransition(
                Duration.millis(350),
                chartBox
        );

        chartFade.setFromValue(0);
        chartFade.setToValue(1);
        chartFade.play();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Calculate electricity usage from saved appliances
    private String formatCurrentUsage() {

        ApplianceData devices =
                (ApplianceData) appData.Get("ApplianceData");

        if (devices == null) {
            return "0 W";
        }

        double total = 0;

        for (Map.Entry<String, Double> entry :
                devices.GetAppliances().entrySet()) {

            if (devices.IsInUse(entry.getKey())) {
                total += entry.getValue();
            }
        }

        return String.format(
                java.util.Locale.US,
                "%.1f W",
                total
        );
    }

    // Restore saved solar forecast
    private void restoreSavedForecast() {

        if (forecast != null && !forecast.isEmpty()) {
            return;
        }

        TimestampData savedData =
                (TimestampData) appData.Get("TimestampData");

        if (savedData == null) {

            System.out.println("No saved forecast found");
            return;
        }

        ArrayList<EstimationTimestamp> restored =
                new ArrayList<>();

        for (Map.Entry<String, Double> entry :
                savedData.GetTimestamps().entrySet()) {

            try {

                LocalDateTime time =
                        LocalDateTime.parse(entry.getKey());

                Double watts = entry.getValue();

                if (watts != null && Double.isFinite(watts)) {

                    restored.add(
                            new EstimationTimestamp(time, watts)
                    );
                }

            } catch (Exception e) {

                System.err.println(
                        "Invalid saved timestamp: "
                                + entry.getKey()
                );
            }
        }

        restored.sort(null);

        forecast = restored;

        System.out.println(
                "Restored forecast points: " + forecast.size()
        );
    }
}
