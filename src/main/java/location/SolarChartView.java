
package location;

import ui.LanguageManager;
import weatherdata.EstimationTimestamp;

import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class SolarChartView {

    // Create the complete chart
    public static VBox create(List<EstimationTimestamp> forecast) {
        // X axis: 24-hour time
        NumberAxis xAxis = new NumberAxis(0, 23, 2);
        xAxis.setAutoRanging(false);
        xAxis.setMinorTickVisible(false);
        xAxis.setLabel(LanguageManager.get("chart.time")
        );

        // Format hours as 00:00, 02:00...
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
        public String toString(Number value) {return String.format(Locale.US, "%02d:00", value.intValue());}

         @Override
          public Number fromString(String text) {return Integer.parseInt(text.split(":")[0]);}}
        );

        // Y axis: Watts
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("W");
        yAxis.setForceZeroInRange(true);
        yAxis.setMinorTickVisible(false);
        // Create AreaChart
        AreaChart<Number, Number> chart = new AreaChart<>(xAxis, yAxis);

        chart.setAnimated(false);
        chart.setCreateSymbols(true);
        chart.setLegendVisible(false);
        chart.setVerticalGridLinesVisible(false);
        chart.setHorizontalGridLinesVisible(true);

        chart.setPrefHeight(350);
        chart.setMinHeight(300);
        chart.getStyleClass().add("modern-line-chart");

        // Chart title
        Label title = new Label();
        title.getStyleClass().add("solar-chart-title");
        // Today button
        Button todayButton = new Button(LanguageManager.get("chart.today"));
        // Tomorrow button
        Button tomorrowButton = new Button(LanguageManager.get("chart.tomorrow"));
        todayButton.getStyleClass().addAll("day-tab", "day-tab-active");
        tomorrowButton.getStyleClass().add("day-tab");

        // Button container
        HBox daySwitcher = new HBox(8, todayButton, tomorrowButton);
        daySwitcher.setAlignment(Pos.CENTER_RIGHT);

        // Spacer between title and buttons
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        // Chart header
        HBox header = new HBox(15, title, spacer, daySwitcher);
        header.setAlignment(Pos.CENTER_LEFT);

        // Forecast status message
        Label message = new Label();
        message.getStyleClass().add("chart-status");

        message.setWrapText(true);
        // Main chart panel
        VBox chartBox = new VBox(12, header, chart, message);
        chartBox.getStyleClass().add("solar-chart-panel");
        VBox.setVgrow(chart, Priority.ALWAYS);
        // Click Today
        todayButton.setOnAction(event -> {setActiveDayButton(todayButton, tomorrowButton);
            updateChart(chart, forecast, false, title, message);
        });

        // Click Tomorrow
        tomorrowButton.setOnAction(event -> {setActiveDayButton(tomorrowButton, todayButton);
            updateChart(chart, forecast, true, title, message
            );
        });
        // Show Today when opening Home
        updateChart(chart, forecast, false, title, message);
        return chartBox;
    }


    // SELECTED BUTTON


    private static void setActiveDayButton(
            Button active, Button inactive) {inactive.getStyleClass().remove("day-tab-active");
        if (!active.getStyleClass().contains("day-tab-active")) {
            active.getStyleClass().add("day-tab-active"
            );
        }
    }


    // UPDATE CHART


    private static void updateChart(AreaChart<Number, Number> chart,
            List<EstimationTimestamp> forecast, boolean tomorrow, Label title, Label message) {

        // New series for the selected day
        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        LocalDate selectedDay = LocalDate.now();
        if (tomorrow) {selectedDay = selectedDay.plusDays(1);
        }

        // If no forecast exists
        if (forecast == null || forecast.isEmpty()) {
            if (!tomorrow) {title.setText(LanguageManager.get("chart.demo"));
                addDemoData(series);
                message.setText(LanguageManager.get("chart.demo.notice"));
            } else {
                title.setText(LanguageManager.get("chart.estimated"));
                message.setText(LanguageManager.get("chart.no.data"));
            }

        } else {
            // Real forecasttitle.setText(LanguageManager.get("chart.estimated"));

            // Sort timestamps
            ArrayList<EstimationTimestamp> sorted = new ArrayList<>(forecast);
            sorted.sort(Comparator.comparing(EstimationTimestamp::GetTime));

            // Add only selected day's values
            for (EstimationTimestamp item : sorted) { LocalDateTime time = item.GetTime();
                if (!time.toLocalDate().equals(selectedDay)) {continue;
                }
                double watts = item.GetWatts();
                if (!Double.isFinite(watts)) {continue;}
                int hour = time.getHour();
                series.getData().add(new XYChart.Data<>(hour, watts));
            }

            if (series.getData().isEmpty()) {message.setText(LanguageManager.get("chart.no.data"));
            } else {message.setText("");}
        }

        // Replace the old series
        chart.getData().setAll(series);
        // Apply hover to new data points
        for (XYChart.Data<Number, Number> item : series.getData()) {enableHover(item);}
    }
    // DEMO DATA

    private static void addDemoData(XYChart.Series<Number, Number> series) {
        int[] hours = {0, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 23
        };
        double[] watts = {0, 0, 0, 100, 120, 200, 180, 140, 150, 70, 100, 0, 0
        };
        for (int i = 0; i < hours.length; i++) {
            series.getData().add(new XYChart.Data<>(hours[i], watts[i])
            );
        }
    }

    // HOVER ON CHART POINTS


    private static void enableHover(XYChart.Data<Number, Number> item) {

        // Listen for the JavaFX point node
        item.nodeProperty().addListener(
       (obs, oldNode, newNode) -> {if (newNode != null) {setupHover(newNode, item);}
                });

        // The node may already exist
        if (item.getNode() != null) {setupHover(item.getNode(), item);}}

    private static void setupHover(Node point, XYChart.Data<Number, Number> item) {

        int hour = item.getXValue().intValue();
        double watts = item.getYValue().doubleValue();

        String information = String.format(Locale.US, "%02d:00\n%.1f W", hour, watts);
        // Floating tooltip
        Tooltip tooltip = new Tooltip(information);

        tooltip.setStyle("-fx-background-color: #172554;" + "-fx-text-fill: white;" + "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" + "-fx-background-radius: 10;" + "-fx-padding: 12 16;");

        // Mouse enters
        point.setOnMouseEntered(event -> {
            tooltip.show(point, event.getScreenX() + 15, event.getScreenY() - 75);

            ScaleTransition grow = new ScaleTransition(Duration.millis(150), point);
            grow.setToX(1.5);
            grow.setToY(1.5);
            grow.play();
        });

        // Mouse moves
        point.setOnMouseMoved(event -> {if (tooltip.isShowing()) {
            tooltip.setAnchorX(event.getScreenX() + 15);
            tooltip.setAnchorY(event.getScreenY() - 75);
            }
        });

        // Mouse exits
        point.setOnMouseExited(event -> {tooltip.hide();
            ScaleTransition shrink = new ScaleTransition(Duration.millis(150), point);
            shrink.setToX(1);
            shrink.setToY(1);
            shrink.play();
        });
    }
}
