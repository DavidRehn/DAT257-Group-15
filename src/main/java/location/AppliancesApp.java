
package location;

import java.time.LocalDateTime;
import java.util.Map;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import location.builder.ButtonBuilder;
import location.builder.CheckBoxBuilder;
import location.builder.LabelBuilder;
import location.builder.LabelDirector;
import location.builder.Row;
import location.builder.TextInputBuilder;
import location.builder.VBoxBuilder;

import other.AppInterface;
import other.ApplianceData;
import other.Model;
import other.SubAppInterface;
import other.TimestampData;

import i18n.LanguageManager;
import ui.PageNavigator;

public class AppliancesApp extends Application implements SubAppInterface {

    private Model appData;
    private AppInterface root;

    private ApplianceData appliances = new ApplianceData();
    private LabelDirector labelDirector;

    @Override
    public void linkAppData(Model m) {this.appData = m;
    }

    @Override
    public void linkRoot(AppInterface root) {this.root = root;
    }

    @Override
    public void start(Stage stage) {
        // Load saved appliances
        ApplianceData savedData = (ApplianceData) appData.Get("ApplianceData");
        if (savedData != null) {appliances = savedData;}
        labelDirector = new LabelDirector();

        // PAGE TITLE
        Label title = createLabel(LanguageManager.get("appliances")
        );

        title.getStyleClass().addAll("text", "home-welcome", "app-page-title");

        // CURRENT SOLAR PRODUCTION
        TimestampData prodData = (TimestampData) appData.Get("TimestampData");
        double prodVal = 0;

        if (prodData != null) {LocalDateTime now = LocalDateTime.now();
            String currentHour = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), 0).toString();
            Double value = prodData.GetTimestamps().get(currentHour);
            if (value != null) {prodVal = value;
            }
        }

        Label currentProduction = createLabel(LanguageManager.get("appliance.production") + ": " + (int) prodVal + " W");

        currentProduction.getStyleClass().addAll("text", "app-stat-value");

        // CURRENT ELECTRICITY USAGE
        Label currentUsage = createLabel(LanguageManager.get("appliance.usage") + ": 0 W");

        currentUsage.getStyleClass().addAll("text", "app-stat-value");

        // SUMMARY CARDS
        VBox productionStat = new VBox(10, currentProduction);
        VBox consumptionStat = new VBox(10, currentUsage);
        productionStat.getStyleClass().add("app-stat-card");
        consumptionStat.getStyleClass().add("app-stat-card");

        HBox.setHgrow(productionStat, Priority.ALWAYS);
        HBox.setHgrow(consumptionStat, Priority.ALWAYS);
        productionStat.setMaxWidth(Double.MAX_VALUE);
        consumptionStat.setMaxWidth(Double.MAX_VALUE);
        HBox infoRow = new HBox(18, productionStat, consumptionStat);

        infoRow.setAlignment(Pos.CENTER_LEFT);
        infoRow.setMaxWidth(Double.MAX_VALUE);

        // APPLIANCE NAME FIELD
        TextField nameField = createTextField(LanguageManager.get("appliance.name"));

        nameField.setPrefWidth(320);
        nameField.setMinWidth(180);
        nameField.setMaxWidth(420);

        HBox.setHgrow(nameField, Priority.ALWAYS);

        nameField.getStyleClass().add("app-input");

        // APPLIANCE WATTAGE FIELD
        TextField wattageField = createTextField(LanguageManager.get("appliance.wattage"));

        wattageField.setPrefWidth(155);

        wattageField.getStyleClass().add("app-input");

        // ADD APPLIANCE BUTTON
        ButtonBuilder addButtonBuilder = new ButtonBuilder();
        labelDirector.constructName(addButtonBuilder, new String[]{LanguageManager.get("appliance.add")
        }
        );

        Button addButton = addButtonBuilder.getButton();addButton.getStyleClass().add("app-primary-button");
        addButton.setPrefWidth(115);

        // INPUT ROW
        Row inputRowBuilder = new Row(nameField, wattageField, addButton);
        HBox inputRow = inputRowBuilder.getRow();
        inputRow.setAlignment(Pos.CENTER_LEFT);
        inputRow.setMaxWidth(Double.MAX_VALUE);

        // TOTAL ELECTRICITY USAGE
        Label totalLabel = createLabel(LanguageManager.get("appliance.total") + ": 0 W");

        totalLabel.getStyleClass().addAll("text", "app-total-label");
        // ERROR MESSAGE
        Label errorMessage = createLabel("");
        errorMessage.getStyleClass().addAll("text", "app-message");

        // APPLIANCE LIST
        VBoxBuilder applianceListBuilder = new VBoxBuilder();
        applianceListBuilder.setSpacing(10);
        applianceListBuilder.setAlignment(Pos.TOP_LEFT);

        VBox applianceList = applianceListBuilder.getVBox();
        applianceList.setMaxWidth(Double.MAX_VALUE);

        // TABLE HEADERS
        Label nameHeader = createLabel(LanguageManager.get("appliance.header.name"));
        nameHeader.getStyleClass().add("text");
        nameHeader.setPrefWidth(210);

        Label wattageHeader = createLabel(LanguageManager.get("appliance.wattage"));

        wattageHeader.getStyleClass().add("text");
        wattageHeader.setPrefWidth(135);

        Label inUseHeader = createLabel(LanguageManager.get("appliance.inuse"));
        inUseHeader.getStyleClass().add("text");
        inUseHeader.setPrefWidth(135);
        Label removeHeader = createLabel(LanguageManager.get("appliance.remove"));

        removeHeader.getStyleClass().add("text");
        removeHeader.setPrefWidth(105);

        // TABLE HEADER ROW
        Row headerRowBuilder = new Row(nameHeader, wattageHeader, inUseHeader, removeHeader);
        HBox headerRow = headerRowBuilder.getRow();
        headerRow.setAlignment(Pos.CENTER_LEFT);
        headerRow.setPadding(new Insets(12, 16, 12, 16));
        headerRow.getStyleClass().add("app-table-header");
        headerRow.setMaxWidth(Double.MAX_VALUE);
        // DISPLAY SAVED APPLIANCES
        for (Map.Entry<String, Double> entry : appliances.GetAppliances().entrySet()) {

            String savedName = entry.getKey();
            double savedWattage = entry.getValue();

            // APPLIANCE NAME
            Label nameLabel = createLabel(savedName);
            nameLabel.getStyleClass().add("text");
            nameLabel.setPrefWidth(210);
            // APPLIANCE WATTAGE
            Label wattageLabel = createLabel(savedWattage + " W");

            wattageLabel.getStyleClass().add("text");
            wattageLabel.setPrefWidth(135);

            // IN USE CHECKBOX
            CheckBoxBuilder inUseCheckBoxBuilder = new CheckBoxBuilder();
            inUseCheckBoxBuilder.setLabelName(LanguageManager.get("appliance.inuse"));

            CheckBox inUseCheckBox = inUseCheckBoxBuilder.getCheckBox();
            inUseCheckBox.setPrefWidth(135);
            inUseCheckBox.setSelected(appliances.IsInUse(savedName));

            // REMOVE BUTTON
            ButtonBuilder removeButtonBuilder = new ButtonBuilder();
            labelDirector.constructName(removeButtonBuilder, new String[]{LanguageManager.get("appliance.remove")});
            Button removeButton = removeButtonBuilder.getButton();
            removeButton.getStyleClass().add("app-delete-button");

            // APPLIANCE ROW
            Row applianceRowBuilder = new Row(nameLabel, wattageLabel, inUseCheckBox, removeButton);
            HBox applianceRow = applianceRowBuilder.getRow();
            applianceRow.setPadding(new Insets(12, 16, 12, 16));

            applianceRow.setAlignment(Pos.CENTER_LEFT);
            applianceRow.getStyleClass().add("app-table-row");
            applianceRow.setMaxWidth(Double.MAX_VALUE);
            applianceRow.setUserData(savedWattage);

            applianceList.getChildren().add(applianceRow);

            // UPDATE IN USE STATUS
            inUseCheckBox.setOnAction(e -> {appliances.SetInUse(savedName, inUseCheckBox.isSelected());
                appData.Store("ApplianceData", appliances);
                appData.SaveToDisc();
                updateTotal(applianceList, totalLabel, currentUsage);
            });

            // REMOVE SAVED APPLIANCE
            removeButtonBuilder.setEvent(() -> e -> {appliances.Remove(savedName, savedWattage);
                appData.Store("ApplianceData", appliances);
                appData.SaveToDisc();
                applianceList.getChildren().remove(applianceRow);
                updateTotal(applianceList, totalLabel, currentUsage);
            });
        }

        // UPDATE INITIAL USAGE
        updateTotal(applianceList, totalLabel, currentUsage);

        // ADD NEW APPLIANCE EVENT
        addButtonBuilder.setEvent(() -> event -> {String name = nameField.getText().trim();
            try {double wattage = Double.parseDouble(wattageField.getText().trim());

                // VALIDATE NAME
                if (name.isBlank() || appliances.hasKey(name)) {errorMessage.setText(
                            LanguageManager.get("appliance.error.name"));
                    return;}

                // VALIDATE WATTAGE
                if (!Double.isFinite(wattage) || wattage <= 0) {errorMessage.setText(LanguageManager.get("appliance.error.wattage"));
                    return;
                }

                errorMessage.setText("");

                // NEW APPLIANCE NAME
                Label nameLabel = createLabel(name);
                nameLabel.getStyleClass().add("text");
                nameLabel.setPrefWidth(210);

                // NEW APPLIANCE WATTAGE
                Label wattageLabel = createLabel(wattage + " W");

                wattageLabel.getStyleClass().add("text");
                wattageLabel.setPrefWidth(135);

                // STORE APPLIANCE
                appliances.Put(name, wattage);
                appData.Store("ApplianceData", appliances);
                appData.SaveToDisc();
                // CREATE CHECKBOX
                CheckBoxBuilder inUseCheckBoxBuilder = new CheckBoxBuilder();

                inUseCheckBoxBuilder.setLabelName(LanguageManager.get("appliance.inuse"));
                CheckBox inUseCheckBox = inUseCheckBoxBuilder.getCheckBox();

                inUseCheckBox.setPrefWidth(135);

                // CREATE REMOVE BUTTON
                ButtonBuilder removeButtonBuilder = new ButtonBuilder();
                labelDirector.constructName(removeButtonBuilder, new String[]{LanguageManager.get("appliance.remove")}
                );

                Button removeButton = removeButtonBuilder.getButton();
                removeButton.getStyleClass().add("app-delete-button");

                // CREATE NEW APPLIANCE ROW
                Row applianceRowBuilder = new Row(nameLabel, wattageLabel, inUseCheckBox, removeButton);
                HBox applianceRow = applianceRowBuilder.getRow();
                applianceRow.setPadding(new Insets(12, 16, 12, 16));
                applianceRow.setAlignment(Pos.CENTER_LEFT);
                applianceRow.getStyleClass().add("app-table-row");

                applianceRow.setMaxWidth(Double.MAX_VALUE);
                applianceRow.setUserData(wattage);
                applianceList.getChildren().add(applianceRow);

                // CHECKBOX EVENT
                inUseCheckBox.setOnAction(e -> {appliances.SetInUse(name, inUseCheckBox.isSelected());
                    appData.Store("ApplianceData", appliances);
                    appData.SaveToDisc();
                    updateTotal(applianceList, totalLabel, currentUsage);
                });
                // REMOVE NEW APPLIANCE
                removeButtonBuilder.setEvent(() -> e -> {appliances.Remove(name, wattage);
                    appData.Store("ApplianceData", appliances);
                    appData.SaveToDisc();
                    applianceList.getChildren().remove(applianceRow);
                    updateTotal(applianceList, totalLabel, currentUsage);}
                );

                // CLEAR INPUTS
                nameField.clear();wattageField.clear();
            } catch (NumberFormatException e) {errorMessage.setText(LanguageManager.get("appliance.error.number"));}
        });

        // ADD APPLIANCE CARD
        Label addHeading = createLabel(LanguageManager.get("appliance.add"));
        addHeading.getStyleClass().add("app-section-heading");
        VBox addCard = new VBox(16, addHeading, inputRow, errorMessage);

        addCard.setPadding(new Insets(24));
        addCard.setMaxWidth(Double.MAX_VALUE);
        addCard.getStyleClass().add("app-card");
        // APPLIANCE LIST CARD
        Label listHeading = createLabel(LanguageManager.get("appliances"));
        listHeading.getStyleClass().add("app-section-heading");

        VBox listCard = new VBox(14, listHeading, headerRow, applianceList, totalLabel);

        listCard.setPadding(new Insets(24));
        listCard.setMaxWidth(Double.MAX_VALUE);
        listCard.getStyleClass().add("app-card");

        // MAIN PAGE LAYOUT
        VBox layout = new VBox(22, title, infoRow, addCard, listCard);
        layout.setPadding(new Insets(25, 32, 35, 32));
        layout.setAlignment(Pos.TOP_LEFT);
        layout.getStyleClass().addAll("layout", "home-content", "app-appliances-page");

        // SCROLLING
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("home-scroll");

        // NAVIGATION
        PageNavigator.show(stage, root, scrollPane, "appliances", null
        );
    }

    // CREATE LABEL USING BUILDER


    private Label createLabel(String text) {LabelBuilder builder = new LabelBuilder();
        labelDirector.constructName(builder, new String[]{text});
        return builder.getLabel();
    }

    // CREATE TEXT FIELD USING BUILDER

    private TextField createTextField(String prompt) {TextInputBuilder builder = new TextInputBuilder();
        // Create field before setting placeholder
        labelDirector.constructName(builder, new String[]{""});
       builder.setPromptText(prompt);
        return builder.getTextField();
    }


    // CALCULATE TOTAL ELECTRICITY USAGE


    private void updateTotal(VBox applianceList, Label totalLabel, Label currentUsage) {
        double total = 0;
        for (Node node : applianceList.getChildren()) {
            HBox row = (HBox) node;
            CheckBox checkBox = (CheckBox) row.getChildren().get(2);
            double wattage = (double) row.getUserData();
            if (checkBox.isSelected()) {total += wattage;
            }
        }

        totalLabel.setText(LanguageManager.get("appliance.total") + ": " + total + " W");
        currentUsage.setText(LanguageManager.get("appliance.usage") + ": " + total + " W");
    }
}
