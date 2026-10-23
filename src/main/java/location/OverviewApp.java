package location;

import java.time.LocalTime;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import location.builder.ButtonBuilder;
import location.builder.LabelBuilder;
import location.builder.LabelDirector;
import location.builder.TextInputBuilder;
import location.builder.VBoxBuilder;
import other.AppInterface;
import other.Model;
import other.ProfileData;
import other.SubAppInterface;
import other.ZoneData;
import powermarketdata.PriceData;
import ui.LanguageManager;
import ui.PageNavigator;

public class OverviewApp extends Application implements SubAppInterface {
    private Model appData;
    private PriceData prices;
    private AppInterface root;
    private Stage stageIn;
    private LabelDirector director=new LabelDirector();

    private double totalProduction=0;
    private javafx.scene.control.Label totalProductionLabel;
    private double totalUsage=0;
    private javafx.scene.control.Label totalUsageLabel;
    private javafx.scene.control.Label totalDifferenceLabel;


    
    @Override
    public void linkAppData(Model m) {
        this.appData = m;
        this.prices=appData.GetPrices();
    }

    @Override
    public void linkRoot(AppInterface root) {
        this.root = root;
    }

    @Override
    public void start(Stage stage){
        stageIn=stage;
        //Main layout
        VBox layout = new VBox(20);

        layout.setPadding(new Insets(24));
        layout.setAlignment(Pos.TOP_LEFT);

        layout.getStyleClass().addAll(
                "layout",
                "home-content"
        );

        HBox header = new HBox( 14);
        header.setPadding(new Insets(24));
        header.setMaxWidth(Double.MAX_VALUE);
        header.setAlignment(Pos.CENTER);

        VBox temp;

        //Add info box for total production to header
        temp = infoBoxConstructor("overview.production", "0 W");
        totalProductionLabel = (javafx.scene.control.Label)temp.getChildren().get(1);
        header.getChildren().add(temp);

        //Add info box for total usage to header
        temp = infoBoxConstructor("overview.usage", "0 W");
        totalUsageLabel = (javafx.scene.control.Label)temp.getChildren().get(1);
        header.getChildren().add(temp);

        //Add info box for total difference to header
        temp = infoBoxConstructor("overview.difference", "0 W");
        totalDifferenceLabel = (javafx.scene.control.Label)temp.getChildren().get(1);
        header.getChildren().add(temp);

        //Add header bar to layout
        layout.getChildren().add(header);
        

        //Row holding profile name input and add button
        HBox inputRow = new HBox(100);
        inputRow.setAlignment(Pos.CENTER);
        inputRow.getStyleClass().add("app-card");
        inputRow.setMaxWidth(Double.MAX_VALUE);

        //Add info for current profile
        HBox nameRow=new HBox();
        nameRow.setAlignment(Pos.CENTER);
        nameRow.setMaxWidth(Double.MAX_VALUE);

        //"Profile name: " + name part of the input row
        String name = "";
        LabelBuilder builderBoxValue = new LabelBuilder();
        director.constructS(builderBoxValue, LanguageManager.get("overview.name")+": ", "boxText");
        nameRow.getChildren().add(builderBoxValue.getLabel());

        //Profile name: + name part of input row
        if(appData.Get("ProfileData")!=null){name=((ProfileData)appData.Get("ProfileData")).ProfileName();}
        builderBoxValue = new LabelBuilder();
        director.constructS(builderBoxValue, name, "boxText");
        nameRow.getChildren().add(builderBoxValue.getLabel());
        inputRow.getChildren().add(nameRow);

        //Input field for naming new profile
        nameRow=new HBox(15);
        nameRow.setAlignment(Pos.CENTER);
        nameRow.setMaxWidth(Double.MAX_VALUE);

        //Input field
        TextInputBuilder nameFieldBuilder = new TextInputBuilder();
        nameFieldBuilder.setLabelName("");
        nameFieldBuilder.setPromptText(LanguageManager.get("overview.name"));
        nameFieldBuilder.setStyl("app-input");
        nameFieldBuilder.setWidth(1000);
        TextField nameField = nameFieldBuilder.getTextField();
        nameRow.getChildren().add(nameField);

        //Button to add new profile
        ButtonBuilder buttonBuilder=new ButtonBuilder();
        director.constructName(buttonBuilder, LanguageManager.get("overview.add"));
        buttonBuilder.setEvent(() -> e ->{
            if(!nameField.getText().contains("*")){
                appData.AddProfile(nameField.getText());
                appData.SwitchProfile(appData.GetHighest());
                root.view(stage, "overview");
            }else{
                nameField.setText(LanguageManager.get("overview.error.name"));
            }
            //layout.getChildren().add(profileConstructor(appData.GetHighest(),test));
        });

        nameRow.getChildren().add(buttonBuilder.getButton());
        inputRow.getChildren().add(nameRow);

        //Making and adding all profiles to layout
        layout.getChildren().add(inputRow);
        int counter=0;
        ArrayList<String[]> maker=new ArrayList<>();
        totalProduction=0;
        totalUsage=0;
        while(appData.ContainKey("ProfileData*"+counter)){
            maker.clear();
            double productionValue=appData.GetProduction(counter);
            totalProduction+=productionValue;
            String[] prod={"production",""+(int)productionValue+" W"};
            maker.add(prod);

            double usageValue = appData.GetUsage(counter);
            totalUsage=usageValue;
            String[] usage = {"usage",""+(int)usageValue+" W"};
            maker.add(usage);

            String[] diff = {"overview.difference",""+(int)(productionValue-usageValue)+" W"};
            maker.add(diff);
            
            
            if(appData.Get("ZoneData",counter)!=null){
                String[] price = {"price",prices.GetCurrentPrice(((ZoneData)appData.Get("ZoneData",counter)).Zone(), LocalTime.now().getHour(), (int)LocalTime.now().getMinute()/4)+" SEK/kWh"};
                maker.add(price);
            }else{
                String[] price = {"price","-"+" SEK/kWh"};
                maker.add(price);
            }
            

            
            

            layout.getChildren().add(profileConstructor(counter,maker));
            counter++;

        }
        totalProductionLabel.setText((int)totalProduction+" W");
        totalUsageLabel.setText((int)totalUsage+" W");
        totalDifferenceLabel.setText((int)(totalProduction-totalUsage)+" W");
        
        Timeline priceClock = new Timeline(
                new KeyFrame(
                        Duration.minutes(15)
                )
        );

        // Scroll pane
        ScrollPane homeScroll = new ScrollPane(layout);

        homeScroll.setFitToWidth(true);
        homeScroll.getStyleClass().add("home-scroll");

        // Display page using PageNavigator
        PageNavigator.show(
                stage,
                root,
                homeScroll,
                "overview",
                priceClock::stop
        );
        
    }


    private VBox profileConstructor(int name,ArrayList<String[]> boxes){
        
        //Main layout for profile box
        VBox layout = new VBox( 14);
        layout.setPadding(new Insets(24));
        layout.getStyleClass().add("app-card");
        layout.setMaxWidth(Double.MAX_VALUE);

        //Title
        LabelBuilder builder = new LabelBuilder();
        director.constructName(builder, appData.GetName(name));
        layout.getChildren().add(builder.getLabel());
        
        //Info box row
        HBox infoRow = new HBox(15);
        for (String[] arr: boxes) {
            infoRow.getChildren().add(infoBoxConstructor(arr[0],arr[1]));
        }
        layout.getChildren().add(infoRow);
        
        //Button row
        HBox buttonRow = new HBox(15);
        ButtonBuilder buttonBuilder=new ButtonBuilder();
        //Add lang
        director.constructName(buttonBuilder, LanguageManager.get("overview.select"));
        buttonBuilder.setEvent(() -> e ->{appData.SwitchProfile(name);root.view(stageIn,"overview");});
        buttonRow.getChildren().add(buttonBuilder.getButton());

        buttonBuilder=new ButtonBuilder();
        director.constructName(buttonBuilder, LanguageManager.get("appliance.remove"));
        buttonBuilder.setEvent(() -> e ->{appData.RemoveProfile(name);root.view(stageIn,"overview");});
        buttonRow.getChildren().add(buttonBuilder.getButton());

        layout.getChildren().add(buttonRow);
        

        return layout;
    }
    

    private VBox infoBoxConstructor(String title, String value){
        VBoxBuilder layout = new VBoxBuilder();
        director.constructVboxAWHIS(layout,8, Pos.CENTER_LEFT, 220, 95, 20, "infoBox");

        LabelBuilder builderBoxTitel = new LabelBuilder();
        director.constructName(builderBoxTitel, LanguageManager.get(title));
        layout.addChildren(builderBoxTitel.getLabel());
        
        LabelBuilder builderBoxValue = new LabelBuilder();
        director.constructS(builderBoxValue, value, "boxText");
        layout.addChildren(builderBoxValue.getLabel());

        return layout.getVBox();
    }
    

}