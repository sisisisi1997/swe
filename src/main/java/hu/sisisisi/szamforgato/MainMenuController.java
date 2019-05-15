package hu.sisisisi.szamforgato;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class MainMenuController {
    @FXML
    private GridPane mainMenu;

    @FXML
    private void handleNewGameAction(ActionEvent event)
    {
        try
        {
            MainApp.getAppInstance().showPage(MainApp.Pages.Game);
        }
        catch(IOException e)
        {

        }
    }

    @FXML
    private void handleSettingsAction(ActionEvent event)
    {
        try
        {
            MainApp.getAppInstance().showPage(MainApp.Pages.Settings);
        }
        catch(IOException e)
        {

        }
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
}
