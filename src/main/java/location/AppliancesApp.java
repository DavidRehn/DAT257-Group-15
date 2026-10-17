package location;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import location.builder.LabelBuilder;
import location.builder.LabelDirector;
import location.builder.TextInputBuilder;
import location.builder.ButtonBuilder;
import location.builder.CheckBoxBuilder;
import location.builder.Row;

import other.AppInterface;
import other.ApplianceData;
import other.Model;
import other.SubAppInterface;
import java.time.LocalDateTime;

import CSS.CSSPath;
import other.TimestampData;
import location.builder.VBoxBuilder;
import language.LanguageManager;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import ui.PageNavigator;

public class AppliancesApp extends Application implements SubAppInterface{
    private Model appData;
    public void linkAppData(Model m){
        this.appData=m;
    }
    private AppInterface root;
    public void linkRoot(AppInterface root){
        this.root=root;
    }
    ApplianceData appliances=new ApplianceData();

    @Override
    public void start(Stage stage) {
        if(appData.Get("ApplianceData")!=null){
            appliances=(ApplianceData)appData.Get("ApplianceData");
        }

        LabelBuilder titleBuilder = new LabelBuilder();
        LabelDirector labelDirector = new LabelDirector();
        labelDirector.constructNameOfLabel(titleBuilder, LanguageManager.get("appliances"));
        Label title = titleBuilder.getLabel();
        title.getStyleClass().add("text");

        TimestampData prodData = (TimestampData) appData.Get("TimestampData");

        double prodVal = 0;
        if (prodData != null) {LocalDateTime now = LocalDateTime.now();
            String currentHour = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), 0).toString();
            Double value = prodData.GetTimestamps().get(currentHour);
            if (value != null) {prodVal = value;}
        }

        LabelBuilder currentProductionBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(currentProductionBuilder, LanguageManager.get("appliance.production") + ": " + (int) prodVal + " W");
        Label currentProduction = currentProductionBuilder.getLabel();
        currentProduction.getStyleClass().add("text");
        LabelBuilder currentUsageBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(currentUsageBuilder, LanguageManager.get("appliance.usage") + ": 0 W");
        Label currentUsage = currentUsageBuilder.getLabel();
        currentUsage.getStyleClass().add("text");

        Row infoRowBuilder = new Row(currentProduction, currentUsage);

        // Two separate summary cards, aligned to the dashboard grid.
        currentProduction.getStyleClass().add("app-stat-value");
        currentUsage.getStyleClass().add("app-stat-value");
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

        // Appliance name
        TextInputBuilder nameFieldBuilder = new TextInputBuilder();
        nameFieldBuilder.setPromptText(LanguageManager.get("appliance.name"));
        TextField nameField = nameFieldBuilder.getTextField();
        nameField.setPrefWidth(320);
        nameField.setMinWidth(180);
        nameField.setMaxWidth(420);
        HBox.setHgrow(nameField, Priority.ALWAYS);
        nameField.getStyleClass().add("app-input");

// Appliance wattage
        TextInputBuilder wattageFieldBuilder = new TextInputBuilder();
        wattageFieldBuilder.setPromptText(LanguageManager.get("appliance.wattage"));
        TextField wattageField = wattageFieldBuilder.getTextField();
        wattageField.setPrefWidth(155);
        wattageField.getStyleClass().add("app-input");
// Add button
        ButtonBuilder addButtonBuilder = new ButtonBuilder();
        addButtonBuilder.setLabelName(LanguageManager.get("appliance.add"));
        Button addButton = addButtonBuilder.getButton();
        addButton.getStyleClass().add("app-primary-button");
        addButton.setPrefWidth(115);
// Put inputs next to each other
        Row inputRowBuilder = new Row(nameField, wattageField, addButton);
        HBox inputRow = inputRowBuilder.getRow();
        inputRow.setAlignment(Pos.CENTER_LEFT);
        inputRow.setMaxWidth(Double.MAX_VALUE);
// Total electricity usage
        LabelBuilder totalLabelBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(totalLabelBuilder, LanguageManager.get("appliance.total") + ": 0 W");
        Label totalLabel = totalLabelBuilder.getLabel();
        totalLabel.getStyleClass().add("text");
        LabelBuilder errorMessageBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(errorMessageBuilder, "");

        Label errorMessage = errorMessageBuilder.getLabel();
        errorMessage.getStyleClass().add("text");
        // List where appliances will be shown
        VBoxBuilder applianceListBuilder = new VBoxBuilder();
        applianceListBuilder.setSpacing(10);
        applianceListBuilder.setAlignment(Pos.CENTER);

        VBox applianceList = applianceListBuilder.getVBox();
        LabelBuilder nameHeaderBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(nameHeaderBuilder, LanguageManager.get("appliance.header.name"));
        Label nameHeader = nameHeaderBuilder.getLabel();
        nameHeader.getStyleClass().add("text");
        nameHeader.setPrefWidth(210);


        LabelBuilder wattageHeaderBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(wattageHeaderBuilder, LanguageManager.get("appliance.wattage"));
        Label wattageHeader = wattageHeaderBuilder.getLabel();
        wattageHeader.getStyleClass().add("text");
        wattageHeader.setPrefWidth(135);


        LabelBuilder inUseHeaderBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(inUseHeaderBuilder, LanguageManager.get("appliance.inuse"));
        Label inUseHeader = inUseHeaderBuilder.getLabel();
        inUseHeader.getStyleClass().add("text");
        inUseHeader.setPrefWidth(135);

        LabelBuilder removeHeaderBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(removeHeaderBuilder, LanguageManager.get("appliance.remove"));
        Label removeHeader = removeHeaderBuilder.getLabel();
        removeHeader.getStyleClass().add("text");
        removeHeader.setPrefWidth(105);

        Row headerRowBuilder = new Row(nameHeader, wattageHeader, inUseHeader, removeHeader);

        HBox headerRow = headerRowBuilder.getRow();
        headerRow.setAlignment(Pos.CENTER_LEFT);
        headerRow.setPadding(new Insets(12, 16, 12, 16));
        headerRow.getStyleClass().add("app-table-header");
        headerRow.setMaxWidth(Double.MAX_VALUE);
// Show already saved appliances
        for (java.util.Map.Entry<String, Double> entry : appliances.GetAppliances().entrySet()) {
            String savedName = entry.getKey();
            double savedWattage = entry.getValue();

            LabelBuilder nameLabelBuilder = new LabelBuilder();
            labelDirector.constructNameOfLabel(nameLabelBuilder, savedName);
            Label nameLabel = nameLabelBuilder.getLabel();
            nameLabel.getStyleClass().add("text");
            nameLabel.setPrefWidth(210);

            LabelBuilder wattageLabelBuilder = new LabelBuilder();
            labelDirector.constructNameOfLabel(wattageLabelBuilder, savedWattage + " W");
            Label wattageLabel = wattageLabelBuilder.getLabel();
            wattageLabel.getStyleClass().add("text");
            wattageLabel.setPrefWidth(135);

            CheckBoxBuilder inUseCheckBoxBuilder = new CheckBoxBuilder();
            inUseCheckBoxBuilder.setLabelName( LanguageManager.get("appliance.inuse"));

            CheckBox inUseCheckBox = inUseCheckBoxBuilder.getCheckBox();
            inUseCheckBox.setPrefWidth(135);
            inUseCheckBox.setSelected(appliances.IsInUse(savedName));

            ButtonBuilder removeButtonBuilder = new ButtonBuilder();
            removeButtonBuilder.setLabelName(  LanguageManager.get("appliance.remove"));

            Button removeButton = removeButtonBuilder.getButton();
            removeButton.getStyleClass().add("app-delete-button");

            Row applianceRowBuilder = new Row(nameLabel, wattageLabel, inUseCheckBox, removeButton);

            HBox applianceRow = applianceRowBuilder.getRow();
            applianceRow.setPadding(new Insets(12, 16, 12, 16));
            applianceRow.setAlignment(Pos.CENTER_LEFT);
            applianceRow.getStyleClass().add("app-table-row");
            applianceRow.setMaxWidth(Double.MAX_VALUE);
            applianceRow.setUserData(savedWattage);
            applianceList.getChildren().add(applianceRow);

            inUseCheckBox.setOnAction(e -> {appliances.SetInUse(savedName, inUseCheckBox.isSelected());
                appData.Store("ApplianceData", appliances);
                appData.SaveToDisc();
                updateTotal(applianceList, totalLabel, currentUsage);
            });

            removeButtonBuilder.setEvent(e -> {appliances.Remove(savedName, savedWattage);
                appData.Store("ApplianceData", appliances);
                appData.SaveToDisc();
                applianceList.getChildren().remove(applianceRow);
                updateTotal(applianceList, totalLabel, currentUsage);
            });
        }
        // Update usage when loading saved appliances
        updateTotal(applianceList, totalLabel, currentUsage);
        addButtonBuilder.setEvent(event -> {String name = nameField.getText().trim();
            try {double wattage = Double.parseDouble(wattageField.getText());
                if (name.isBlank() || appliances.hasKey(name)) {
                    errorMessage.setText(LanguageManager.get("appliance.error.name"));
                    return;}
                if (wattage <= 0) {errorMessage.setText(LanguageManager.get("appliance.error.wattage"));
                    return;}
                errorMessage.setText("");

                LabelBuilder nameLabelBuilder = new LabelBuilder();
                labelDirector.constructNameOfLabel(nameLabelBuilder, name);
                Label nameLabel = nameLabelBuilder.getLabel();
                nameLabel.getStyleClass().add("text");

                LabelBuilder wattageLabelBuilder = new LabelBuilder();
                labelDirector.constructNameOfLabel(wattageLabelBuilder, wattage + " W");
                Label wattageLabel = wattageLabelBuilder.getLabel();
                wattageLabel.getStyleClass().add("text");
                appliances.Put(name,wattage);
                appData.Store("ApplianceData",appliances);
                appData.SaveToDisc();

                nameLabel.setPrefWidth(210);
                wattageLabel.setPrefWidth(135);
                CheckBoxBuilder inUseCheckBoxBuilder = new CheckBoxBuilder();
                inUseCheckBoxBuilder.setLabelName(  LanguageManager.get("appliance.inuse"));

                CheckBox inUseCheckBox = inUseCheckBoxBuilder.getCheckBox();
                inUseCheckBox.setPrefWidth(135);

                ButtonBuilder removeButtonBuilder = new ButtonBuilder();
                removeButtonBuilder.setLabelName(LanguageManager.get("appliance.remove"));

                Button removeButton = removeButtonBuilder.getButton();
                removeButton.getStyleClass().add("app-delete-button");

                Row applianceRowBuilder = new Row(nameLabel, wattageLabel, inUseCheckBox, removeButton);

                HBox applianceRow = applianceRowBuilder.getRow();
                applianceRow.setPadding(new Insets(12, 16, 12, 16));
                applianceRow.setAlignment(Pos.CENTER_LEFT);
                applianceRow.getStyleClass().add("app-table-row");
                applianceRow.setMaxWidth(Double.MAX_VALUE);

                // Save wattage inside the row
                applianceRow.setUserData(wattage);
                applianceList.getChildren().add(applianceRow);

                // Update total when checkbox changes
                inUseCheckBox.setOnAction(e -> {appliances.SetInUse(nameLabel.getText(), inUseCheckBox.isSelected());

                    appData.Store("ApplianceData", appliances);
                    appData.SaveToDisc();
                    updateTotal(applianceList, totalLabel, currentUsage);
                });
                // Remove appliance
                removeButtonBuilder.setEvent(e -> {
                    appliances.Remove(nameLabel.getText(), wattage);
                    appData.SaveToDisc();
                    applianceList.getChildren().remove(applianceRow);
                    updateTotal(applianceList, totalLabel, currentUsage);});
                // Clear input fields
                nameField.clear();wattageField.clear();
            } catch (NumberFormatException e) {errorMessage.setText(LanguageManager.get("appliance.error.number"));
            }
        });



        // Refined appliances dashboard: clear hierarchy and compact content.
        title.getStyleClass().add("home-welcome");
        Label addHeading = new Label(LanguageManager.get("appliance.add"));
        addHeading.getStyleClass().add("app-section-heading");
        VBox addCard = new VBox(16, addHeading, inputRow, errorMessage);
        addCard.setPadding(new Insets(24));
        addCard.setMaxWidth(Double.MAX_VALUE);
        addCard.getStyleClass().add("app-card");

        Label listHeading = new Label(LanguageManager.get("appliances"));
        listHeading.getStyleClass().add("app-section-heading");
        totalLabel.getStyleClass().add("app-total-label");
        errorMessage.getStyleClass().add("app-message");
        applianceList.setAlignment(Pos.TOP_LEFT);
        applianceList.setMaxWidth(Double.MAX_VALUE);
        VBox listCard = new VBox(14, listHeading, headerRow, applianceList, totalLabel);
        listCard.setPadding(new Insets(24));
        listCard.setMaxWidth(Double.MAX_VALUE);
        listCard.getStyleClass().add("app-card");

        VBox layout = new VBox(22, title, infoRow, addCard, listCard);
        layout.setPadding(new Insets(25, 32, 35, 32));
        layout.setAlignment(Pos.TOP_LEFT);
        layout.getStyleClass().addAll("layout", "home-content", "app-appliances-page");

        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("home-scroll");
        PageNavigator.show(stage, root, scrollPane, "appliances", null);
    }

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
        currentUsage.setText(LanguageManager.get("appliance.usage") + ": " + total + " W"
        );
    }
}
