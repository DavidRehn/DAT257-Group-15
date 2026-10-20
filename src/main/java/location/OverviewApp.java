package location;

import java.util.HashMap;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import location.builder.ButtonBuilder;
import location.builder.LabelBuilder;
import location.builder.LabelDirector;
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

        ButtonBuilder buttonBuilder=new ButtonBuilder();
        HashMap<String,String> test=new HashMap<>();
        test.put("production","0");
        director.constructName(buttonBuilder, new String[]{LanguageManager.get("appliance.add")});
        buttonBuilder.setEvent(() -> e ->{appData.AddProfile("Name");layout.getChildren().add(profileConstructor(appData.GetHighest(),test));});

        layout.getChildren().add(buttonBuilder.getButton());

        int counter=0;
        HashMap<String,String> maker=new HashMap<>();
        while(appData.ContainKey("ProfileData*"+counter)){
            maker.clear();
            maker.put("production","0");
            maker.put("usage","0");
            maker.put("price","0");

            layout.getChildren().add(profileConstructor(counter,maker));
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


    private VBox profileConstructor(int name,HashMap<String,String> boxes){
        
        //Main layout for profile box
        VBox layout = new VBox( 14);
        layout.setPadding(new Insets(24));
        layout.getStyleClass().add("app-card");
        layout.setMaxWidth(Double.MAX_VALUE);

        //Title
        LabelBuilder builder = new LabelBuilder();
        director.constructName(builder, new String[]{appData.GetName(name)});
        layout.getChildren().add(builder.getLabel());

        //Info box row
        HBox infoRow = new HBox();
        for (HashMap.Entry<String, String> en : boxes.entrySet()) {
            infoRow.getChildren().add(infoBoxConstructor(en.getKey(),en.getValue()));
        }
        layout.getChildren().add(infoRow);

        //Button row
        HBox buttonRow = new HBox();
        ButtonBuilder buttonBuilder=new ButtonBuilder();
        //Add lang
        director.constructName(buttonBuilder, new String[]{"Select"});
        buttonBuilder.setEvent(() -> e ->{appData.SwitchProfile(name);});
        buttonRow.getChildren().add(buttonBuilder.getButton());

        buttonBuilder=new ButtonBuilder();
        director.constructName(buttonBuilder, new String[]{LanguageManager.get("appliance.remove")});
        buttonBuilder.setEvent(() -> e ->{appData.RemoveProfile(name);});
        buttonRow.getChildren().add(buttonBuilder.getButton());

        layout.getChildren().add(buttonRow);
        

        return layout;
    }

    


    private VBox infoBoxConstructor(String title, String value){
        VBox layout = new VBox(8);
        layout.setPrefWidth(220);
        layout.setMinHeight(95);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER_LEFT);
        layout.getStyleClass().add("infoBox");

        LabelBuilder builder = new LabelBuilder();
        director.constructName(builder, new String[]{LanguageManager.get(title)});
        layout.getChildren().add(builder.getLabel());
        
        
        builder = new LabelBuilder();
        director.constructName(builder, new String[]{value});
        javafx.scene.control.Label temp = builder.getLabel();
        temp.getStyleClass().add("boxText");
        layout.getChildren().add(builder.getLabel());
        
        return layout;
    }
    

}