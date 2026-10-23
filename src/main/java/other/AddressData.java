package other;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class AddressData implements SavableData {

    private String address;

    public AddressData(String address) {
        this.address = address;
    }

    public AddressData(JsonObject obj) {
        this.address = obj.getString("address");
    }

    public String Address() {
        return address;
    }

    @Override
    public JsonObject ToJsonObj(String key) {

        JsonObjectBuilder builder =
                Json.createObjectBuilder();

        builder.add(
                "address",
                address
        );

        JsonObject obj =
                builder.build();

        builder =
                Json.createObjectBuilder();

        builder.add(
                key,
                obj
        );

        return builder.build();
    }
}