package location;

import java.time.LocalTime;
import java.util.ArrayList;

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
import location.builder.LabelBuilder;
import location.builder.LabelDirector;
import other.AppInterface;
import other.Model;
import other.SubAppInterface;
import powermarketdata.PowerMarketParser;
import powermarketdata.PowerMarketRetriver;
import ui.LanguageManager;
import ui.PageNavigator;

public class OverviewApp extends Application implements SubAppInterface {
    private Model appData;
    private AppInterface root;
    private LabelDirector director=new LabelDirector();
    @Override
    public void linkAppData(Model m) {
        this.appData = m;
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


        layout.getChildren().add(profileConstructor());
        layout.getChildren().add(profileConstructor());
        layout.getChildren().add(profileConstructor());
        

        Runnable refreshPrice = () -> {

            Thread worker = new Thread(() -> {

                try {
                    PowerMarketRetriver provider =
                            new PowerMarketRetriver();

                    ArrayList<Double> prices = new ArrayList<>(
                            PowerMarketParser.ParseToJson(
                                    provider.GetPowerMarketInfo()
                            )
                    );

                    int index = LocalTime.now().getHour() * 4;

                    if (index >= 0 && index < prices.size()) {

                        double value = prices.get(index);
                        /* 
                        Platform.runLater(() ->
                                priceValue.setText(value + " SEK/kWh")
                        );*/
                    }

                } catch (Exception exception) {

                    System.err.println(
                            "Could not update electricity price: "
                                    + exception.getMessage()
                    );
                }
            });

            worker.setDaemon(true);
            worker.start();
        };
        
        Timeline priceClock = new Timeline(
                new KeyFrame(
                        Duration.minutes(15),
                        e -> refreshPrice.run()
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


    private VBox profileConstructor(){
        //Main layout for profile box
        VBox layout = new VBox( 14);
        layout.setPadding(new Insets(24));
        layout.getStyleClass().add("app-card");
        layout.setMaxWidth(Double.MAX_VALUE);

        //Title
        LabelBuilder builder = new LabelBuilder();
        director.constructName(builder, new String[]{LanguageManager.get("location")});
        layout.getChildren().add(builder.getLabel());

        //Info box row
        HBox row1 = new HBox();
        row1.getChildren().add(infoBoxConstructor());
        row1.getChildren().add(infoBoxConstructor());
        row1.getChildren().add(infoBoxConstructor());
        layout.getChildren().add(row1);

        //Button row
        HBox row2 = new HBox();
        layout.getChildren();
        

        return layout;
    }

    private VBox infoBoxConstructor(){
        VBox layout = new VBox(8);
        layout.setPrefWidth(220);
        layout.setMinHeight(95);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER_LEFT);
        layout.getStyleClass().add("infoBox");

        LabelBuilder builder = new LabelBuilder();
        director.constructName(builder, new String[]{LanguageManager.get("location")});
        layout.getChildren().add(builder.getLabel());
        
        
        builder = new LabelBuilder();
        director.constructName(builder, new String[]{LanguageManager.get("location")});
        layout.getChildren().add(builder.getLabel());
        
        return layout;
    }

}