package hu.sisisisi.szamforgato;

import hu.sisisisi.szamforgato.model.Direction;
import hu.sisisisi.szamforgato.model.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameController
{
    private static GameController instance;
    private static Logger logger = LoggerFactory.getLogger(GameController.class);

    private IGameView gameView;
    private GameState gameState;
    private int selectedRow,
                selectedCol;

    public static GameController getInstance()
    {
        if(instance == null)
            GameController.instance = new GameController();
        return instance;
    }

    private GameController()
    {

    }

    public void updateSelection(int col, int row)
    {
        this.selectedCol = col;
        this.selectedRow = row;
        if(this.gameView != null)
            this.gameView.selectCell(col, row);
    }

    private void updateGameView()
    {
        for(int col = 0; col < this.gameState.getSize(); ++ col)
        {
            for(int row = 0; row < this.gameState.getSize(); ++ row)
            {
                this.gameView.updateCell(col, row, this.gameState.getNumberAt(col, row));
            }
        }
    }

    public void gameStarted(int size)
    {
        this.gameState = new GameState(size);
        if(this.gameView != null)
        {
            gameView.createGameTable(this.gameState.getSize());
            this.updateGameView();
        }
        logger.info(size + " méretű tábla beállítva");
    }

    public void setGameView(IGameView view)
    {
        this.gameView = view;
        if(this.gameState != null)
        {
            logger.debug("Játéknézet frissítése...");
            gameView.createGameTable(this.gameState.getSize());
            this.updateGameView();
            logger.debug("Játéknézet frissítve");
        }
    }

    public void userInputReceived(Direction d)
    {
        if(d == Direction.Left || d == Direction.Right)
        {
            gameState.modifyState(this.selectedRow, d);
            for(int i = 0; i < this.gameState.getSize(); ++ i)
            {
                gameView.updateCell(i, selectedRow, gameState.getNumberAt(i, selectedRow));
            }
        }
        else
        {
            gameState.modifyState(this.selectedCol, d);
            for (int i = 0; i < this.gameState.getSize(); ++ i)
            {
                gameView.updateCell(selectedCol, i, gameState.getNumberAt(selectedCol, i));
            }
        }

        if(gameState.isWinningState())
        {
            gameView.displayWin();
        }
    }
}
