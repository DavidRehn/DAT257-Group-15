package weatherdata;

import java.time.LocalDateTime;

/**
 *  Immutable class to represent a timestamp from the data received from the Open Meteo API.
 *  Each timestamp has a time (date & time) and a solar radiation value (W/m^2).
 */
public class ForecastTimestamp extends Timestamp{

    /** Constructor to create a ForecastTimestamp object from the given parameters.
     * @param time Timestamp when the radiation reading.
     * @param watts Expected solar radiation in W / m^2.
     */
    public ForecastTimestamp(LocalDateTime time, double solarRadiationWPm2){
        super(time, solarRadiationWPm2);
    }

    /** Returns a string representation of this object.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return "{" + GetTime() + ", " + GetWatts() + "}";
    }
}
