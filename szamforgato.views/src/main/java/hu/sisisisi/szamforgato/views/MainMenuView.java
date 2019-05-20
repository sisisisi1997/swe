package hu.sisisisi.szamforgato.views;

import hu.sisisisi.szamforgato.MainApp;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MainMenuView
{
    private static Logger logger = LoggerFactory.getLogger(MainMenuView.class);

    @FXML
    private GridPane mainMenu;
    @FXML
    private Button newGameButton;
    @FXML
    private Button settingsButton;
    @FXML
    public Button scoreBoardButton;

    @FXML
    private void handleNewGameClick()
    {
        try
        {
            MainApp.getAppInstance().showPage(MainApp.Pages.PreGame);
        }
        catch(IOException e)
        {
            logger.error("A játék elkezdése sikertelen.");
            ViewUtilities.showButtonErrorText(newGameButton, "A játék elkezdése sikertelen.");
        }
    }

    @FXML
    private void handleSettingsClick()
    {
        try
        {
            MainApp.getAppInstance().showPage(MainApp.Pages.Settings);
        }
        catch(IOException e)
        {
            logger.error("A beállítások megnyitása sikertelen.");
            ViewUtilities.showButtonErrorText(settingsButton, "A beállítások megnyitása sikertelen.");
        }
    }

    @FXML
    private void handleExitClick()
    {
        Platform.exit();
    }

    public void initialize()
    {
        for(Node e : mainMenu.getChildren())
        {
            if(e.getClass() == Button.class)
            {
                ((Button)e).setPrefWidth(Double.POSITIVE_INFINITY);
                GridPane.setMargin(e, new Insets(5, 0, 5, 0));
            }
        }
    }

    public void handleScoreClick()
    {
        try
        {
            MainApp.getAppInstance().showPage(MainApp.Pages.ScoreBoard);
        }
        catch (IOException e)
        {
            logger.error("A ranglétra betöltése sikertelen.");
            ViewUtilities.showButtonErrorText(scoreBoardButton, "A ranglétra betöltése sikertelen.");
        }
    }
}
