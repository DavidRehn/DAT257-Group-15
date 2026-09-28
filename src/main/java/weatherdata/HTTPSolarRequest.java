package weatherdata;

import other.Direction;

/** 
 * Immutable class to represent the parameters needed for a request to the Open Meteo Api for Global Tilted Radiation (GTI) data.
 */
public class HTTPSolarRequest {
    private final double latitude;      
    private final double longitude;     
    private final int tiltDeg;          // Tilt of solar panels
    private final int days;             // How many days the forecast should show
    private int azimuthDeg;        // Direction solar panels are facing 0-360 degrees, 0 = South, 90 = SOUTHWEST, 180 = north, etc..

    /** Constructor to create a HTTPSolarRequest object with the provided parameters.
     * @param latitude  Latitude of the solar array.
     * @param longitude Longitude of the solar array.
     * @param tiltDeg Tilt of the solar array (in degrees).
     * @param days Number of days in advance to get a forecast for (max 16).
     * @param direction Direction the solar array is facing.
     */
    public HTTPSolarRequest(double latitude, double longitude, int tiltDeg, int days, Direction direction){
        this.latitude = latitude;
        this.longitude = longitude;
        this.tiltDeg = tiltDeg;
        this.days = days;
        this.azimuthDeg = DirectionToAzimuth(direction);
    }

    /** Function for getting the azimuth used in the api call from a direction.
     *  Azimuth is represented as degrees from south (clockwise).
     * @param dir Direction to get azimuth from.
     * @return Azimuth in degrees from south (clockwise).
     */
    private static int DirectionToAzimuth(Direction dir){
        switch (dir) {
            case SOUTH:        return 0;      // Degrees from south (clock-wise)
            case SOUTHWEST:    return 45;
            case WEST:         return 90;
            case NORTHWEST:    return 135;
            case NORTH:        return 180;
            case NORTHEAST:    return 225;
            case EAST:         return 270;
            case SOUTHEAST:    return 315;
            default : return -1;    // Can never happen
        }
    }

    /** Returns the latitude value of the object.
     * @return Latitude.
     */
    public double Latitude(){return latitude;}

    /** Returns the logitude value of the object.
     * @return Longitude.
     */
    public double Longitude(){return longitude;}

    /** Returns the tilt of the object.
     * @return Tilt in degrees.
     */
    public int Tilt(){return tiltDeg;}

    /** Returns the number of days to get a forecast for.
     * @return Days in advance of forecast.
     */
    public int Days(){return days;}

    /** Returns the azimuth of the object.
     * @return Azimuth in degrees from south (clockwise).
     */
    public int Azimuth(){return azimuthDeg;}
}