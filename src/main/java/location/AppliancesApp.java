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

        HBox backRow = new HBox(backButton);
        backRow.setAlignment(Pos.CENTER_LEFT);
        backRow.setMaxWidth(Double.MAX_VALUE);


// أضف هذا الجزء هنا
        LabelBuilder currentProductionBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(
                currentProductionBuilder,
                "Current production: 100 W"
        );
        Label currentProduction = currentProductionBuilder.getLabel();

        LabelBuilder currentUsageBuilder = new LabelBuilder();
        labelDirector.constructNameOfLabel(
                currentUsageBuilder,
                "Current usage: 0 W"
        );
        Label currentUsage = currentUsageBuilder.getLabel();

        HBox infoRow = new HBox(
                50,
                currentProduction,
                currentUsage
        );

        infoRow.setAlignment(Pos.CENTER);



        // Appliance name
     /*   TextField nameField = new TextField();
        nameField.setPromptText("Appliance name");
        nameField.setPrefWidth(200);

      */
        TextInputBuilder nameFieldBuilder = new TextInputBuilder();
        nameFieldBuilder.setPromptText("Appliance name");
        TextField nameField = nameFieldBuilder.getTextField();
        nameField.setPrefWidth(200);

// Appliance wattage
     /*   TextField wattageField = new TextField();
        wattageField.setPromptText("Wattage");
        wattageField.setPrefWidth(120);

      */
        TextInputBuilder wattageFieldBuilder = new TextInputBuilder();
        wattageFieldBuilder.setPromptText("Wattage");
        TextField wattageField = wattageFieldBuilder.getTextField();
        wattageField.setPrefWidth(120);
// Add button
      /*  Button addButton = new Button("Add");

       */
        ButtonBuilder addButtonBuilder = new ButtonBuilder();
        addButtonBuilder.setLabelName("Add");

        Button addButton = addButtonBuilder.getButton();
// Put inputs next to each other
        Row inputRowBuilder =
                new Row(nameField, wattageField, addButton);

        HBox inputRow = inputRowBuilder.getRow();

// Total electricity usage
        Label totalLabel = new Label("Total usage: 0 W");
        Label errorMessage = new Label("");
        // List where appliances will be shown
        VBox applianceList = new VBox(10);
        applianceList.setAlignment(Pos.CENTER);
        Label nameHeader = new Label("Name");
        nameHeader.setPrefWidth(120);

        Label wattageHeader = new Label("Wattage");
        wattageHeader.setPrefWidth(100);

        Label inUseHeader = new Label("In use");
        inUseHeader.setPrefWidth(100);

        Label removeHeader = new Label("Remove");
        removeHeader.setPrefWidth(80);

        HBox headerRow = new HBox(
                20,
                nameHeader,
                wattageHeader,
                inUseHeader,
                removeHeader
        );

        headerRow.setAlignment(Pos.CENTER);
// Show already saved appliances
        for (java.util.Map.Entry<String, Double> entry
                : appliances.GetAppliances().entrySet()) {

            String savedName = entry.getKey();
            double savedWattage = entry.getValue();

            Label nameLabel = new Label(savedName);
            Label wattageLabel = new Label(savedWattage + " W");

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

            applianceRow.setUserData(savedWattage);

            applianceList.getChildren().add(applianceRow);

            inUseCheckBox.setOnAction(e -> {
                updateTotal(applianceList, totalLabel, currentUsage);
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

                Label nameLabel = new Label(name);
                Label wattageLabel = new Label(wattage + " W");
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
                    updateTotal(applianceList, totalLabel, currentUsage);
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

        

        VBox layout = new VBox(
                20,
                backRow,
                infoRow,
                title,
                inputRow,
                errorMessage,
                headerRow,
                applianceList,
                totalLabel
        );
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