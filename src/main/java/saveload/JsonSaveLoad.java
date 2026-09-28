package saveload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.FileAlreadyExistsException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

public class JsonSaveLoad implements SaveLoadInterface {
    private String filename ="SavedAppData.json";
    private JsonObjectBuilder builder;
    /** 
        * @pram 
    */
    public void save(Hashtable<String,Object> pairs){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        resetBuilder();
        for (HashMap.Entry<String, Object> en : pairs.entrySet()) {
            
            String key = en.getKey();
            Object val = en.getValue();
            if (val instanceof Integer){
                builder.add(key,((Integer)val).intValue());
            }else if (val instanceof Double){
                builder.add(key,((Double)val).doubleValue());
            }else if (val instanceof  String){
                builder.add(key,(String)val);
            }
        }
        JsonObject payload = builder.build();
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

        }
    }

    /** 
    */
    private void resetBuilder(){
        builder = Json.createObjectBuilder();
    }

    /** 
        * @return 
    */
    public Hashtable load(){
        Hashtable<String, Object> payload = new Hashtable<>();
        File file = new File(filename);
        try(Scanner reader = new Scanner(file)){
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
            }
        }catch(FileNotFoundException e){}

        return payload;
    }
}
