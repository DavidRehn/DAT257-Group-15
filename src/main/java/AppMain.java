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

public class AppMain extends Application implements AppInterface{
    private StartMenu startMenu = new StartMenu();
    private LocationApp settingsMenu = new LocationApp();
    private AppliancesApp appliancesMenu = new AppliancesApp();

    private SaveLoadInterface SL = new JsonSaveLoad();
    private Model appData = new Model(SL);

    public void start(Stage stage) {
        appData.LoadFromDisc();
        initSubApp(startMenu);
        initSubApp(settingsMenu);
        initSubApp(appliancesMenu);
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
