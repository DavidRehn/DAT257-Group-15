package location.builder.Event;

import location.builder.BBuilder;

public class CheckAllUserInputs implements BBuilder{

    public void setEvent(BBuilder bBuilder){
/*
        checkAllButton.setOnAction(event -> {

            if (longitudeField.getText().isBlank()
                    || latitudeField.getText().isBlank()
                    || areaField.getText().isBlank()
                    || tiltField.getText().isBlank()
                    || efficiencyField.getText().isBlank()
                    || directionList.getValue() == null
                    || zoneList.getValue() == null) {

                allMessage.setText("Please fill in all fields");

            } else {

//Check Longitude
                double longitude=0;
                try {

                    longitude = Double.parseDouble(longitudeField.getText());

                    if (longitude < -180 || longitude > 180 || !Double.isFinite(longitude)) {
                        //Sets message at the bottom of the setting screen
                        allMessage.setText("Longitude must be between -180 and 180");
                        
                        //Exit early
                        return;


                    } 
                } catch (NumberFormatException e) {

                    allMessage.setText("Please enter a valid longitude");

                }
                    
 // Check Latitude
                double latitude=0;
                try {

                    latitude = Double.parseDouble(latitudeField.getText());

                    if (latitude < -90 || latitude > 90 || !Double.isFinite(latitude)) {

                        allMessage.setText(
                            "Latitude must be between -90 and 90"
                        );
                        return;

                    }
                } catch (NumberFormatException e) {

                    allMessage.setText("Please enter a valid latitude");

                }

 // Check solar panel area
                double area=0;
                try {
                    area = Double.parseDouble(areaField.getText());

                    if (area <= 0 || !Double.isFinite(area)) {

                        allMessage.setText("Area must be greater than 0");
                        return;
                    }
                } catch (NumberFormatException e) {

                    allMessage.setText("Please enter a valid area");

                }

// Check solar panel tilt
                int tilt=0;
                try {

                    tilt = Integer.parseInt(tiltField.getText());

                    if (tilt < 0 || tilt > 90) {

                        allMessage.setText("Tilt must be between 0 and 90");
                        return;
                    }
                } catch (NumberFormatException e) {

                    allMessage.setText("Please enter a valid whole number for tilt");

                }
                    
// Check solar panel efficiency
                double efficiency=0;
                try {
                    efficiency = Double.parseDouble(efficiencyField.getText());

                    if (efficiency <= 0.0||efficiency > 1.0|| !Double.isFinite(efficiency)) {

                        allMessage.setText("Efficiency must be between 0.0 and 1.0");
                        return;
                    }
                    
                } catch (NumberFormatException e) {

                    allMessage.setText("Please enter a valid number for tilt");
                    
                }

                allMessage.setText("All inputs are valid");

                                                
// Save the user's information
                
                appData.Store("LocationData",new LocationData(longitude, latitude));
                appData.Store("ZoneData",new ZoneData(zoneList.getValue()));
                appData.Store("SolarPanel", new SolarPanel(area,tilt,SolarPanel.directionConverter(directionList.getValue().toUpperCase()),efficiency));
                
                appData.SaveToDisc();

            }//end of else
        });
        */

    }

}
