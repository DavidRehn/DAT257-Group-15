package other;

import java.util.Hashtable;

import powermarketdata.PriceData;
import saveload.SaveLoadInterface;

public class Model {
    private Hashtable<String, SavableData> datatable;
    private SaveLoadInterface saveLoad;
    private int currentProfile=0;
    private int highestProfile=0;
    private PriceData prices = new PriceData();
    
    public Model(SaveLoadInterface saveLoad){
        prices.run();
        this.saveLoad = saveLoad;
        datatable=new Hashtable<>();
        
    }
    

    public SavableData Get(String key){
        return datatable.get(key + "*" + currentProfile);
    }
    public SavableData Get(String key,int i){
        return datatable.get(key + "*" + i);
    }

    public boolean ContainKey(String key){
        return datatable.containsKey(key);
    }
    public String GetName(int i){
        if(ContainKey("ProfileData*"+i)){
            return ((ProfileData)Get("ProfileData",i)).ProfileName();
        }else{
            return null;
        }
    }
    public int GetHighest(){return highestProfile;}
    public void AddProfile(String name){
        highestProfile++;
        datatable.put("ProfileData*"+highestProfile,new ProfileData(name,highestProfile));
    }
    public void RemoveProfile(int i){
        if(highestProfile==0){
            System.out.println("can not delete last profile");
            return;
        }
        System.out.println(i);
        ProfileData current=(ProfileData)datatable.get("ProfileData*"+i);
        for(String s : current.ProfileRef()){
                datatable.remove(s+"*"+i);
        }
        datatable.remove("ProfileData*"+i);
        while(datatable.containsKey("ProfileData*"+(i+1))){
            current = (ProfileData)datatable.get("ProfileData*"+(i+1));
            

            datatable.put("ProfileData*"+i,datatable.get("ProfileData*"+(i+1)));

            for(String s : current.ProfileRef()){
                datatable.put(s+"*"+i,datatable.get(s+"*"+(i+1)));
                datatable.remove(s+"*"+(i+1));
            }
            current.SetIndex(i);
            datatable.put("ProfileData*"+i,datatable.get("ProfileData*"+(i+1)));
            datatable.remove("ProfileData*"+(i+1));
            i++;
            
            
        }
        System.out.println(datatable);
        highestProfile--;
        SaveToDisc();
    }
    public void SwitchProfile(int i){
        currentProfile=i;
    }

    public void Store(String key, SavableData data){
        ((ProfileData)datatable.get("ProfileData*"+currentProfile)).add(key);
        datatable.put(key+"*"+currentProfile, data);
    }

    public boolean SaveToDisc(){
        return saveLoad.save(datatable);
    }
    
    public void LoadFromDisc(){
        datatable.clear();
        datatable.putAll(saveLoad.load());
        
        if(!datatable.containsKey("ProfileData*0")){
            datatable.clear();
            datatable.put("ProfileData*0",new ProfileData("Default"));
            System.out.println("failed to load complete profile, please make new save");
        }
        highestProfile=0;
        while(datatable.containsKey("ProfileData*"+(highestProfile+1))){
            highestProfile++;
        }
        
    }

    
    

    private void SetProfile(int profile){
        this.currentProfile = profile;
    }

    public void UpdatePrices(){
        prices.run();
    }
    public PriceData GetPrices(){
        return prices;
    }
}
/* 
public interface SaveLoadInterface {
    public void save(Hashtable<String,Object> values);
    
    public Hashtable<String,Object> load();
}*/