package weatherdata;

import other.Direction;

public class HTTPSolarRequest {
    private final double latitude;      
    private final double longitude;     
    private final int tiltDeg;          // Tilt of solar panels
    private final int days;             // How many days the forecast should show
    private int azimuthDeg;        // Direction solar panels are facing 0-360 degrees, 0 = South, 90 = SOUTHWEST, 180 = north, etc..

    public HTTPSolarRequest(double latitude, double longitude, int tiltDeg, int days, Direction direction){
        this.latitude = latitude;
        this.longitude = longitude;
        this.tiltDeg = tiltDeg;
        this.days = days;
        this.azimuthDeg = DirectionToAzimuth(direction);
    }

    private static int DirectionToAzimuth(Direction dir){
        switch (dir) {
            case Direction.SOUTH:        return 0;      // Degrees from south (clock-wise)
            case Direction.SOUTHWEST:    return 45;
            case Direction.WEST:         return 90;
            case Direction.NORTHWEST:    return 135;
            case Direction.NORTH:        return 180;
            case Direction.NORTHEAST:    return 225;
            case Direction.EAST:         return 270;
            case Direction.SOUTHEAST:    return 315;
            default : return -1;    // Can never happen
        }
    }

    public double Latitude(){return latitude;}

    public double Longitude(){return longitude;}

    public int Tilt(){return tiltDeg;}

    public int Days(){return days;}

    public int Azimuth(){return azimuthDeg;}
}