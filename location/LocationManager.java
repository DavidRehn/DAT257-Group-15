
package location;

import java.util.prefs.Preferences;

public class LocationManager {

    // Save the selected city
    public void saveCity(String city) {

        Preferences settings =
                Preferences.userNodeForPackage(LocationManager.class);

        settings.put("city", city);
    }

    // Read the saved city
    public String getCity() {

        Preferences settings =
                Preferences.userNodeForPackage(LocationManager.class);

        return settings.get("city", "No city saved");
    }
}