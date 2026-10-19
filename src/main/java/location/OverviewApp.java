package location;

import javafx.application.Application;
import javafx.stage.Stage;
import other.AppInterface;
import other.Model;
import other.SubAppInterface;

public class OverviewApp extends Application implements SubAppInterface {
    private Model appData;
    private AppInterface root;
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
        
    }


}