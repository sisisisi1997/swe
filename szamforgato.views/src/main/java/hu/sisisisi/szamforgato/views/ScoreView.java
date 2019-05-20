package hu.sisisisi.szamforgato.views;

import hu.sisisisi.szamforgato.ScoreBoardHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class ScoreView
{
    @FXML
    private TextField sizeText;

    @FXML
    private GridPane scoreGrid;

    private Background red = new Background(new BackgroundFill(Color.RED, new CornerRadii(0), null));
    private Background white = null;

    private Label[] helyek = new Label[10];
    private Label[] pontok = new Label[10];

    public void initialize()
    {
        for(int i = 0; i < helyek.length; ++ i)
        {
            helyek[i] = new Label((i + 1) + ". hely:");
            GridPane.setHalignment(helyek[i], HPos.CENTER);
            scoreGrid.add(helyek[i], 0, i + 1);

            pontok[i] = new Label();
            GridPane.setHalignment(pontok[i], HPos.CENTER);
            scoreGrid.add(pontok[i], 1, i + 1);
        }
    }

    public void handleSizeChanged(KeyEvent event)
    {
        if(white == null)
        {
            white = sizeText.getBackground();
        }
        if(event.getCode() == KeyCode.ENTER)
        {
            try
            {
                int size = Integer.parseInt(sizeText.getText());
                sizeText.setBackground(white);
                if(Files.exists(Paths.get("board" + size + ".json")))
                {
                    ArrayList<Pair<String, Integer>> scores = ScoreBoardHandler.getScores(size);
                    for(int i = 0; i < 10; ++ i)
                    {
                        if(i < scores.size())
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
    }
}
