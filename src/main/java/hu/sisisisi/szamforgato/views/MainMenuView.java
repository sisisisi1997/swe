package hu.sisisisi.szamforgato.views;

import hu.sisisisi.szamforgato.MainApp;
import hu.sisisisi.szamforgato.ScoreBoardHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;

public class MainMenuView
{
    private static Logger logger = LoggerFactory.getLogger(MainMenuView.class);

    @FXML
    private GridPane mainMenu;
    @FXML
    private Button exitButton;

    private Thread exitRedThread;
    private Paint defaultTextFill;
    private boolean isDefTextFillSet = false;

    @FXML
    private void handleNewGameAction(ActionEvent event)
    {
        try
        {
            MainApp.getAppInstance().showPage(MainApp.Pages.PreGame);
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

    @FXML
    private void handleExitAction(MouseEvent event)
    {
        try
        {
            MainApp.getAppInstance().stop();
        }
        catch(Exception e)
        {
            if(exitRedThread != null && exitRedThread.isAlive())
            {
                exitRedThread.interrupt();
            }
            logger.error("Sikertelen kilépés");
            final Paint p = isDefTextFillSet ? defaultTextFill : exitButton.getTextFill();
            if(!isDefTextFillSet) // nem tudjuk itt beállítani a p-t, mert final
            {
                this.defaultTextFill = p;
                this.isDefTextFillSet = true;
            }
            exitButton.setTextFill(Color.RED);
            exitButton.setText("Sikertelen kilépés!");
            exitRedThread = new Thread(() ->
            {
                try
                {
                    Thread.sleep(1500);
                    Platform.runLater(() ->
                    {
                        exitButton.setText("Kilépés");
                        exitButton.setTextFill(p);
                    });
                }
                catch(InterruptedException ex)
                {
                    // resetelve lett az idő addig, amíg visszíállítjuk
                }
            });
            exitRedThread.start();
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

    public void handleScoreClick(MouseEvent event) {
        try
        {
            MainApp.getAppInstance().showPage(MainApp.Pages.ScoreBoard);
        }
        catch (IOException e)
        {

        }
    }
}
