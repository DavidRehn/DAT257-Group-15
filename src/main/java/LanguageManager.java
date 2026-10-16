
package i18n;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class LanguageManager {

    // Save the selected language
    private static final Preferences preferences =
            Preferences.userNodeForPackage(LanguageManager.class);

    // Default language is English
    private static String language =
            preferences.get("language", "en");

    // Load the translations
    private static ResourceBundle messages =
            ResourceBundle.getBundle(
                    "lang.messages",
                    Locale.forLanguageTag(language)
            );

    // Get translated text
    public static String get(String key) {
        return messages.getString(key);
    }

    // Get current language
    public static String getLanguage() {
        return language;
    }

    // Change language
    public static void setLanguage(String newLanguage) {

        if (!newLanguage.equals("sv")) {
            newLanguage = "en";
        }

        language = newLanguage;

        // Remember the selection
        preferences.put("language", language);

        // Reload translations
        messages = ResourceBundle.getBundle(
                "lang.messages",
                Locale.forLanguageTag(language)
        );
    }
}
