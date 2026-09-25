
package location;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javax.json.JsonArray;
public class StartMenu extends Application {
    private static JsonArray forecastTimes;
    private static JsonArray forecastRadiation;

    public static void setForecastData(
            JsonArray times,
            JsonArray radiation
    ) {
        forecastTimes = times;
        forecastRadiation = radiation;
    }
    @Override
    public void start(Stage stage) {

        // Header
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("EEE HH:mm");

        Label date = new Label(
                LocalDateTime.now().format(formatter)
        );


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

        Label title = new Label("Solar App");
        Button settings = new Button("Settings");

        title.setStyle("-fx-font-size: 22px;");
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);

        HBox header = new HBox(20, date, title, settings);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #6096e6;");
        HBox.setHgrow(title, Priority.ALWAYS);

        // Temporary values - APIs will be connected later

        Label production = new Label("Current production");
        Label productionValue = new Label("100 W");

        Label usage = new Label("Current usage");
        Label usageValue = new Label("500 W");

        Label price = new Label("Current electricity price");
        Label priceValue = new Label("2 SEK/kWh");

// Make the numbers bigger and bold
        productionValue.setStyle(
                "-fx-font-size: 25px; -fx-font-weight: bold;"
        );

        usageValue.setStyle(
                "-fx-font-size: 25px; -fx-font-weight: bold;"
        );

        priceValue.setStyle(
                "-fx-font-size: 25px; -fx-font-weight: bold;"
        );


        // Create three information boxes

        VBox productionBox =
                new VBox(8, production, productionValue);

        VBox usageBox =
                new VBox(8, usage, usageValue);

        VBox priceBox =
                new VBox(8, price, priceValue);

        // Style the boxes
        for (VBox box : new VBox[]{
                productionBox, usageBox, priceBox
        }) {
            box.setPrefWidth(220);
            box.setMinHeight(95);
            box.setPadding(new Insets(20));
            box.setAlignment(Pos.CENTER_LEFT);

            box.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-color: #DDE3EC;" +
                            "-fx-border-radius: 8;" +
                            "-fx-background-radius: 8;"
            );
        }

        // Put the boxes next to each other
        HBox information = new HBox(
                15, productionBox, usageBox, priceBox
        );

        information.setAlignment(Pos.CENTER);
        information.setPadding(new Insets(20));


        // Create the chart axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();


        xAxis.setLabel("Time (GMT)");

        boolean hasForecast =
                forecastTimes != null && forecastRadiation != null;

        yAxis.setLabel(hasForecast ? "W/m²" : "W");

// Create the bar chart
        BarChart<String, Number> chart =
                new BarChart<>(xAxis, yAxis);

        chart.setTitle(
                hasForecast
                        ? "Hourly solar irradiance"
                        : "Hourly production (demo)"
        );

        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setPrefHeight(330);

// Data for the chart
        XYChart.Series<String, Number> data =
                new XYChart.Series<>();

        if (hasForecast) {

            // Display real API data
            int count = Math.min(
                    forecastTimes.size(),
                    forecastRadiation.size()
            );

            for (int i = 0; i < count; i++) {

                String time = forecastTimes.getString(i);

                double irradiance = forecastRadiation
                        .getJsonNumber(i)
                        .doubleValue();

                // Extract the hour from the timestamp
                String hour = time.substring(11, 16);

                data.getData().add(
                        new XYChart.Data<>(hour, irradiance)
                );
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
        settings.setOnAction(event -> {
            clock.stop();
            new LocationApp().start(stage);
        });

        // Main layout
        VBox layout = new VBox(20, header, information, chart);
        layout.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(layout, 800, 550);

        stage.setTitle("Solar App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}