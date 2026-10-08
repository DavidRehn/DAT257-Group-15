package weatherdata;

import java.time.LocalDateTime;

/** Immutable interface to represent a timestamp and corresponding wattage
 */
public abstract class Timestamp{
    private final LocalDateTime time;
    private final double watts;

    public Timestamp(LocalDateTime time, double watts){
        this.time = time;
        this.watts = watts;
    }

    public LocalDateTime GetTime() {return time;}

    public double GetWatts(){return watts;}

    @Override
    public String toString(){
        return "{" + GetTime() + ", " + GetWatts() + "}";
    }
}
