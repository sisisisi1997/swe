package hu.sisisisi.szamforgato.views;

import hu.sisisisi.szamforgato.SettingsHandler;
import hu.sisisisi.szamforgato.controller.GameController;
import hu.sisisisi.szamforgato.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Ez a játék elkezdése előtti nézet, mely lehetőséget ad a játékosnak a táblaméret és a felhasználónév beállítására.
 */
public class PreGameView
{
    private static Logger logger = LoggerFactory.getLogger(PreGameView.class);

    @FXML
    private void initialize()
    {
        this.tableSizeText.setText(Integer.toString(SettingsHandler.getSettings().getTableSize()));
        this.playerNameText.setText(SettingsHandler.getSettings().getUsername());
    }

    @FXML
    private TextField playerNameText;

    @FXML
    private TextField tableSizeText;

    @FXML
    private Button startButton;

    @FXML
    private void startButtonClicked()
    {
        int size = Integer.parseInt(tableSizeText.getText());
        GameController.getInstance().setName(playerNameText.getText());
        GameController.getInstance().createGameState(size);
        logger.info("Játéktábla mérete: " + size);

        try
        {
            MainApp.getAppInstance().showPage(MainApp.Pages.Game);
        }
        catch(IOException ex)
        {
            logger.error("A játék betöltése sikertelen.");
            ViewUtilities.showButtonErrorText(startButton, "A játék betöltése sikertelen.");
        }
    }
}
