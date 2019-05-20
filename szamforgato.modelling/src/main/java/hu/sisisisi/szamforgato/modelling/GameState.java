package hu.sisisisi.szamforgato.modelling;

import java.util.Random;


/**
 * Az osztály egy játékállapotot reprezentál, és alkalmas annak manipulálására.
 * Egy játékállapot egy olyan NxN-es tömb, melynek minden eleme egy 1 és N^2 közötti szám, mely a tömbben egyedi.
 */
@SuppressWarnings("ManualArrayCopy")
public class GameState
{
    private int[][] state;
    private int size;

    /**
     * Ez a metódus visszatér a tábla méretével.
     * @return A tábla mérete.
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Ez a getter metódus visszaadja a megadott koordinátán fellelhető számot.
     * @param column A koordináta X eleme.
     * @param row A koordináta Y eleme.
     * @return A szám, mely a {@code GameState} példány által tárolt játékállapotban a megadott (X, Y) koordinátán van.
     */
    public int getNumberAt(int column, int row)
    {
        return state[column][row];
    }

    public int[][] getState()
    {
        return this.state;
    }

    /**
     * A metódus inicializál egy játékállapotot, mely a megadott méretű lesz.
     * A létrehozott állapot véletlenszerűen megkevert, de mindenképpen megoldható.
     * @param rowsColumns A pálya szélessége és magassága.
     */
    public GameState(int rowsColumns)
    {
        this.size = rowsColumns;
        this.state = new int[rowsColumns][rowsColumns];
        for(int row = 0; row < rowsColumns; ++ row)
        {
            for(int column = 0; column < rowsColumns; ++ column)
            {
                this.state[column][row] = (rowsColumns * row) + column + 1;
            }
        }

        // Azért ezzel a módszerrel hozzuk létre a kezdeti pályaállapotot, mert ha csak minden helyre betennénk
        // egy véletlenszerű számot az eddig fel nem használtak közül, nem biztos, hogy megoldható lenne a játék.
        Random rn = new Random();
        Direction[] directions = new Direction[]
                {
                        Direction.Left,
                        Direction.Right,
                        Direction.Up,
                        Direction.Down
                };

        //for(int i = 0, max = 1; i < max; ++ i)
        //{
        this.modifyState(rn.nextInt(this.size), directions[rn.nextInt(directions.length)]);
        //}
    }

    /**
     * Ez a metódus valamelyik oszlopot vagy sort eggyel elcsúsztatja a megadott irányba úgy,
     * hogy a játékállapotból kikerülő elem viszsakerüljön a pálya másik végén.
     * @param rowOrCol A módosítani kívánt sor vagy oszlop 0-tól kezdődő indexű sorszáma.
     * @param direction Az irány, amelyikbe csúsztatni szeretnénk.
     */
    public void modifyState(int rowOrCol, Direction direction)
    {
        switch(direction)
        {
            case Up:
                int firstRow = state[rowOrCol][0];
                for(int row = 1; row < this.size; ++ row)
                {
                    state[rowOrCol][row - 1] = state[rowOrCol][row];
                }
                state[rowOrCol][this.size - 1] = firstRow;
                break;
            case Down:
                int lastRow = state[rowOrCol][this.size - 1];
                for(int row = this.size - 1; row > 0; -- row)
                {
                    state[rowOrCol][row] = state[rowOrCol][row - 1];
                }
                state[rowOrCol][0] = lastRow;
                break;
            case Left:
                int firstCol = state[0][rowOrCol];
                for(int col = 1; col < this.size; ++ col)
                {
                    state[col - 1][rowOrCol] = state[col][rowOrCol];
                }
                state[this.size - 1][rowOrCol] = firstCol;
                break;
            case Right:
                int lastCol = state[this.size - 1][rowOrCol];
                for(int col = this.size - 1; col > 0; -- col)
                {
                    state[col][rowOrCol] = state[col - 1][rowOrCol];
                }
                state[0][rowOrCol] = lastCol;
                break;
        }
    }

    /**
     * Megállapítja, hogy a jelenlegi játékállapot nyerő állapot-e, vagy nem.
     * A nyerő állapot azt jelenti, hogy balról jobbra, majd fentről lefelé a számok egyesével növekednek.
     * @return Egy boolean változó, mely jelzi, hogy ebben az állapotban van-e a játékállapot.
     */
    public boolean isWinningState()
    {
        for(int col = 0; col < this.size; ++ col)
        {
            for(int row = 0; row < this.size; ++ row)
            {
                if(this.state[col][row] != (this.size * row) + col + 1)
                {
                    return false;
                }
            }
        }

        return true;
    }
}