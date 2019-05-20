package hu.sisisisi.szamforgato.views;

import hu.sisisisi.szamforgato.MainApp;
import hu.sisisisi.szamforgato.SettingsHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SettingsView
{
    private static Logger logger = LoggerFactory.getLogger(SettingsView.class);

    @FXML
    private TextField usernameBox;
    @FXML
    private TextField tableSizeBox;
    @FXML
    private Button saveButton;

    public void initialize()
    {
        SettingsHandler settings = SettingsHandler.getSettings();
        this.usernameBox.setText(settings.getUsername());
        this.tableSizeBox.setText(Integer.toString(settings.getTableSize()));
    }

    @FXML
    public void handleSaveSettingsClick()
    {
        SettingsHandler handler = SettingsHandler.getSettings();
        handler.setTableSize(Integer.parseInt(tableSizeBox.getText()));
        handler.setUsername(usernameBox.getText());

        try
        {
            handler.saveToDisk();
            try
            {
                MainApp.getAppInstance().showPage(MainApp.Pages.MainMenu);
            }
            catch(IOException e)
            {
                logger.error("A főmenübe való visszalépés sikertelen.");
                ViewUtilities.showButtonErrorText(saveButton, "A főmenübe való visszalépés sikertelen.");
            }
        }
        catch(IOException e)
        {
            logger.error("A beállítások mentése sikertelen.");
            ViewUtilities.showButtonErrorText(saveButton, "A beállítások mentése sikertelen.");
        }
    }
}
