package location;

import java.io.StringReader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import other.LocationData;

public class AddressGeocoder {

    private static final String API_URL =
            "https://nominatim.openstreetmap.org/search";

    private final HttpClient httpClient;

    // Avoid sending the same address repeatedly
    private final Map<String, LocationData> cache = new HashMap<>();
    private final Map<String, String> postcodeCache = new HashMap<>();
    // Nominatim allows maximum 1 request per second
    private long lastRequestTime = 0;

    public AddressGeocoder() {
        httpClient = HttpClient.newHttpClient();
    }

    public synchronized LocationData findLocation(String address) {

        if (address == null || address.isBlank()) {
            return null;
        }

        String cleanAddress = address.trim();

        // Return already searched address
        if (cache.containsKey(cleanAddress)) {
            return cache.get(cleanAddress);
        }

        try {

            // Respect Nominatim rate limit
            long timeSinceLastRequest =
                    System.currentTimeMillis() - lastRequestTime;

            if (timeSinceLastRequest < 1000) {
                Thread.sleep(1000 - timeSinceLastRequest);
            }

            String encodedAddress = URLEncoder.encode(
                    cleanAddress,
                    StandardCharsets.UTF_8
            );
            String requestUrl =
                    API_URL
                            + "?q=" + encodedAddress
                            + "&format=jsonv2"
                            + "&addressdetails=1"
                            + "&limit=1"
                            + "&countrycodes=se";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .header(
                            "User-Agent",
                            "DAT257-SolarApp/1.0 "
                                    + "(https://github.com/DavidRehn/DAT257-Group-15)"
                    )
                    .GET()
                    .build();

            lastRequestTime = System.currentTimeMillis();

            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (response.statusCode() != 200) {
                return null;
            }

            try (JsonReader reader = Json.createReader(
                    new StringReader(response.body()))) {

                JsonArray results = reader.readArray();

                if (results.isEmpty()) {
                    return null;
                }

                JsonObject firstResult =
                        results.getJsonObject(0);

                double latitude = Double.parseDouble(firstResult.getString("lat"));

                double longitude = Double.parseDouble(firstResult.getString("lon"));
                JsonObject addressDetails =
                        firstResult.getJsonObject("address");

                String postcode = null;

                if (addressDetails != null) {
                    postcode = addressDetails.getString(
                            "postcode",
                            null
                    );
                }
                LocationData location = new LocationData(longitude, latitude);
                cache.put(cleanAddress, location);
                if (postcode != null) {
                    postcodeCache.put(
                            cleanAddress,
                            postcode
                    );
                }
                return location;
            }

        } catch (Exception e) {
            System.err.println("Could not find address: " + e.getMessage());

            return null;
        }
    }
    public synchronized String getPostcode(
            String address
    ) {

        if (address == null) {
            return null;
        }

        return postcodeCache.get(
                address.trim()
        );
    }
}