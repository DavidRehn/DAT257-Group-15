package test;

import other.Direction;
import weatherdata.HTTPSolarRequest;
import weatherdata.SolarRadiationRetreiver;

public class test {

    public static void main(String[] args) {
        HTTPSolarRequest req = new HTTPSolarRequest(57.668, 11.998, 45, 1, Direction.SOUTH);
        SolarRadiationRetreiver srr = new SolarRadiationRetreiver();
        srr.GetWeatherInfo(req);
    }
}