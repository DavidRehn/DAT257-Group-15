package weatherdata;

import java.time.LocalDateTime;

/**
 *  Immutable class to represent a timestamp from the data received from the Open Meteo API.
 *  Each timestamp has a time (date & time) and a solar radiation value (W/m^2).
 */
public class ForecastTimestamp {
    private final LocalDateTime time;
    private final double solarRadiationWpM2;

    /** Constructor to create a ForecastTimestamp object from the given parameters.
     * @param time Timestamp when the radiation reading.
     * @param radiationWpM2 Expected solar radiation in W / m^2.
     */
    public ForecastTimestamp(LocalDateTime time, double radiationWpM2){
        this.time = time;
        this.solarRadiationWpM2 = radiationWpM2;
    }


    /** Returns the timestamp of the object.
     * @return Timestamp
     */
    public LocalDateTime Time(){return time;}

    /** Returns the solar radiaton value of the object.
     *  Unit: W / m^2.
     * @return  Solar raiaton value.
     */
    public double SolarRadiationWpM2(){return solarRadiationWpM2;}

    /** Returns a string representation of this object.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return "{" + time + ", " + solarRadiationWpM2 + "}";
    }
}
