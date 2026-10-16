package location.builder;

public interface ChartBuilder {

    void setTitle(String title);

    void setLegendVisible(boolean visible);

    void setAnimated(boolean animated);

    void setPrefHeight(double height);

    void setXAxisLabel(String label);

    void setYAxisLabel(String label);
}