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
import other.TimestampData;
import location.builder.VBoxBuilder;

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

        labelDirector.constructNameOfLabel(titleBuilder, "Appliances");

        Label title = titleBuilder.getLabel();

        ButtonBuilder backButtonBuilder = new ButtonBuilder();
        backButtonBuilder.setLabelName("← Back to main page");

        Button backButton = backButtonBuilder.getButton();

        backButtonBuilder.setEvent(event -> {
            System.out.println(root);
            root.viewStart(stage);
        });

        Row backRowBuilder = new Row(backButton);

        HBox backRow = backRowBuilder.getRow();
        backRow.setMaxWidth(Double.MAX_VALUE);


// أضف هذا الجزء هنا
        TimestampData prodData =
                (TimestampData) appData.Get("TimestampData");

        double prodVal = 0;

        if (prodData != null) {

            LocalDateTime now = LocalDateTime.now();

            String currentHour =
                    LocalDateTime.of(
                            now.getYear(),
                            now.getMonth(),
                            now.getDayOfMonth(),
                            now.getHour(),
                            0
                    ).toString();

            Double value =
                    prodData.GetTimestamps().get(currentHour);

            if (value != null) {
                prodVal = value;
            }
        }

        LabelBuilder currentProductionBuilder = new LabelBuilder();

        labelDirector.constructNameOfLabel(
                currentProductionBuilder,
                "Current production: " + (int) prodVal + " W"
        );

        Label currentProduction = currentProductionBuilder.getLabel();
        LabelBuilder currentUsageBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(
                currentUsageBuilder,
                "Current usage: 0 W"
        );
        Label currentUsage = currentUsageBuilder.getLabel();

        Row infoRowBuilder = new Row(
                currentProduction,
                currentUsage
        );

        HBox infoRow = infoRowBuilder.getRow();



        // Appliance name

        TextInputBuilder nameFieldBuilder = new TextInputBuilder();
        nameFieldBuilder.setPromptText("Appliance name");
        TextField nameField = nameFieldBuilder.getTextField();
        nameField.setPrefWidth(200);

// Appliance wattage


        TextInputBuilder wattageFieldBuilder = new TextInputBuilder();
        wattageFieldBuilder.setPromptText("Wattage");
        TextField wattageField = wattageFieldBuilder.getTextField();
        wattageField.setPrefWidth(120);
// Add button



        ButtonBuilder addButtonBuilder = new ButtonBuilder();
        addButtonBuilder.setLabelName("Add");

        Button addButton = addButtonBuilder.getButton();
// Put inputs next to each other
        Row inputRowBuilder =
                new Row(nameField, wattageField, addButton);

        HBox inputRow = inputRowBuilder.getRow();

// Total electricity usage
        LabelBuilder totalLabelBuilder = new LabelBuilder();

        labelDirector.constructNameOfLabel(
                totalLabelBuilder,
                "Total usage: 0 W"
        );

        Label totalLabel = totalLabelBuilder.getLabel();

        LabelBuilder errorMessageBuilder = new LabelBuilder();

        labelDirector.constructNameOfLabel(
                errorMessageBuilder,
                ""
        );

        Label errorMessage = errorMessageBuilder.getLabel();
        // List where appliances will be shown
        VBoxBuilder applianceListBuilder = new VBoxBuilder();
        applianceListBuilder.setSpacing(10);
        applianceListBuilder.setAlignment(Pos.CENTER);

        VBox applianceList = applianceListBuilder.getVBox();

        LabelBuilder nameHeaderBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(
                nameHeaderBuilder,
                "Name"
        );
        Label nameHeader = nameHeaderBuilder.getLabel();
        nameHeader.setPrefWidth(120);


        LabelBuilder wattageHeaderBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(
                wattageHeaderBuilder,
                "Wattage"
        );
        Label wattageHeader = wattageHeaderBuilder.getLabel();
        wattageHeader.setPrefWidth(100);


        LabelBuilder inUseHeaderBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(
                inUseHeaderBuilder,
                "In use"
        );
        Label inUseHeader = inUseHeaderBuilder.getLabel();
        inUseHeader.setPrefWidth(100);


        LabelBuilder removeHeaderBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(
                removeHeaderBuilder,
                "Remove"
        );
        Label removeHeader = removeHeaderBuilder.getLabel();
        removeHeader.setPrefWidth(80);

        Row headerRowBuilder = new Row(
                nameHeader,
                wattageHeader,
                inUseHeader,
                removeHeader
        );

        HBox headerRow = headerRowBuilder.getRow();
// Show already saved appliances
        for (java.util.Map.Entry<String, Double> entry
                : appliances.GetAppliances().entrySet()) {

            String savedName = entry.getKey();
            double savedWattage = entry.getValue();

            LabelBuilder nameLabelBuilder = new LabelBuilder();
            labelDirector.constructNameOfLabel(
                    nameLabelBuilder,
                    savedName
            );
            Label nameLabel = nameLabelBuilder.getLabel();
            nameLabel.setPrefWidth(120);


            LabelBuilder wattageLabelBuilder = new LabelBuilder();
            labelDirector.constructNameOfLabel(
                    wattageLabelBuilder,
                    savedWattage + " W"
            );
            Label wattageLabel = wattageLabelBuilder.getLabel();
            wattageLabel.setPrefWidth(100);

            CheckBoxBuilder inUseCheckBoxBuilder = new CheckBoxBuilder();
            inUseCheckBoxBuilder.setLabelName("In use");

            CheckBox inUseCheckBox = inUseCheckBoxBuilder.getCheckBox();
            inUseCheckBox.setPrefWidth(100);

            inUseCheckBox.setSelected(
                    appliances.IsInUse(savedName)
            );

            ButtonBuilder removeButtonBuilder = new ButtonBuilder();
            removeButtonBuilder.setLabelName("Remove");

            Button removeButton = removeButtonBuilder.getButton();

            Row applianceRowBuilder = new Row(
                    nameLabel,
                    wattageLabel,
                    inUseCheckBox,
                    removeButton
            );

            HBox applianceRow = applianceRowBuilder.getRow();
            applianceRow.setPadding(new Insets(8));

            applianceRow.setStyle(
                    "-fx-border-color: #CCCCCC;" +
                            "-fx-border-width: 1;" +
                            "-fx-background-color: white;"
            );

            applianceRow.setUserData(savedWattage);

            applianceList.getChildren().add(applianceRow);

            inUseCheckBox.setOnAction(e -> {

                appliances.SetInUse(savedName, inUseCheckBox.isSelected()
                );

                appData.Store("ApplianceData", appliances);
                appData.SaveToDisc();

                updateTotal(applianceList, totalLabel, currentUsage
                );
            });

            removeButtonBuilder.setEvent(e -> {
                appliances.Remove(savedName, savedWattage);
                appData.Store("ApplianceData", appliances);
                appData.SaveToDisc();

                applianceList.getChildren().remove(applianceRow);
                updateTotal(applianceList, totalLabel, currentUsage);
            });
        }
        addButtonBuilder.setEvent(event -> {

            String name = nameField.getText().trim();

            try {
                double wattage = Double.parseDouble(wattageField.getText());

                if (name.isBlank() || appliances.hasKey(name)) {
                    errorMessage.setText("Please enter a unique appliance name");
                    return;
                }

                if (wattage <= 0) {
                    errorMessage.setText("Wattage must be greater than 0");
                    return;
                }

                errorMessage.setText("");

                LabelBuilder nameLabelBuilder = new LabelBuilder();
                labelDirector.constructNameOfLabel(
                        nameLabelBuilder,
                        name
                );
                Label nameLabel = nameLabelBuilder.getLabel();

                LabelBuilder wattageLabelBuilder = new LabelBuilder();
                labelDirector.constructNameOfLabel(
                        wattageLabelBuilder,
                        wattage + " W"
                );
                Label wattageLabel = wattageLabelBuilder.getLabel();
                appliances.Put(name,wattage);
                appData.Store("ApplianceData",appliances);
                appData.SaveToDisc();

                nameLabel.setPrefWidth(120);
                wattageLabel.setPrefWidth(100);
                CheckBoxBuilder inUseCheckBoxBuilder = new CheckBoxBuilder();
                inUseCheckBoxBuilder.setLabelName("In use");

                CheckBox inUseCheckBox = inUseCheckBoxBuilder.getCheckBox();
                inUseCheckBox.setPrefWidth(100);

                ButtonBuilder removeButtonBuilder = new ButtonBuilder();
                removeButtonBuilder.setLabelName("Remove");

                Button removeButton = removeButtonBuilder.getButton();

                Row applianceRowBuilder = new Row(
                        nameLabel,
                        wattageLabel,
                        inUseCheckBox,
                        removeButton
                );

                HBox applianceRow = applianceRowBuilder.getRow();
                applianceRow.setPadding(new Insets(8));

                applianceRow.setStyle(
                        "-fx-border-color: #CCCCCC;" +
                                "-fx-border-width: 1;" +
                                "-fx-background-color: white;"
                );
                headerRow.setPadding(new Insets(8));

                headerRow.setStyle(
                        "-fx-border-color: #CCCCCC;" +
                                "-fx-border-width: 1;" +
                                "-fx-background-color: #F5F5F5;"
                );
                applianceList.setMaxWidth(600);
                headerRow.setMaxWidth(600);

                // Save wattage inside the row
                applianceRow.setUserData(wattage);

                applianceList.getChildren().add(applianceRow);

                // Update total when checkbox changes
                inUseCheckBox.setOnAction(e -> {

                    appliances.SetInUse(
                            nameLabel.getText(),
                            inUseCheckBox.isSelected()
                    );

                    appData.Store("ApplianceData", appliances);
                    appData.SaveToDisc();

                    updateTotal(applianceList, totalLabel, currentUsage
                    );
                });

                // Remove appliance
                removeButtonBuilder.setEvent(e -> {
                    appliances.Remove(nameLabel.getText(), wattage);
                    appData.SaveToDisc();

                    applianceList.getChildren().remove(applianceRow);
                    updateTotal(applianceList, totalLabel, currentUsage);
                });

                // Clear input fields
                nameField.clear();
                wattageField.clear();

            } catch (NumberFormatException e) {
                errorMessage.setText("Please enter a valid wattage");
            }
        });



        VBoxBuilder layoutBuilder = new VBoxBuilder();

        layoutBuilder.setSpacing(20);

        layoutBuilder.addChildren(
                backRow,
                infoRow,
                title,
                inputRow,
                errorMessage,
                headerRow,
                applianceList,
                totalLabel
        );

        layoutBuilder.setAlignment(Pos.TOP_CENTER);

        VBox layout = layoutBuilder.getVBox();

        layout.setPadding(new Insets(20));
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(layout, 800, 550);

        stage.setTitle("Solar App");
        stage.setScene(scene);
        stage.show();
    }
    private void updateTotal(
            VBox applianceList,
            Label totalLabel,
            Label currentUsage
    ) {

        double total = 0;

        for (Node node : applianceList.getChildren()) {

            HBox row = (HBox) node;

            CheckBox checkBox =
                    (CheckBox) row.getChildren().get(2);

            double wattage =
                    (double) row.getUserData();

            if (checkBox.isSelected()) {
                total += wattage;
            }
        }

        totalLabel.setText("Total usage: " + total + " W");
        currentUsage.setText("Current usage: " + total + " W");
    }
}