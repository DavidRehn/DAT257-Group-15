package other;

import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;

public class ProfileData implements SavableData{
    private String profileName="Default";
    private int index=0;
    private ArrayList<String> profileData = new ArrayList<String>();

    public ProfileData(String n){
        this.profileName = n;

        
    }
    public ProfileData(String n,int i){
        this.profileName = n;
        this.index=i;
    }
    public ProfileData(JsonObject obj){
        this.profileName = obj.getString("profile");
        this.index = obj.getInt("index");
        this.profileData = new ArrayList<String>();
        obj.getJsonArray("refrences").forEach(key -> profileData.add(((JsonString)key).getString()));


    }

    public String ProfileName(){return  profileName;}
    public int ProfileIndex(){return  index;}
    public ArrayList<String> ProfileRef(){return  profileData;}
    public void add(String s){if(!profileData.contains(s)){profileData.add(s);}}
    public void SetIndex(int i){index=i;}
    @Override
    public JsonObject ToJsonObj(String key){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

        builder.add("profile", profileName);
        builder.add("index",index);
        for(String s : profileData){
            arrBuilder.add(s);
        }
        builder.add("refrences",arrBuilder.build());

        JsonObject obj = builder.build();
        builder = Json.createObjectBuilder();
        builder.add(key, obj);
        return builder.build();
    }

    
}
