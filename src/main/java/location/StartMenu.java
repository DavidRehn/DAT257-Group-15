
package location;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Formatter;

import javax.json.JsonArray;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
//import javafx.scene.chart.CategoryAxis;
//import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import other.AppInterface;
import other.Model;
import other.SubAppInterface;
import other.TimestampData;
import powermarketdata.PowerMarketParser;
import powermarketdata.PowerMarketRetriver;
import weatherdata.EstimationTimestamp;

import location.builder.LabelBuilder;
import location.builder.LabelDirector;
import location.builder.ButtonBuilder;
import location.builder.VBoxBuilder;
import location.builder.Row;
import location.builder.BarChartBuilder;

import CSS.CSSPath;

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
        LabelDirector labelDirector = new LabelDirector();


        // Header
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("EEE HH:mm");

        LabelBuilder dateBuilder = new LabelBuilder();

        labelDirector.constructNameOfLabel(
                dateBuilder,
                LocalDateTime.now().format(formatter)
        );

        Label date = dateBuilder.getLabel();

// Update the clock automatically
        Timeline clock = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    date.setText(
                            LocalDateTime.now().format(formatter)
                    );
                })
        );

        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();

        LabelBuilder titleBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(
                titleBuilder,
                "Solar App"
        );

        Label title = titleBuilder.getLabel();
        ButtonBuilder appliancesButtonBuilder = new ButtonBuilder();
        appliancesButtonBuilder.setLabelName("Appliances");
        Button appliances = appliancesButtonBuilder.getButton();

        ButtonBuilder settingsButtonBuilder = new ButtonBuilder();
        settingsButtonBuilder.setLabelName("Settings");
        Button settings = settingsButtonBuilder.getButton();
        title.getStyleClass().add("title"); 
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);

        Row headerRowBuilder = new Row(20, date, title, appliances, settings);
        HBox header = headerRowBuilder.getRow();
        header.setPadding(new Insets(15));
        header.getStyleClass().add("header"); 
        HBox.setHgrow(title, Priority.ALWAYS);


        TimestampData prodData =
                (TimestampData) appData.Get("TimestampData");

        double prodVal = 0;

        if (prodData != null) {

            LocalDateTime now = LocalDateTime.now();

            String currentHour =
                    LocalDateTime.of(
                            now.getYear(),
                            now.getMonth(),
                            now.getDayOfMonth(),
                            now.getHour(),
                            0
                    ).toString();

            Double value =
                    prodData.GetTimestamps().get(currentHour);

            if (value != null) {
                prodVal = value;
            }
        }
        LabelBuilder productionBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(
                productionBuilder,
                "Current production"
        );
        Label production = productionBuilder.getLabel();


        LabelBuilder productionValueBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(
                productionValueBuilder,
                (int) prodVal + " W"
        );
        Label productionValue = productionValueBuilder.getLabel();


        LabelBuilder usageBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(
                usageBuilder,
                "Current usage"
        );
        Label usage = usageBuilder.getLabel();


        LabelBuilder usageValueBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(
                usageValueBuilder,
                "500 W"
        );
        Label usageValue = usageValueBuilder.getLabel();


        LabelBuilder priceBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(
                priceBuilder,
                "Current electricity price"
        );
        Label price = priceBuilder.getLabel();


        LabelBuilder priceValueBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(
                priceValueBuilder,
                " SEK/kWh"
        );
        Label priceValue = priceValueBuilder.getLabel();


// Uppdate Live
        PowerMarketRetriver x = new PowerMarketRetriver();
        ArrayList<Double> r = new ArrayList<>( PowerMarketParser.ParseToJson(x.GetPowerMarketInfo() ));
        double a = r.get(LocalTime.now().getHour()*4);

        priceValue.setText(a + " SEK/kWh");

        Timeline priceClock = new Timeline(
            //
            new KeyFrame(Duration.minutes(15), event -> {
                //
                PowerMarketRetriver pmsrr = new PowerMarketRetriver();
                ArrayList<Double> priceList = new ArrayList<>( PowerMarketParser.ParseToJson(pmsrr.GetPowerMarketInfo() ));
                double pris = priceList.get(LocalTime.now().getHour()*4);

                priceValue.setText(pris + " SEK/kWh");
            })
        );

        priceClock.setCycleCount(Timeline.INDEFINITE);
        priceClock.play();

        productionValue.getStyleClass().add("boxText"); 
        usageValue.getStyleClass().add("boxText"); 
        priceValue.getStyleClass().add("boxText"); 


        // Create three information boxes

        VBoxBuilder productionBoxBuilder = new VBoxBuilder();
        productionBoxBuilder.setSpacing(8);
        productionBoxBuilder.addChildren(
                production,
                productionValue
        );

        VBox productionBox = productionBoxBuilder.getVBox();


        VBoxBuilder usageBoxBuilder = new VBoxBuilder();
        usageBoxBuilder.setSpacing(8);
        usageBoxBuilder.addChildren(
                usage,
                usageValue
        );

        VBox usageBox = usageBoxBuilder.getVBox();


        VBoxBuilder priceBoxBuilder = new VBoxBuilder();
        priceBoxBuilder.setSpacing(8);
        priceBoxBuilder.addChildren(
                price,
                priceValue
        );

        VBox priceBox = priceBoxBuilder.getVBox();

        // Style the boxes
        for (VBox box : new VBox[]{
                productionBox, usageBox, priceBox
        }) {
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


        // Create the chart axes
        boolean hasForecast = forecast != null;

        BarChartBuilder chartBuilder =
                new BarChartBuilder();

        chartBuilder.setXAxisLabel("Time (local)");
        chartBuilder.setYAxisLabel("W");

        chartBuilder.setTitle(
                hasForecast
                        ? "Hourly solar irradiance"
                        : "Hourly production (demo)"
        );

        chartBuilder.setLegendVisible(false);
        chartBuilder.setAnimated(false);
        chartBuilder.setPrefHeight(330);

        BarChart<String, Number> chart =
                chartBuilder.getChart();

// Data for the chart
        XYChart.Series<String, Number> data =
                new XYChart.Series<>();

        if (hasForecast) {
            // Display real API data
            int count = forecast.size();

            for (int i = 0; i < count; i++) {
                String time = String.valueOf(forecast.get(i).GetTime().getHour());
                double irradiance = forecast.get(i).GetWatts();
                data.getData().add(new XYChart.Data<>(time, irradiance));
            }

        } else {

            // Temporary data until the API is connected
            data.getData().add(new XYChart.Data<>("9:00", 100));
            data.getData().add(new XYChart.Data<>("10:00", 120));
            data.getData().add(new XYChart.Data<>("11:00", 200));
            data.getData().add(new XYChart.Data<>("12:00", 180));
            data.getData().add(new XYChart.Data<>("13:00", 140));
            data.getData().add(new XYChart.Data<>("14:00", 150));
            data.getData().add(new XYChart.Data<>("15:00", 70));
        }

        chart.getData().add(data);
        new XYChart.Series<>();



        // Change the bars to yellow
        for (XYChart.Data<String, Number> item : data.getData()) {

            item.nodeProperty().addListener(
                    (obs, oldNode, newNode) -> {
                        if (newNode != null) {
                            newNode.setStyle("-fx-bar-fill: #4A90E2;");                        }
                    }
            );

            if (item.getNode() != null) {
                item.getNode().setStyle("-fx-bar-fill: #4A90E2;");
            }
        }

        // Open the existing settings page
        appliancesButtonBuilder.setEvent(event -> {
            clock.stop();
            root.viewAppliances(stage);
        });

        settingsButtonBuilder.setEvent(event -> {
            clock.stop();
            root.viewSettings(stage);
        });
        // Main layout
        VBoxBuilder layoutBuilder = new VBoxBuilder();

        layoutBuilder.setSpacing(20);

        layoutBuilder.addChildren(header, information, chart);

        layoutBuilder.setAlignment(Pos.TOP_CENTER);

        VBox layout = layoutBuilder.getVBox();

        Scene scene = new Scene(layout, 800, 550);

        // Add stylesheets (layout and style are seperate)
        scene.getStylesheets().add(CSSPath.layoutPath);
        scene.getStylesheets().add(CSSPath.stylePath);

        stage.setTitle("Solar App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}