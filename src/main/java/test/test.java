package test;
import powermarketdata.PowerMarketParser;
import other.Direction;
import other.SavableData;
import other.SolarPanel;
import other.ZoneData;
import weatherdata.EnergyEstimator;
import weatherdata.HTTPSolarRequest;
import weatherdata.SolarRadiationRetreiver;
import weatherdata.WeatherParser;
//import powermarketdata.HTTPMarketRequest;
import powermarketdata.PowerMarketRetriver;
import saveload.JsonSaveLoad;

import java.util.ArrayList;
import java.util.Hashtable;

import weatherdata.EstimationTimestamp;
//import powermarketdata.*;

public class test {

    public static void main(String[] args) {
        // for testing sending & reciving from/for the Weather API
        /* 
        HTTPSolarRequest req = new HTTPSolarRequest(57.668, 11.998, 45, 1, Direction.SOUTH);
        WeatherParser parser = new WeatherParser();
        SolarRadiationRetreiver srr = new SolarRadiationRetreiver(new HTTPSolarRequest(11, 58, 45, 1, Direction.SOUTH), parser);
        */
        SolarPanel panel = new SolarPanel(30, 30, Direction.SOUTH, 0.2);
        ZoneData z = new ZoneData("SE2");
        //EnergyEstimator estimator = new EnergyEstimator(parser, panel);
        //srr.GetWeatherInfo(req);
        //estimator.BuildTimestampList();
        //ArrayList<EstimationTimestamp> timestamps = estimator.GetTimestamps();
        JsonSaveLoad saveLoad = new JsonSaveLoad();
        Hashtable<String, SavableData> data = new Hashtable<>();
        data.put("SolarPanel*0", panel);
        data.put("ZoneData*0", z);
        saveLoad.save(data);

        Hashtable<String, SavableData> result = saveLoad.load();
        
        System.out.println(result);

        // for testing sending & reciving from/for the Power Market API.
        
        //HTTPSolarRequest pmreq = new HTTPSolarRequest(57.668, 11.998, 45, 1, Direction.SOUTH);
        //PowerMarketRetriver pmsrr = new PowerMarketRetriver();
        //pmsrr.GetPowerMarketInfo(); //pmreq);
        
        //PowerMarketParser.ParseToJson(pmsrr.GetPowerMarketInfo() );
    }
}