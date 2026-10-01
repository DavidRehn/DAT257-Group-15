package weatherdata;

import java.util.ArrayList;

import other.SolarPanel;

/** Class to handle the calculation of the electricity production estimate.
 */
public class EnergyEstimator {
    private WeatherParser parser;
    private SolarPanel panel;
    private ArrayList<EstimationTimestamp> timestamps;

    // NOCT parameters (constants)
    private static final double NOCT = 45.0;       // °C
    private static final double NOCT_AMBIENT = 20.0; // °C
    private static final double NOCT_IRRADIANCE = 800.0; // W/m²

    /** Constructor to build an object from the provided parameters.
     * @param parser Instance of a WeatherParser object.
     * @param panel Instance of a SolarPanel object.
     */
    public EnergyEstimator(WeatherParser parser,  SolarPanel panel){
        this.parser = parser;
        this.panel = panel;
        timestamps = new ArrayList<>();
    }

    /** Function to estimate a power output for all timestamps.
     */
    public void BuildTimestampList(){
        for (ForecastTimestamp forecast : parser.GetTimestamps()) {
            timestamps.add(new EstimationTimestamp(forecast.GetTime(), Estimate(forecast.GetWatts(), forecast.GetTemp())));
        }
    }

    /** Function to calculate an estimation of the electricity production of a solar array.
     * @param WpM2 Solar irradiance (W/m^2).
     * @param ambientTemp Ambient (air) temperature (°C).
     * @return Power estimate (W).
     */
    private double Estimate(double WpM2, double ambientTemp){
        return WpM2 * panel.Area() * panel.Efficiency() * TempCoefficient(ambientTemp, WpM2);
    }

    /** Returns the list of timestamps with corresponding electricity production estimate. 
     * @return List with timestamps.
     */
    public ArrayList<EstimationTimestamp> GetTimestamps(){return timestamps;}

    /** Function to calculate the power temperature coefficient, meaning efficiency at a certain temperature. 
     * @param ambientTemp Ambient (air) temperature (°C).
     * @param irradiance Solar irradiance (W/m^2).
     * @return Power temperature coefficient.
     */
    private double TempCoefficient(double ambientTemp, double irradiance){     
        return 1 + -0.004 * (CellTemp(ambientTemp, irradiance) - 25.0); // Hardcoded coefficient = -0.4%/°C, baseline = 25°C
    }

    /** Fuction to estimate the temperature of the solar array based on ambient temperature and nominal operating cell temperature (NOCT).
     *  Based on the function Tcell = Tambient + ((NOCT - NOCT-ambient) / NOCT-irradiance) * irradiance.
     * @param ambientTemp Ambient (air) temperature (°C).
     * @param irradiance Solar irradience (W/m^2).
     * @return Temperature of the solar array.
     */
    private double CellTemp(double ambientTemp, double irradiance){
        return ambientTemp + ((NOCT - NOCT_AMBIENT)/NOCT_IRRADIANCE) * irradiance;
    }
}
