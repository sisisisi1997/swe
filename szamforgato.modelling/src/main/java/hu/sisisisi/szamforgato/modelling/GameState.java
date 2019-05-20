package hu.sisisisi.szamforgato.modelling;

import java.util.Random;


/**
 * Az osztály egy játékállapotot reprezentál, és alkalmas annak manipulálására.
 * Egy játékállapot egy olyan NxN-es tömb, melynek minden eleme egy 1 és N^2 közötti szám, mely a tömbben egyedi.
 */
public class GameState
{
    private int[][] states;
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
        return states[column][row];
    }

    /**
     * A metódus inicializál egy játékállapotot, mely a megadott méretű lesz.
     * A létrehozott állapot véletlenszerűen megkevert, de mindenképpen megoldható.
     * @param rowsColumns A pálya szélessége és magassága.
     */
    public GameState(int rowsColumns)
    {
        this.size = rowsColumns;
        this.states = new int[rowsColumns][rowsColumns];
        for(int row = 0; row < rowsColumns; ++ row)
        {
            for(int column = 0; column < rowsColumns; ++ column)
            {
                this.states[column][row] = (rowsColumns * row) + column + 1;
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
                int firstRow = states[rowOrCol][0];
                for(int row = 1; row < this.size; ++ row)
                {
                    states[rowOrCol][row - 1] = states[rowOrCol][row];
                }
                states[rowOrCol][this.size - 1] = firstRow;
                break;
            case Down:
                int lastRow = states[rowOrCol][this.size - 1];
                for(int row = this.size - 1; row > 0; -- row)
                {
                    states[rowOrCol][row] = states[rowOrCol][row - 1];
                }
                states[rowOrCol][0] = lastRow;
                break;
            case Left:
                int firstCol = states[0][rowOrCol];
                for(int col = 1; col < this.size; ++ col)
                {
                    states[col - 1][rowOrCol] = states[col][rowOrCol];
                }
                states[this.size - 1][rowOrCol] = firstCol;
                break;
            case Right:
                int lastCol = states[this.size - 1][rowOrCol];
                for(int col = this.size - 1; col > 0; -- col)
                {
                    states[col][rowOrCol] = states[col - 1][rowOrCol];
                }
                states[0][rowOrCol] = lastCol;
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
                if(this.states[col][row] != (this.size * row) + col + 1)
                {
                    return false;
                }
            }
        }

        return true;
    }
}