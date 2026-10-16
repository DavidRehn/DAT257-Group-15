
package ui;

import CSS.CSSPath;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import java.util.prefs.Preferences;

public class ThemeManager {
    // Save user preferences
    private static final Preferences preferences = Preferences.userNodeForPackage(ThemeManager.class);
    // Default theme is Light Mode
    private static boolean darkMode = preferences.getBoolean("darkMode", false);

    // Apply the selected CSS to any page
    public static void apply(Scene scene) {String layoutPath = CSSPath.layoutPath;

        // Find the CSS folder
        int lastSlash = layoutPath.lastIndexOf('/');String cssFolder = layoutPath.substring(0, lastSlash + 1);
        // Select the correct theme
        String themeFile;

        if (darkMode) {themeFile = "Dark.css"; } else {themeFile = "Default.css";}
        // Load layout and selected theme
        scene.getStylesheets().setAll(layoutPath, cssFolder + themeFile);}

    // Create a reusable theme button
    public static Button createButton(Stage stage) {

        Button themeButton = new Button();themeButton.getStyleClass().add("theme-button");
        updateButton(themeButton);
        themeButton.setOnAction(event -> {
            // Switch theme
            darkMode = !darkMode;
            // Save selected theme
            preferences.putBoolean("darkMode", darkMode);
            // Apply without reloading the page
            if (stage.getScene() != null) {apply(stage.getScene());}
            updateButton(themeButton);
        });
        return themeButton;
    }

    // Update button icon
    private static void updateButton(Button button) {
        if (darkMode) {button.setText("☀");button.setTooltip(new Tooltip("Light mode / Ljust läge"));
        } else {button.setText("☾");button.setTooltip(new Tooltip("Dark mode / Mörkt läge"));
        }
    }
}
