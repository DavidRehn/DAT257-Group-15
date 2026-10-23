package location;

import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class ElectricityZoneFinder {

    private static final String API_URL =
            "https://rattlatt.se/api/v1/elomrade-uppslag";

    private final HttpClient httpClient;

    public ElectricityZoneFinder() {
        httpClient = HttpClient.newHttpClient();
    }

    public String findZone(String postcode) {

        if (postcode == null || postcode.isBlank()) {
            return null;
        }

        try {

            String cleanPostcode =
                    postcode.replace(" ", "");

            String requestBody =
                    "{\"postnummer\":\""
                            + cleanPostcode
                            + "\"}";

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(API_URL))
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(
                                            requestBody
                                    )
                            )
                            .build();

            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (response.statusCode() != 200) {
                return null;
            }

            try (
                    JsonReader reader =
                            Json.createReader(
                                    new StringReader(
                                            response.body()
                                    )
                            )
            ) {

                JsonObject result =
                        reader.readObject();

                String status =
                        result.getString(
                                "status",
                                ""
                        );

                if (!status.equals("ok")) {
                    return null;
                }

                return result.getString(
                        "priceArea",
                        null
                );
            }

        } catch (Exception e) {

            System.err.println(
                    "Could not find electricity zone: "
                            + e.getMessage()
            );

            return null;
        }
    }
}