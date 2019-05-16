package hu.sisisisi.szamforgato.views;

import hu.sisisisi.szamforgato.GameController;
import hu.sisisisi.szamforgato.IGameView;
import hu.sisisisi.szamforgato.model.Direction;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameView implements IGameView
{
    private static Logger logger = LoggerFactory.getLogger(GameView.class);

    @FXML
    private GridPane gameGrid;

    private Label[][] labels;
    private Label stepCounter;

    private int rowColCount,
                sidePercent = 30;

    private ImageView leftImage,
                      rightImage,
                      upImage,
                      downImage;

    public void initialize()
    {
        try
        {
            GameController.getInstance().setGameView(this);
        }
        catch(Exception ex)
        {
            logger.error("A game view megjelenítése sikertelen.");
        }

        gameGrid.setOnMouseClicked(this::gameGridMouseClick);
    }

    private boolean addArrows()
    {
        try
        {
            ClassLoader l = GameView.class.getClassLoader();
            leftImage = new ImageView(new Image(l.getResource("Left.png").openStream()));
            rightImage = new ImageView(new Image(l.getResource("Right.png").openStream()));
            upImage = new ImageView(new Image(l.getResource("Up.png").openStream()));
            downImage = new ImageView(new Image(l.getResource("Down.png").openStream()));
        }
        catch (Exception e)
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


        setArrowSize();

        gameGrid.add(leftImage, 1, 2, 1, rowColCount);
        gameGrid.add(upImage, 2, 1, rowColCount, 1);
        gameGrid.add(rightImage, rowColCount + 2, 2, 1, rowColCount);
        gameGrid.add(downImage, 2, rowColCount + 2, rowColCount, 1);

        gameGrid.heightProperty().addListener((ObservableValue<? extends Number> observableValue, Number number, Number t1) ->
            setArrowSize());
        gameGrid.widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number number, Number t1) ->
            setArrowSize());

        leftImage.setOnMouseClicked((MouseEvent  -> GameController.getInstance().userInputReceived(Direction.Left)));
        rightImage.setOnMouseClicked((mouseEvent -> GameController.getInstance().userInputReceived(Direction.Right)));
        upImage.setOnMouseClicked((mouseEvent    -> GameController.getInstance().userInputReceived(Direction.Up)));
        downImage.setOnMouseClicked((mouseEvent  -> GameController.getInstance().userInputReceived(Direction.Down)));

        return true;
    }

    public void updateCell(int col, int row, int value)
    {
        labels[col][row].setText(Integer.toString(value));
    }

    private void setArrowSize()
    {
        double w1 = this.getCellWidth() * 0.95,
               h1 = this.getCellHeight() * rowColCount * 0.8,
               w2 = this.getCellWidth() * rowColCount * 0.8,
               h2 = this.getCellHeight() * 0.95;

        leftImage.setFitWidth(w1);
        leftImage.setFitHeight(h1);

        upImage.setFitWidth(w2);
        upImage.setFitHeight(h2);

        rightImage.setFitWidth(w1);
        rightImage.setFitHeight(h1);

        downImage.setFitWidth(w2);
        downImage.setFitHeight(h2);
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

    public void createGameTable(int size)
    {
        this.rowColCount = size;

        gameGrid.getChildren().clear();
        gameGrid.getColumnConstraints().clear();
        gameGrid.getRowConstraints().clear();

        labels = new Label[rowColCount][rowColCount];
        logger.debug("Cleared previous game");

        setGridFrame(rowColCount + 2);
        logger.debug("set frame");
        if(!addArrows())
        {
            return;
        }
        logger.debug("added arrows");

        for(int column = 0; column < rowColCount; ++ column)
        {
            for(int row = 0; row < rowColCount; row ++)
            {
                Label l = new Label();
                gameGrid.add(l, column + 2, row + 2);
                GridPane.setHalignment(l, HPos.CENTER);
                labels[column][row] = l;
            }
        }
        logger.debug("set labels");

        stepCounter = new Label();
        this.updateStepCount(0); // hozzáadhatnánk a szöveget közvetlenül is, de úgy két helyen kellene megváltoztatni a formátumot változtatás esetén, ami bugok forrása
        GridPane.setValignment(stepCounter, VPos.TOP);
        gameGrid.add(stepCounter, 2, rowColCount + 3, rowColCount, 1);
    }

    public void selectCell(int col, int row)
    {
        Background redBackground = new Background(new BackgroundFill(Color.RED, new CornerRadii(0), null));
        for(int currentCol = 0; currentCol < rowColCount; ++ currentCol)
        {
            for(int currentRow = 0; currentRow < rowColCount; ++ currentRow)
            {
                if(currentCol == col || currentRow == row)
                {
                    labels[currentCol][currentRow].setBackground(redBackground);
                }
                else
                {
                    labels[currentCol][currentRow].setBackground(null);
                }
            }
        }
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
                    GameController.getInstance().updateSelection(col, row);
                    selectCell(col, row);
                    break columnFor;
                }
            }
        }
    }

    public void displayWin()
    {
        gameGrid.getChildren().forEach(e -> e.setVisible(false));

        Label youWinText = new Label("Gratulálok! Nyertél!");
        youWinText.setFont(new Font(30));
        youWinText.setTextFill(Color.DARKBLUE);
        GridPane.setValignment(youWinText, VPos.BOTTOM);
        GridPane.setHalignment(youWinText, HPos.CENTER);
        gameGrid.add(youWinText, 1, 0, rowColCount + 2, 1);

        Button retryButton = new Button("Új játék");
        retryButton.setMaxWidth(Double.POSITIVE_INFINITY);
        GridPane.setFillWidth(retryButton, true);
        gameGrid.add(retryButton, 1, 1, rowColCount + 2, rowColCount + 2);

        retryButton.setOnMouseClicked((mouseEvent ->
        {
            GameController.getInstance().createGameState(rowColCount);
        }));
    }

    public void updateStepCount(int count)
    {
        stepCounter.setText("Lépések száma: " + count);
    }
}
