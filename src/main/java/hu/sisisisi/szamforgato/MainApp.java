package hu.sisisisi.szamforgato;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;


public class MainApp extends Application {

    private static MainApp appInstance = null;

    public static MainApp getAppInstance()
    {
        return appInstance;
    }

    private Map<Pages, String> fxmlFiles = Map.ofEntries(
            Map.entry(Pages.MainMenu, "menu.fxml"),
            Map.entry(Pages.Settings, "settings.fxml"),
            Map.entry(Pages.Game, "game.fxml"),
            Map.entry(Pages.PreGame, "PreGame.fxml")
    );

    private Stage stage;

    public Scene getCurrentScene()
    {
        return this.stage.getScene();
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        appInstance = this;

        this.stage = stage;
        stage.setTitle("Számforgató játék");

        showPage(Pages.MainMenu);

        stage.show();
    }

    public void showPage(Pages page) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFiles.get(page)));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public enum Pages
    {
        MainMenu,
        Settings,
        PreGame,
        Game
    }
}
