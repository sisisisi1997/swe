package hu.sisisisi.szamforgato.views;

import hu.sisisisi.szamforgato.MainApp;
import hu.sisisisi.szamforgato.ScoreBoardHandler;
import hu.sisisisi.szamforgato.SettingsHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


/**
 * Ez a nézet a ranglétra nézet, melyben meg lehet tekinteni a különböző táblaméretek legjobb pontjait elérő játékosokat.
 */
public class ScoreView
{
    @FXML
    private TextField sizeText;

    @FXML
    private GridPane scoreGrid;

    @FXML
    private Button backToMenuButton;

    private Background red = new Background(new BackgroundFill(Color.RED, new CornerRadii(0), null));
    private Background white = null;

    private Label[] helyek = new Label[10];
    private Label[] pontok = new Label[10];

    @FXML
    private void initialize()
    {
        sizeText.setText(Integer.toString(SettingsHandler.getSettings().getTableSize()));

        for(int i = 0; i < helyek.length; ++ i)
        {
            helyek[i] = new Label((i + 1) + ". hely:");
            GridPane.setHalignment(helyek[i], HPos.CENTER);
            scoreGrid.add(helyek[i], 0, i + 2, 2, 1);

            pontok[i] = new Label();
            GridPane.setHalignment(pontok[i], HPos.CENTER);
            scoreGrid.add(pontok[i], 2, i + 2);
        }
    }

    @FXML
    private void handleTextChanged(KeyEvent event)
    {
        if(event.getCode() == KeyCode.ENTER)
        {
            handleLoadScores();
        }
    }

    @FXML
    private void handleLoadScores()
    {
        if(white == null)
        {
            white = sizeText.getBackground();
        }

        try
        {
            int size = Integer.parseInt(sizeText.getText());
            sizeText.setBackground(white);
            if(Files.exists(Paths.get("board" + size + ".json")))
            {
                ArrayList<Pair<String, Integer>> scores = ScoreBoardHandler.getScores(size);
                for(int i = 0; i < 10; ++ i)
                {
                    if(scores != null && i < scores.size())
                    {
                        pontok[i].setText(scores.get(i).getKey() + ": " + scores.get(i).getValue() + " lépés");
                    }
                    else
                    {
                        pontok[i].setText("");
                    }
                }
            }
            else
            {
                for(int i = 0; i < 10; ++ i)
                {
                    pontok[i].setText("");
                }
            }
        }
        catch(NumberFormatException ex)
        {
            sizeText.setBackground(red);
        }
    }

    @FXML
    private void handleBackToMenuClick()
    {
        try
        {
            MainApp.getAppInstance().showPage(MainApp.Pages.MainMenu);
        }
        catch(IOException e)
        {
            ViewUtilities.showButtonErrorText(backToMenuButton, "A menübe való visszalépés sikertelen.");
        }
    }
}
