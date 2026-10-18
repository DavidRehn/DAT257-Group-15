package ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import i18n.LanguageManager;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import other.AppInterface;

/** One persistent scene: the sidebar and header do not change between pages. */
public class PageNavigator {

    private static Stage window;
    private static AppInterface app;
    private static BorderPane shell;
    private static StackPane content;
    private static Scene scene;
    private static Timeline clock;
    private static Label clockLabel;
    private static Button languageButton;
    private static Button homeButton = sidebarButton("home", "home");
    private static Button settingsButton = sidebarButton("settings", "settings");
    private static Button appliancesButton = sidebarButton("appliances", "appliances");
    private static Button overviewButton = sidebarButton("overview", "overview");
    private final static Button[] sidebarButtons={homeButton,settingsButton,appliancesButton,overviewButton};
    

    private static String currentPage = "home";
    private static Runnable onLeave = () -> {};

    private PageNavigator() {
        // Utility class: no instances needed.
    }

    public static void show(Stage stage, AppInterface navigation, Node pageContent, String page, Runnable cleanup) {
        if (scene == null || window != stage) {buildShell(stage, navigation);}
        // Stop page-specific tasks from the previously displayed page.
        onLeave.run();
        onLeave = cleanup == null ? () -> {} : cleanup;
        currentPage = page;

        updateLanguage();
        updateActiveButton();

        // Change ONLY the central content, not the Stage or the Scene.
        pageContent.setOpacity(0);
        pageContent.setTranslateX(12);
        content.getChildren().setAll(pageContent);

        FadeTransition fade = new FadeTransition(
         Duration.millis(180), pageContent);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(
          Duration.millis(180), pageContent);
        slide.setFromX(12);
        slide.setToX(0);
        new ParallelTransition(fade, slide).play();
    }

    private static void buildShell(Stage stage, AppInterface navigation) {
        if (clock != null) {clock.stop();}

        window = stage;app = navigation;shell = new BorderPane();content = new StackPane();
        content.getStyleClass().add("home-content");
        Label logo = new Label("☀ Solar App");
        logo.getStyleClass().add("sidebar-title");

        VBox sidebar = new VBox(18);
        sidebar.setPadding(new Insets(30, 15, 20, 15));
        sidebar.setPrefWidth(220);
        sidebar.setMinWidth(220);
        sidebar.getStyleClass().add("sidebar");
        //Add button actions
        homeButton.setOnAction(e -> app.view(window,"home"));
        sidebar.getChildren().add(homeButton);

        overviewButton.setOnAction(e -> app.view(window,"overview"));
        sidebar.getChildren().add(overviewButton);

        settingsButton.setOnAction(e -> app.view(window,"settings"));
        sidebar.getChildren().add(settingsButton);

        appliancesButton.setOnAction(e -> app.view(window,"appliances"));
        sidebar.getChildren().add(appliancesButton);

        
        

        clockLabel = new Label();
        clockLabel.setMinWidth(135);


        languageButton = new Button();
        languageButton.getStyleClass().add("language-button");
        languageButton.setOnAction(e -> {
            if (LanguageManager.getLanguage().equals("en")) {
                LanguageManager.setLanguage("sv");
            } else {
                LanguageManager.setLanguage("en");
            }
            // Reload current page's translated labels. The shell stays intact.
            updateLanguage();
            openCurrentPage();
        });

        Button themeButton = ThemeManager.createButton(stage);
        HBox controls = new HBox(10, languageButton, themeButton);
        controls.setAlignment(Pos.CENTER_RIGHT);

        BorderPane header = new BorderPane();
        header.setLeft(clockLabel);

        header.setRight(controls);
        header.setPadding(new Insets(22, 25, 14, 25));
        header.getStyleClass().addAll("header", "home-header");

        VBox middle = new VBox(0, header, content);
        VBox.setVgrow(content, Priority.ALWAYS);
        middle.getStyleClass().add("home-content");

        shell.setLeft(sidebar);
        shell.setCenter(middle);

        scene = new Scene(shell, 1200, 800);
        ThemeManager.apply(scene);
        stage.setTitle("Solar App");
        stage.setMinWidth(900);
        stage.setMinHeight(650);
        stage.setScene(scene);
        stage.show();

        updateClock();
        clock = new Timeline(new KeyFrame(Duration.seconds(30), event -> updateClock()));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    private static Button sidebarButton(String key, String page) {
        Button button = new Button(LanguageManager.get(key));
        button.getStyleClass().add("sidebar-button");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setUserData(page);
        return button;
    }

    private static void updateClock() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEE HH:mm",
             Locale.forLanguageTag(LanguageManager.getLanguage()));
        clockLabel.setText(LocalDateTime.now().format(format));
    }

    private static void updateLanguage() {
        for(Button button : sidebarButtons){
            button.setText(LanguageManager.get((String)String.valueOf(button.getUserData())));
        }
        
        languageButton.setText(LanguageManager.getLanguage().equals("en") ? "SV" : "EN");
        updateClock();
    }

    private static void updateActiveButton() {
        
        for (Button button : sidebarButtons) {button.getStyleClass().remove("sidebar-active");
            if (currentPage.equals(button.getUserData())) {button.getStyleClass().add("sidebar-active");
            }
        }
    }

    private static void openCurrentPage() {
        app.view(window,currentPage);
    
    }
}
