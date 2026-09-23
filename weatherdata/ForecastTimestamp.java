package weatherdata;

import java.time.LocalDateTime;

public class ForecastTimestamp {
    private LocalDateTime time;
    private double solarRadiationWpM2;

    public ForecastTimestamp(LocalDateTime time, double radiationWpM2){
        this.time = time;
        this.solarRadiationWpM2 = radiationWpM2;
    }

    public LocalDateTime Time(){return time;}

    public double SolarRadiationWpM2(){return solarRadiationWpM2;}
}
