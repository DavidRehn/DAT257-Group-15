package weatherdata;

//weatherdata
import other.Direction;
import weatherdata.HTTPSolarRequest;
import weatherdata.SolarRadiationRetreiver;
//saveload
import saveload.JsonSaveLoad;
import saveload.SaveLoadInterface;
import java.util.Hashtable;

public class CallWeatherAPI{
    public SaveLoadInterface saveLoad = new JsonSaveLoad();
    public Hashtable<String, Object> saveData = new Hashtable<>();
    /* 
    public void CallWeatherAPI{
        
    }

    public void callAPI(){
        HTTPSolarRequest req = new HTTPSolarRequest(saveData.get("latitude"), saveData.get("longitude"), saveData.get("tilt"), 1, saveData.get("direction"));
        SolarRadiationRetreiver srr = new SolarRadiationRetreiver();
        srr.GetWeatherInfo(req);
    }*/
}