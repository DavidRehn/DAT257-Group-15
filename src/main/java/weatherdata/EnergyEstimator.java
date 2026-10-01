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
            timestamps.add(new EstimationTimestamp(forecast.GetTime(), Estimate(forecast.GetWatts(), forecast.GetTemp())));
        }
    }


    private double Estimate(double WpM2, double ambientTemp){
        return WpM2 * panel.Area() * panel.Efficiency() * TempCoefficient(ambientTemp, WpM2);    //todo: include temp coefficient
    }

    public ArrayList<EstimationTimestamp> GetTimestamps(){return timestamps;}


    private double TempCoefficient(double ambientTemp, double irradiance){     
        return 1 + -0.004 * (CellTemp(ambientTemp, irradiance) - 25.0); // Hardcoded coefficient = -0.4%/°C, baseline = 25°C
    }

    private double CellTemp(double ambientTemp, double irradiance){    // approximation of cell temp
        return ambientTemp + ((45 - 20)/800) * irradiance;  // Hardcoded NOCT = 45°C, 
    }
}
