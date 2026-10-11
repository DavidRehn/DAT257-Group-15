package location.builder;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;

public class BarChartBuilder implements ChartBuilder {

    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    private BarChart<String, Number> chart;

    public BarChartBuilder() {

        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();

        chart = new BarChart<>(xAxis, yAxis);
    }

    @Override
    public void setTitle(String title) {
        chart.setTitle(title);
    }

    @Override
    public void setLegendVisible(boolean visible) {
        chart.setLegendVisible(visible);
    }

    @Override
    public void setAnimated(boolean animated) {
        chart.setAnimated(animated);
    }

    @Override
    public void setPrefHeight(double height) {
        chart.setPrefHeight(height);
    }

    @Override
    public void setXAxisLabel(String label) {
        xAxis.setLabel(label);
    }

    @Override
    public void setYAxisLabel(String label) {
        yAxis.setLabel(label);
    }

    public BarChart<String, Number> getChart() {
        return chart;
    }
}