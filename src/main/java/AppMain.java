import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import location.AppliancesApp;
import location.LocationApp;
import location.StartMenu;
import other.AppInterface;
import other.Model;
import other.SubAppInterface;
import saveload.JsonSaveLoad;
import saveload.SaveLoadInterface;
public class AppMain extends Application implements AppInterface{
    private StartMenu homeMenu = new StartMenu();
    private LocationApp settingsMenu = new LocationApp();
    private AppliancesApp appliancesMenu = new AppliancesApp();
    private final HashMap<String,Application> Menus = new HashMap<>(){{
        put("home",homeMenu);
        put("settings",settingsMenu);
        put("appliances",appliancesMenu);
        put("overview",homeMenu);
    }};

    private SaveLoadInterface SL = new JsonSaveLoad();
    private Model appData = new Model(SL);

    public void start(Stage stage) {

        java.net.URL iconUrl = getClass().getResource("/icons/solar_app_icon.png");

        if (iconUrl != null) {stage.getIcons().add(new Image(iconUrl.toExternalForm()));}
        else {System.out.println("Solar App icon not found!");
        }

        appData.LoadFromDisc();
        initSubApp(homeMenu);
        initSubApp(settingsMenu);
        initSubApp(appliancesMenu);
        // One Stage for the entire application. PageNavigator creates one Scene.
        stage.setTitle("Solar App");
        view(stage,"home");
    }


    public static void main(String[] args) {
        launch(AppMain.class,args);
    }

    public void view(Stage stage,String key){
        try {
            
            Menus.get(key).start(stage);
            
            
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Menu key not found, ensure String key in the view(Stage stage, String key) call matches a key in AppMain.Menus");
            Platform.exit();
            
        }
        
    }
    
    

    public void initSubApp(SubAppInterface app){
        app.linkAppData(this.appData);
        app.linkRoot(this);
    }
}
