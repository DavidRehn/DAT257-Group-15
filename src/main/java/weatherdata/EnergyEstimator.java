package weatherdata;

import java.util.ArrayList;

import other.SolarPanel;

public class EnergyEstimator {
    private WeatherParser parser;
    private SolarPanel panel;
    private ArrayList<EstimationTimestamp> timestamps;

    public EnergyEstimator(WeatherParser parser,  SolarPanel panel){
        this.parser = parser;
        this.panel = panel;
        timestamps = new ArrayList<>();
    }

    public void BuildTimestampList(){
        for (ForecastTimestamp forecast : parser.GetTimestamps()) {
            timestamps.add(new EstimationTimestamp(forecast.GetTime(), Estimate(forecast.GetWatts())));
        }
    }


    private double Estimate(double WpM2){
        return WpM2 * panel.Area() * panel.Efficiency();
    }

    public ArrayList<EstimationTimestamp> GetTimestamps(){return timestamps;}
}
