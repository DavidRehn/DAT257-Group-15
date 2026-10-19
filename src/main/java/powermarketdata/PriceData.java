package powermarketdata;

import java.util.ArrayList;
import java.util.HashMap;
public class PriceData implements Runnable{
    private HashMap<String,ArrayList<Double>> storedData=new HashMap<>();
    

    public void run() {
                try {
                    PowerMarketRetriver provider =
                            new PowerMarketRetriver();

                    ArrayList<Double> prices = new ArrayList<>(
                            PowerMarketParser.ParseToJson(
                                    provider.GetPowerMarketInfo()
                            )
                    );

                    storedData.put("SE3", prices);

                } catch (Exception exception) {

                    System.err.println(
                            "Could not update electricity price: "
                                    + exception.getMessage()
                    );
                }
            

            
        }; 

        public Double GetCurrentPrice(String zone, int hour, int offset){
            if(storedData.get(zone)==null){
                return null;
            }
            int index = (hour * 4)+offset;
            
            double value = (storedData.get(zone)).get(index);
            return value;
        }

        public ArrayList<Double> Get(String Key){
            if(!storedData.containsKey(Key)){
                return null;
            }
            return storedData.get(Key);
        }

 }

