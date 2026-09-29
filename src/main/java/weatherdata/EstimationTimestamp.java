package weatherdata;

import java.sql.Time;
import java.time.LocalDateTime;

/**
 *  Immutable class to represent a timestamp with a corresponding energy production (W)
 */
public class EstimationTimestamp extends Timestamp{

    public EstimationTimestamp(LocalDateTime time, double watts){
        super(time, watts);
    }
}
