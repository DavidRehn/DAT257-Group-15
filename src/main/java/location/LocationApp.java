
package location;
//import
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;

public  class LocationApp extends Application {

    @Override
    public void start(Stage stage) {

 // Create the title

        Label title = new Label("Location and Solar Panel Info");

        Label locationTitle = new Label("Location");

        Label solarPanelTitle = new Label("Solar panel details");
//Back to main page

        Button backButton = new Button("← Back to main page");

        backButton.setOnAction(event -> {
            stage.close();
        });
// Longitude
        TextField longitudeField = new TextField();
        longitudeField.setPromptText("Enter longitude");
        longitudeField.setMaxWidth(220);


// Check longitude
        Button checkButton = new Button("Check longitude");

        Label errorMessage = new Label("");

        checkButton.setOnAction(event -> {

            try {
                double longitude =
                        Double.parseDouble(longitudeField.getText());

                if (longitude >= -180 && longitude <= 180) {
                    errorMessage.setText("Valid longitude");
                } else {
                    errorMessage.setText("Longitude must be between -180 and 180");
                }

            } catch (NumberFormatException e) {
                errorMessage.setText("Please enter a valid number");
            }

        });


// Latitude
        TextField latitudeField = new TextField();
        latitudeField.setPromptText("Enter latitude");
        latitudeField.setMaxWidth(220);


// Check latitude
        Button latitudeButton = new Button("Check latitude");

        Label latitudeMessage = new Label("");

        latitudeButton.setOnAction(event -> {

            try {
                double latitude =
                        Double.parseDouble(latitudeField.getText());

                if (latitude >= -90 && latitude <= 90) {
                    latitudeMessage.setText("Valid latitude");
                } else {
                    latitudeMessage.setText(
                            "Latitude must be between -90 and 90"
                    );
                }

            } catch (NumberFormatException e) {
                latitudeMessage.setText(
                        "Please enter a valid number"
                );
            }

        });


// Solar panel area
        TextField areaField = new TextField();
        areaField.setPromptText("Area (m²)");
        areaField.setMaxWidth(220);


// Check solar panel area
        Button areaButton = new Button("Check area");

        Label areaMessage = new Label("");

        areaButton.setOnAction(event -> {

            try {
                double area =
                        Double.parseDouble(areaField.getText());

                if (area > 0 && Double.isFinite(area)) {
                    areaMessage.setText("Valid area");
                } else {
                    areaMessage.setText("Area must be greater than 0");
                }

            } catch (NumberFormatException e) {
                areaMessage.setText("Please enter a valid number");
            }

        });

// Solar panel tilt
        TextField tiltField = new TextField();
        tiltField.setPromptText("Tilt (degrees)");
        tiltField.setMaxWidth(220);


// Check solar panel tilt
        Button tiltButton = new Button("Check tilt");

        Label tiltMessage = new Label("");

        tiltButton.setOnAction(event -> {

            try {
                int tilt = Integer.parseInt(tiltField.getText());

                if (tilt >= 0 && tilt <= 90) {
                    tiltMessage.setText("Valid tilt");
                } else {
                    tiltMessage.setText(
                            "Tilt must be between 0 and 90"
                    );
                }

            } catch (NumberFormatException e) {
                tiltMessage.setText(
                        "Please enter a valid whole number"
                );
            }

        });

// Solar panel direction
        ComboBox<String> directionList = new ComboBox<>();

        directionList.getItems().addAll(
                "North",
                "South",
                "East",
                "West"
        );

        directionList.setPromptText("Choose direction");

        directionList.setPrefWidth(185);
        directionList.setMaxWidth(185);

 // Check solar panel direction
        Button directionButton = new Button("Check direction");

        Label directionMessage = new Label("");

        directionButton.setOnAction(event -> {

            String direction = directionList.getValue();

            if (direction == null) {
                directionMessage.setText("Please choose a direction");
            } else {
                directionMessage.setText("Valid direction: " + direction);
            }

        });


 // Check all user inputs
        Button checkAllButton = new Button("Check All");

        Label allMessage = new Label("");

        checkAllButton.setOnAction(event -> {

            if (longitudeField.getText().isBlank()
                    || latitudeField.getText().isBlank()
                    || areaField.getText().isBlank()
                    || tiltField.getText().isBlank()
                    || directionList.getValue() == null) {

                allMessage.setText("Please fill in all fields");

            } else {

                try {

                    double longitude =
                            Double.parseDouble(longitudeField.getText());

                    if (longitude < -180 || longitude > 180
                            || !Double.isFinite(longitude)) {

                        allMessage.setText(
                                "Longitude must be between -180 and 180"
                        );


                    } else {

 // Check latitude
                        try {

                            double latitude =
                                    Double.parseDouble(latitudeField.getText());

                            if (latitude < -90 || latitude > 90
                                    || !Double.isFinite(latitude)) {

                                allMessage.setText(
                                        "Latitude must be between -90 and 90"
                                );


                            } else {

 // Check solar panel area
                                try {

                                    double area =
                                            Double.parseDouble(areaField.getText());

                                    if (area <= 0 || !Double.isFinite(area)) {

                                        allMessage.setText(
                                                "Area must be greater than 0"
                                        );


                                    } else {

// Check solar panel tilt
                                        try {

                                            int tilt = Integer.parseInt(tiltField.getText());

                                            if (tilt < 0 || tilt > 90) {

                                                allMessage.setText(
                                                        "Tilt must be between 0 and 90"
                                                );

                                            } else {

                                                allMessage.setText(
                                                        "All inputs are valid"
                                                );

                                            }

                                        } catch (NumberFormatException e) {

                                            allMessage.setText(
                                                    "Please enter a valid whole number for tilt"
                                            );

                                        }

                                    }


                                } catch (NumberFormatException e) {

                                    allMessage.setText(
                                            "Please enter a valid area"
                                    );

                                }

                            }


                        } catch (NumberFormatException e) {

                            allMessage.setText(
                                    "Please enter a valid latitude"
                            );

                        }

                    }


                } catch (NumberFormatException e) {

                    allMessage.setText(
                            "Please enter a valid longitude"
                    );
                }
            }
        });


        // Create a list of cities
        /*
        ComboBox<String> cityList = new ComboBox<>();

        cityList.getItems().addAll(
                "Göteborg",
                "Stockholm",
                "Malmö",
                "Uppsala"
        );

        cityList.setPromptText("Choose your city");

        // Create the save button
        Button saveButton = new Button("Save Location");

        // Create a message for the user
        Label message = new Label("");

        // Create the location manager
        LocationManager location = new LocationManager();

        String savedCity = location.getCity();

        if (cityList.getItems().contains(savedCity)) {

            cityList.setValue(savedCity);

        }

        // What happens when the user clicks Save
        saveButton.setOnAction(event -> {

            String city = cityList.getValue();

            if (city != null) {

                location.saveCity(city);

                message.setText("Saved: " + city);

            } else {

                message.setText("Please choose a city first.");

            }
        });

        // Create the layout
        */
        VBox layout = new VBox(15);

// Put the back button on the left
        HBox backRow = new HBox(backButton);

        backRow.setAlignment(Pos.CENTER_LEFT);
        backRow.setPadding(new Insets(0, 0, 0, 20));
        backRow.setMaxWidth(Double.MAX_VALUE);

// Longitude row
        Label longitudeLabel = new Label("Longitude");
        longitudeLabel.setPrefWidth(90);

        HBox longitudeRow = new HBox(15, longitudeLabel, longitudeField);
        longitudeRow.setAlignment(Pos.CENTER);

// Latitude row
        Label latitudeLabel = new Label("Latitude");
        latitudeLabel.setPrefWidth(90);

        HBox latitudeRow = new HBox(15, latitudeLabel, latitudeField);
        latitudeRow.setAlignment(Pos.CENTER);


// Area row
        Label areaLabel = new Label("Area");
        areaLabel.setPrefWidth(90);

        HBox areaRow = new HBox(15, areaLabel, areaField);

// Tilt row
        Label tiltLabel = new Label("Tilt");
        tiltLabel.setPrefWidth(90);

        HBox tiltRow = new HBox(15, tiltLabel, tiltField);
        tiltRow.setAlignment(Pos.CENTER);

// Direction row
        Label directionLabel = new Label("Direction");
        directionLabel.setPrefWidth(90);

        HBox directionRow = new HBox(15, directionLabel, directionList);
        directionRow.setAlignment(Pos.CENTER);

        areaRow.setAlignment(Pos.CENTER);

        layout.getChildren().add(title);
        layout.getChildren().add(backRow);

// Location section
        layout.getChildren().add(locationTitle);

        layout.getChildren().add(longitudeRow);
       // layout.getChildren().add(checkButton);
       // layout.getChildren().add(errorMessage);

        layout.getChildren().add(latitudeRow);
       // layout.getChildren().add(latitudeButton);
       // layout.getChildren().add(latitudeMessage);

// Solar panel section
        layout.getChildren().add(solarPanelTitle);
        layout.getChildren().add(areaRow);
       // layout.getChildren().add(areaButton);
       // layout.getChildren().add(areaMessage);

        layout.getChildren().add(tiltRow);
        //layout.getChildren().add(tiltButton);
        // layout.getChildren().add(tiltMessage);

        layout.getChildren().add(directionRow);
        // layout.getChildren().add(directionButton);
        //layout.getChildren().add(directionMessage);

        // Check everything
        layout.getChildren().add(checkAllButton);
        layout.getChildren().add(allMessage);
        // layout.getChildren().add(cityList);
        // layout.getChildren().add(saveButton);
        // layout.getChildren().add(message);

        layout.setAlignment(Pos.CENTER);

// Create and display the window

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane, 520, 650);

        stage.setTitle("Solar App");
        stage.setScene(scene);
        stage.show();
    }
}