package other;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class ProfileData implements SavableData{
    private String profileName;

    public ProfileData(String n){
        this.profileName = n;
    }
    public ProfileData(JsonObject obj){
        this.profileName = obj.getString("profileName");
    }

    public String ProfileName(){return  profileName;}
    

    @Override
    public JsonObject ToJsonObj(String key){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("profile", profileName);
        JsonObject obj = builder.build();
        builder = Json.createObjectBuilder();
        builder.add(key, obj);
        return builder.build();
    }

    
}
