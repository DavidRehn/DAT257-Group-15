import javafx.application.Application;
import javafx.stage.Stage;
import location.AppliancesApp;
import location.LocationApp;
import location.StartMenu;
import other.AppInterface;
import other.Model;
import other.SubAppInterface;
import saveload.JsonSaveLoad;
import saveload.SaveLoadInterface;
import javafx.scene.image.Image;
import java.io.InputStream;
import java.io.IOException;
public class AppMain extends Application implements AppInterface{
    private StartMenu startMenu = new StartMenu();
    private LocationApp settingsMenu = new LocationApp();
    private AppliancesApp appliancesMenu = new AppliancesApp();

    private SaveLoadInterface SL = new JsonSaveLoad();
    private Model appData = new Model(SL);

    public void start(Stage stage) {

        java.net.URL iconUrl = getClass().getResource("/icons/solar_app_icon.png");

        if (iconUrl != null) {stage.getIcons().add(new Image(iconUrl.toExternalForm()));}
        else {System.out.println("Solar App icon not found!");
        }

        appData.LoadFromDisc();
        initSubApp(startMenu);
        initSubApp(settingsMenu);
        initSubApp(appliancesMenu);
        // One Stage for the entire application. PageNavigator creates one Scene.
        stage.setTitle("Solar App");
        viewStart(stage);
    }


    public static void main(String[] args) {
        launch(AppMain.class,args);
    }
    public void viewStart(Stage stage){
        System.out.println("Switch to start");
        startMenu.start(stage);
    }
    public void viewSettings(Stage stage){
        System.out.println("Switch to settings");
        settingsMenu.start(stage);
    }
    public void viewAppliances(Stage stage){
        System.out.println("Switch to appliances");
        appliancesMenu.start(stage);
    }
    public void initSubApp(SubAppInterface app){
        app.linkAppData(this.appData);
        app.linkRoot(this);
    }
}
