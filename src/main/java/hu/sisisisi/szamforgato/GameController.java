package hu.sisisisi.szamforgato;

import hu.sisisisi.szamforgato.model.Direction;
import hu.sisisisi.szamforgato.model.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Az MVC vezérlő osztály a játék lejátszásához.
 * Kezelni tud bármilyen nézetet, mely implementálja az {@link hu.sisisisi.szamforgato.IGameView} interfészt.
 */
public class GameController
{
    private static GameController instance;
    private static Logger logger = LoggerFactory.getLogger(GameController.class);

    private IGameView gameView;
    private GameState gameState;
    private int selectedRow,
                selectedCol,
                numberOfSteps = 0;
    private String name = "default";

    /**
     * Visszaadja a vezérlő példányát. Ha ez nem létezik, elkészít egy példányt.
     * @return A vezérlő példánya.
     */
    public static GameController getInstance()
    {
        if(instance == null)
            GameController.instance = new GameController();
        return instance;
    }

    private GameController()
    {

    }

    /**
     * Set the name of the player playing this game.
     * @param name The name of the player.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Kijelöli a megadott sort és oszlopot, mely kijelölés a fel, le, jobbra, és balra mozgatás alapja lesz.
     * Ha nincs játékállapot beállítva, semmit nem csinál.
     * Ha a megadott sor és oszlop érvénytelen, nem csinál semmit.
     * Ha létezik nézet, utasítja azt, hogy válassza ki az adott sor és oszlop által meghatározott cellát.
     * @param col A kiválasztandó oszlop.
     * @param row A kiválasztandó sor.
     */
    public void updateSelection(int col, int row)
    {
        this.selectedCol = col;
        this.selectedRow = row;
        if(this.gameView != null)
            this.gameView.selectCell(col, row);
    }

    /**
     * Utasítja a nézetet, hogy frissítse a megadott értékekre a táblázatának celláit.
     * Ha nincs beállítva nézet, ez a metódus semmit nem csinál.
     */
    private void updateGameView()
    {
        if(gameView == null)
            return;

        for(int col = 0; col < this.gameState.getSize(); ++ col)
        {
            for(int row = 0; row < this.gameState.getSize(); ++ row)
            {
                this.gameView.updateCell(col, row, this.gameState.getNumberAt(col, row));
            }
        }
        gameView.updateStepCount(this.numberOfSteps);
    }

    /**
     * Létrehoz egy játékállapotot a megadott táblamérettel.
     * @param size A tábla mérete.
     */
    public void createGameState(int size)
    {
        this.gameState = new GameState(size);
        this.numberOfSteps = 0;
        if(this.gameView != null)
        {
            gameView.createGameTable(this.gameState.getSize());
            this.updateGameView();
            gameView.selectCell(0, 0);
            this.selectedRow = 0;
            this.selectedCol = 0;
        }
        logger.info(size + " méretű tábla beállítva");
    }

    /**
     * Beállítja a nézetet a vezérlő számára.
     * @param view A beállítandó nézet, melyet innentől a vezérlő kezel.
     */
    public void setGameView(IGameView view)
    {
        this.gameView = view;
        if(this.gameState != null)
        {
            gameView.createGameTable(this.gameState.getSize());
            this.updateGameView();
        }
    }

    /**
     * Lekezeli, ha a felhasználó lépteti a játékállapotot, és a tárolt állapotot a lépésnek megfelelően frissíti.
     * Ha van nézet, a lépésnek megfelelően frissíti azt.
     * @param d Az irány (egy {@link hu.sisisisi.szamforgato.model.Direction} példány), melybe a játékos lépett.
     */
    public void userInputReceived(Direction d)
    {
        this.numberOfSteps ++;
        if(d == Direction.Left || d == Direction.Right)
        {
            gameState.modifyState(this.selectedRow, d);
            for(int i = 0; i < this.gameState.getSize() && this.gameView != null; ++ i)
            {
                gameView.updateCell(i, selectedRow, gameState.getNumberAt(i, selectedRow));
            }
        }
        else
        {
            gameState.modifyState(this.selectedCol, d);
            for (int i = 0; i < this.gameState.getSize() && this.gameView != null; ++ i)
            {
                gameView.updateCell(selectedCol, i, gameState.getNumberAt(selectedCol, i));
            }
        }

        if(gameView != null)
        {
            gameView.updateStepCount(numberOfSteps);
            if(gameState.isWinningState())
            {
                ScoreBoardHandler.saveScore(this.gameState.getSize(), this.name, numberOfSteps);
                gameView.displayWin();
            }
        }
    }
}
