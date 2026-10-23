
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
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.StringConverter;
import location.builder.ButtonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class SolarChartView {
    private static ArrayList<Button> dayButtons = new ArrayList<>();
    private static int day;
    // Create the complete chart
    public static VBox create(List<EstimationTimestamp> forecast) {
        for (EstimationTimestamp estimationTimestamp : forecast) {
            System.out.println(estimationTimestamp);
        }
        // X axis: 24-hour time
        NumberAxis xAxis = new NumberAxis(0, 23, 2);
        xAxis.setAutoRanging(false);
        xAxis.setMinorTickVisible(false);
        xAxis.setLabel(LanguageManager.get("chart.time"));

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

        if (forecast != null){
            dayButtons = new ArrayList<>();
            int days = forecast.size() / 24;
            for (int i = 0; i < days; i++){
                Button button;
                String label;
                if(i == 0){
                    button = new Button("Today");
                    button.getStyleClass().addAll("day-tab", "day-tab-active");
                }else{
                    String date = String.valueOf(forecast.get(i*24).GetTime().getDayOfMonth());
                    String ending;
                    switch (date) {
                        case "1":
                        case "21":
                        case "31":
                            ending = "st";
                            break;
                        case "2":
                        case "22":
                            ending = "nd";
                            break;
                        case "3":
                        case "23":
                            ending = "rd";
                            break;
                        default:
                            ending = "th";
                            break;
                    }
                    button = new Button(date + ending);
                    button.getStyleClass().add("day-tab");
                }
                button.setOnAction(event -> {
                    setActiveDayButton(button);
                    updateChart(chart, forecast);
                });
                dayButtons.add(button);
            }
        }

        // Button container
        HBox daySwitcher = new HBox(10);    // Margin between buttons
        daySwitcher.getChildren().addAll(dayButtons);
        daySwitcher.setAlignment(Pos.CENTER_LEFT);

        // Chart header
        HBox header = new HBox(15, title, daySwitcher);
        header.setAlignment(Pos.CENTER_LEFT);

        // Forecast status message
        Label message = new Label();
        message.getStyleClass().add("chart-status");

        message.setWrapText(true);
        // Main chart panel
        VBox chartBox = new VBox(12, header, chart, message);
        chartBox.getStyleClass().add("solar-chart-panel");
        VBox.setVgrow(chart, Priority.ALWAYS);
        

        // Show Today when opening Home
        updateChart(chart, forecast);
        return chartBox;
    }


    // SELECTED BUTTON


    private static void setActiveDayButton( Button active) {
        for (Button button : dayButtons) {
            button.getStyleClass().remove("day-tab-active");
        }
        active.getStyleClass().add("day-tab-active");
    }


    // UPDATE CHART


    private static void updateChart(AreaChart<Number, Number> chart, List<EstimationTimestamp> forecast) {

        // New series for the selected day
        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        LocalDate selectedDay = LocalDate.now();
        int day = 100000;   // Should always be set by loop below, larde number to make errors obvious.
        for (int i = 0; i < dayButtons.size(); i++){
            if (dayButtons.get(i).getStyleClass().contains("day-tab-active")){
                day = i;
            }
        }
        System.out.println("Base day: "+selectedDay);
        selectedDay = selectedDay.plusDays(day);
        System.out.println("Offset day: "+selectedDay);

        // If no forecast exists
        if (forecast == null || forecast.isEmpty()) {
            addDemoData(series);
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
                //System.out.println("Watts: "+watts);
                if (!Double.isFinite(watts)) {continue;}
                int hour = time.getHour();
                //System.out.println("Time: "+time);
                series.getData().add(new XYChart.Data<>(hour, watts));
            }
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

        String information = String.format(Locale.US, "%02d:00\n%.0f W", hour, watts);
        // Floating tooltip
        Tooltip tooltip = new Tooltip(information);
        tooltip.getStyleClass().add("chart-hover-card");

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
