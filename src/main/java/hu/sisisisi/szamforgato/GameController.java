package hu.sisisisi.szamforgato;

import hu.sisisisi.szamforgato.model.Direction;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GameController
{
    private static Logger logger = LoggerFactory.getLogger(GameController.class);

    @FXML
    private GridPane gameGrid;

    private Label[][] labels;

    private int selectedRow = -1,
                selectedCol = -1,
                rowColCount = 5,
                sidePercent = 30;

    private ImageView leftImage,
                      rightImage,
                      upImage,
                      downImage;

    public void initialize()
    {
        gameGrid.setOnMouseClicked(this::gameGridMouseClick);

        labels = new Label[rowColCount][rowColCount];

        setGridFrame( rowColCount + 2);
        if(!addArrows())
        {
            try
            {
                MainApp.getAppInstance().stop();
            }
            catch(Exception e)
            {
                logger.error("Sikertelen kilépés a játékból hiba után.");
            }
        }

        for(int column = 0; column < rowColCount; ++ column)
        {
            for(int row = 0; row < rowColCount; row ++)
            {
                Label l = new Label(Integer.toString((rowColCount * row) + column));
                gameGrid.add(l, column + 2, row + 2);
                GridPane.setHalignment(l, HPos.CENTER);
                labels[column][row] = l;
            }
        }

        selectCell(0, 0);
    }

    private boolean addArrows()
    {
        try
        {
            leftImage = new ImageView(new Image(this.getClass().getResource("Left.png").openStream()));
            rightImage = new ImageView(new Image(this.getClass().getResource("Right.png").openStream()));
            upImage = new ImageView(new Image(this.getClass().getResource("Left.png").openStream()));
            downImage = new ImageView(new Image(this.getClass().getResource("Right.png").openStream()));
        }
        catch (IOException e)
        {
           logger.error("Nem lehetett betölteni a nyílgombokat, kilépés.");
           return false;
        }

        GridPane.setHalignment(leftImage, HPos.CENTER);
        GridPane.setHalignment(upImage, HPos.CENTER);
        GridPane.setHalignment(rightImage, HPos.CENTER);
        GridPane.setHalignment(downImage, HPos.CENTER);

        GridPane.setValignment(leftImage, VPos.CENTER);
        GridPane.setValignment(upImage, VPos.CENTER);
        GridPane.setValignment(rightImage, VPos.CENTER);
        GridPane.setValignment(downImage, VPos.CENTER);

        upImage.setRotate(90);
        downImage.setRotate(90);

        setArrowSize();

        gameGrid.add(leftImage, 1, 2, 1, rowColCount);
        gameGrid.add(upImage, 2, 1, rowColCount, 1);
        gameGrid.add(rightImage, rowColCount + 2, 2, 1, rowColCount);
        gameGrid.add(downImage, 2, rowColCount + 2, rowColCount, 1);

        gameGrid.heightProperty().addListener((ObservableValue<? extends Number> observableValue, Number number, Number t1) ->
            setArrowSize());
        gameGrid.widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number number, Number t1) ->
            setArrowSize());

        logger.debug("Nyílgombok sikeresen hozzáadva.");
        return true;
    }

    private void setArrowSize()
    {
        double w = this.getCellWidth() * 0.95,
               h = this.getCellHeight() * rowColCount * 0.8;

        leftImage.setFitWidth(w);
        leftImage.setFitHeight(h);

        upImage.setFitHeight(h);
        upImage.setFitWidth(w);

        rightImage.setFitWidth(w);
        rightImage.setFitHeight(h);

        downImage.setFitHeight(h);
        downImage.setFitWidth(w);
    }

    private void goLeft(MouseEvent e)
    {

    }

    private double getCellWidth()
    {
        return gameGrid.getWidth() * (100 - 2 * this.sidePercent) * 0.01d / (rowColCount + 2);
    }

    private double getCellHeight()
    {
        return gameGrid.getHeight() * (100 - 2 * this.sidePercent) * 0.01d / (rowColCount + 2);
    }

    private void setGridFrame(int middleColumns)
    {
        double middlePercent = (100 - 2 * sidePercent) / (double)middleColumns;

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(sidePercent);
        gameGrid.getColumnConstraints().add(c1);

        RowConstraints r1 = new RowConstraints();
        r1.setPercentHeight(sidePercent);
        gameGrid.getRowConstraints().add(r1);

        for(int i = 0; i < middleColumns; ++ i)
        {
            ColumnConstraints c = new ColumnConstraints();
            c.setPercentWidth(middlePercent);
            gameGrid.getColumnConstraints().add(c);

            RowConstraints r = new RowConstraints();
            r.setPercentHeight(middlePercent);
            gameGrid.getRowConstraints().add(r);
        }

        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(sidePercent);
        gameGrid.getColumnConstraints().add(c2);

        RowConstraints r2 = new RowConstraints();
        r2.setPercentHeight(sidePercent);
        gameGrid.getRowConstraints().add(r2);
    }

    private void selectCell(int col, int row)
    {
        if(selectedRow == row && selectedCol == col)
            return;

        if(selectedRow != -1 && selectedCol != -1) {
            for (int i = 0; i < rowColCount; ++i)
                labels[i][selectedRow].setBackground(null);
            for (int i = 0; i < rowColCount; ++i)
                labels[selectedCol][i].setBackground(null);
        }

        Background redBackground = new Background(new BackgroundFill(Color.RED, new CornerRadii(0), null));

        for(int i = 0; i < rowColCount; ++ i)
            labels[i][row].setBackground(redBackground);
        for(int i = 0; i < rowColCount; ++ i)
            labels[col][i].setBackground(redBackground);

        selectedCol = col;
        selectedRow = row;
    }

    private void gameGridMouseClick(MouseEvent e)
    {
        columnFor:
        for(int col = 0; col < this.rowColCount; ++ col)
        {
            for(int row = 0; row < this.rowColCount; ++ row)
            {
                Bounds cellBounds = gameGrid.getCellBounds(col + 2, row + 2);
                if(cellBounds.contains(new Point2D(e.getSceneX(), e.getSceneY())))
                {
                    selectCell(col, row);
                    break columnFor;
                }
            }
        }
    }
}
