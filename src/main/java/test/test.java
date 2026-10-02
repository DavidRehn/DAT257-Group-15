package test;
import powermarketdata.PowerMarketParser;
/* 
import other.Direction;
import weatherdata.HTTPSolarRequest;
import weatherdata.SolarRadiationRetreiver;
*/
//import powermarketdata.HTTPMarketRequest;
import powermarketdata.PowerMarketRetriver;
//import powermarketdata.*;

public class test {

    public static void main(String[] args) {
        // for testing sending & reciving from/for the Weather API
        /* 
        HTTPSolarRequest req = new HTTPSolarRequest(57.668, 11.998, 45, 1, Direction.SOUTH);
        SolarRadiationRetreiver srr = new SolarRadiationRetreiver();
        srr.GetWeatherInfo(req);
        */

        // for testing sending & reciving from/for the Power Market API.
        
        //HTTPSolarRequest pmreq = new HTTPSolarRequest(57.668, 11.998, 45, 1, Direction.SOUTH);
        PowerMarketRetriver pmsrr = new PowerMarketRetriver();
        //pmsrr.GetPowerMarketInfo(); //pmreq);
        
        PowerMarketParser.ParseToJson(pmsrr.GetPowerMarketInfo() );
    }
}