package location;

import java.util.HashMap;

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
import other.SubAppInterface;
import powermarketdata.PriceData;
import ui.LanguageManager;
import ui.PageNavigator;

public class OverviewApp extends Application implements SubAppInterface {
    private Model appData;
    private PriceData prices;
    private AppInterface root;
    private LabelDirector director=new LabelDirector();
    
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
        //Main layout
        VBox layout = new VBox(20);

        layout.setPadding(new Insets(24));
        layout.setAlignment(Pos.TOP_LEFT);

        layout.getStyleClass().addAll(
                "layout",
                "home-content"
        );




        

        
        HBox inputRow = new HBox(15);
        inputRow.setAlignment(Pos.CENTER);
        inputRow.setMaxWidth(Double.MAX_VALUE);

        TextInputBuilder nameFieldBuilder = new TextInputBuilder();
        nameFieldBuilder.setLabelName("");
        nameFieldBuilder.setPromptText(LanguageManager.get("overview.name"));
        nameFieldBuilder.setStyl("app-input");
        nameFieldBuilder.setWidth(600);
        TextField nameField = nameFieldBuilder.getTextField();
        inputRow.getChildren().add(nameField);

        ButtonBuilder buttonBuilder=new ButtonBuilder();
        HashMap<String,String> test=new HashMap<>();
        test.put("production","0");
        director.constructName(buttonBuilder, LanguageManager.get("appliance.add"));
        buttonBuilder.setEvent(() -> e ->{
            if(!nameField.getText().contains("*")){
                appData.AddProfile(nameField.getText());
                root.view(stage, "overview");
            }else{
                nameField.setText(LanguageManager.get("overview.error.name"));
            }
            
            //layout.getChildren().add(profileConstructor(appData.GetHighest(),test));
        });

        inputRow.getChildren().add(buttonBuilder.getButton());

        layout.getChildren().add(inputRow);
        int counter=0;
        HashMap<String,String> maker=new HashMap<>();
        while(appData.ContainKey("ProfileData*"+counter)){
            maker.clear();
            maker.put("production","0");
            maker.put("usage","0");
            maker.put("price","0");

            layout.getChildren().add(profileConstructor(counter,maker,stage));
            counter++;

        }
        
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


    private VBox profileConstructor(int name,HashMap<String,String> boxes,Stage stage){
        
        //Main layout for profile box
        VBox layout = new VBox( 14);
        layout.setPadding(new Insets(24));
        layout.getStyleClass().add("app-card");
        layout.setMaxWidth(Double.MAX_VALUE);

        //Title
        LabelBuilder builder = new LabelBuilder();
        director.constructName(builder, appData.GetName(name));
        layout.getChildren().add(builder.getLabel());
        /* 
        //Info box row
        HBox infoRow = new HBox();
        for (HashMap.Entry<String, String> en : boxes.entrySet()) {
            infoRow.getChildren().add(infoBoxConstructor(en.getKey(),en.getValue()));
        }
        layout.getChildren().add(infoRow);
        */
        //Button row
        HBox buttonRow = new HBox();
        ButtonBuilder buttonBuilder=new ButtonBuilder();
        //Add lang
        director.constructName(buttonBuilder, LanguageManager.get("overview.select"));
        buttonBuilder.setEvent(() -> e ->{appData.SwitchProfile(name);});
        buttonRow.getChildren().add(buttonBuilder.getButton());

        buttonBuilder=new ButtonBuilder();
        director.constructName(buttonBuilder, LanguageManager.get("appliance.remove"));
        buttonBuilder.setEvent(() -> e ->{appData.RemoveProfile(name);root.view(stage,"overview");});
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