package saveload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.nio.file.FileAlreadyExistsException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import other.SavableData;

public class JsonSaveLoad implements SaveLoadInterface {
    private String filename ="SavedAppData.json";

    public JsonSaveLoad() {
    }

    
    // For casting loaded jsonobjects
    private String c;
    private int id;
    
    /** [{"solarPanel*0" : {object}}, {"Zone*0" : {object}}, ...]
    */
   @Override 
    public boolean save(Hashtable<String,SavableData> values){
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (HashMap.Entry<String, SavableData> en : values.entrySet()) {
            
            String key = en.getKey();
            SavableData val = en.getValue();
            builder.add(val.ToJsonObj(key)); 
        }
        JsonArray payload = builder.build();
        System.out.println(payload.toString());
        try {
            File file = new File(filename);
            try {
                file.createNewFile();
            } catch (FileAlreadyExistsException e) {
                System.out.println("FileExists");
            }
            FileWriter fileWriter = new FileWriter(filename);
            fileWriter.write(payload.toString());
            fileWriter.flush();
            System.out.println("saved in file");
        } catch (IOException e) {
            return false;
        }
        return true;
    }



    /** 
        * @return 
    */
    public Hashtable<String, SavableData> load(){
        Hashtable<String, SavableData> payload = new Hashtable<>();
        File file = new File(filename);
        JsonArrayBuilder builder = Json.createArrayBuilder();
        try(Scanner reader = new Scanner(file)){
            reader.useDelimiter("\\A");

            while (reader.hasNext()) {
                JsonReader read = Json.createReader(new StringReader(reader.next()));
                JsonArray arr = read.readArray();
                System.out.println(arr);
                read.close();
                for (JsonValue val : arr) {
                    System.out.println(val);
                    
                    GetClass(val);
                    for (JsonObject.Entry<String, JsonValue> en : ((JsonObject)val).entrySet()) {
                        try{
                            Class data = Class.forName("other."+ c);
                            Class[] paraType = {Class.forName("javax.json.JsonObject")};
                            System.out.println(paraType[0]);
                            Constructor<SavableData> con = data.getConstructor(paraType);
                            SavableData obj = con.newInstance(((JsonObject)val).getJsonObject(en.getKey()));
                            
                                String key = en.getKey();
                                payload.put(key,obj);
                            
                                
                        }catch(Exception e){
                            e.printStackTrace();
                            
                        }
                }
                }
            }
            /* 
            while(reader.hasNextLine()){
                JsonReader read = Json.createReader(new StringReader(reader.nextLine()));
                JsonObject obj = read.readObject();
                read.close();
                
                for (JsonObject.Entry<String, JsonValue> en : obj.entrySet()) {
                    String key = en.getKey();
                    JsonValue val = en.getValue();




                    if (val instanceof JsonNumber){
                        if(((JsonNumber) val).isIntegral()){
                            payload.put(key,((JsonNumber) val).intValue());
                        }else{
                            payload.put(key,((JsonNumber) val).doubleValue());
                        }
                    }else if (val instanceof JsonString){
                        payload.put(key,((JsonString) val).getString());
                    }
                }
            }*/
        }catch(FileNotFoundException e){}

        return payload;
    }

    /** Helper function to get the class to cast a loaded object to, as well as the id of the object.
     * @param val JsonValue loaded from the array.
     */
    private void GetClass(JsonValue val){
        JsonObject obj = (JsonObject)val;
        Object[] arr = obj.keySet().toArray();
        String s = (String) arr[0];
        c = s.substring(0, s.indexOf("*"));
        System.out.println(c);
        id = Integer.parseInt(s.substring(s.indexOf("*") + 1, s.length()));
        System.out.println(id);
    }
}
