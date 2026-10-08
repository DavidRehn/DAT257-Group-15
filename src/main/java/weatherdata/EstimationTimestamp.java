package weatherdata;

import java.sql.Time;
import java.time.LocalDateTime;

/**
 *  Immutable class to represent a timestamp with a corresponding energy production estimate (W).
 */
public class EstimationTimestamp extends Timestamp implements Comparable{

    public EstimationTimestamp(LocalDateTime time, double watts){
        super(time, watts);
    }

    public int compareTo(Object timestamp){
        EstimationTimestamp date = (EstimationTimestamp)timestamp;
        return this.GetTime().compareTo(date.GetTime());
    }
}
