import java.lang.constant.DirectMethodHandleDesc;
import java.net.http.HttpRequest;

import javax.net.ssl.HttpsURLConnection;
import other.Direction;
import weatherdata.*;

public class test {

    public static void main(String[] args) {
        HTTPSolarRequest req = new HTTPSolarRequest(57.668, 11.998, 45, 5, Direction.SOUTH);
        SolarRadiationRetreiver srr = new SolarRadiationRetreiver(req);
    }
}