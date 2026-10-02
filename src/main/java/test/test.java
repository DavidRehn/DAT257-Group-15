package test;
import powermarketdata.PowerMarketParser;
import other.Direction;
import other.SolarPanel;
import weatherdata.EnergyEstimator;
import weatherdata.HTTPSolarRequest;
import weatherdata.SolarRadiationRetreiver;
import weatherdata.WeatherParser;
//import powermarketdata.HTTPMarketRequest;
import powermarketdata.PowerMarketRetriver;
import java.util.ArrayList; 
import weatherdata.EstimationTimestamp;
//import powermarketdata.*;

public class test {

    public static void main(String[] args) {
        // for testing sending & reciving from/for the Weather API
        
        HTTPSolarRequest req = new HTTPSolarRequest(57.668, 11.998, 45, 1, Direction.SOUTH);
        WeatherParser parser = new WeatherParser();
        SolarRadiationRetreiver srr = new SolarRadiationRetreiver(new HTTPSolarRequest(11, 58, 45, 1, Direction.SOUTH), parser);
        SolarPanel panel = new SolarPanel(30, 0.2);
        EnergyEstimator estimator = new EnergyEstimator(parser, panel);
        srr.GetWeatherInfo(req);
        estimator.BuildTimestampList();
        ArrayList<EstimationTimestamp> timestamps = estimator.GetTimestamps();

        for (EstimationTimestamp time : timestamps) {
            System.out.println(time);
        }
        

        // for testing sending & reciving from/for the Power Market API.
        
        //HTTPSolarRequest pmreq = new HTTPSolarRequest(57.668, 11.998, 45, 1, Direction.SOUTH);
        //PowerMarketRetriver pmsrr = new PowerMarketRetriver();
        //pmsrr.GetPowerMarketInfo(); //pmreq);
        
        //PowerMarketParser.ParseToJson(pmsrr.GetPowerMarketInfo() );
    }
}