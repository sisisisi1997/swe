package hu.sisisisi.szamforgato.views;

import hu.sisisisi.szamforgato.GameController;
import hu.sisisisi.szamforgato.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PreGameView
{
    private static Logger logger = LoggerFactory.getLogger(PreGameView.class);

    @FXML
    private TextField tableSizeText;

    public void startButtonClicked(MouseEvent e)
    {
        int size = Integer.parseInt(tableSizeText.getText());
        GameController.getInstance().createGameState(size);
        logger.info("Játéktábla mérete: " + size);

        try
        {
            MainApp.getAppInstance().showPage(MainApp.Pages.Game);
        }
        catch(IOException ex)
        {
            logger.error("A játék betöltése sikertelen.");
            // TODO: error msg mutatása a felhasználónak
        }
    }
}
