package hu.sisisisi.szamforgato.controller;

/**
 * Ez az interface tartalmazza azokat a metódusokat, melyek szükségesek a vezérlő számára a nézetek
 * megfelelő frissítéséhez.
 */
public interface IGameView
{
    /**
     * Egy megadott cella tartalmát frissíti.
     * @param col A cella X koordinátája.
     * @param row A cella Y koordinátája.
     * @param value A cella új értéke.
     */
    void updateCell(int col, int row, int value);

    /**
     * A megadott méretben elkészíti a nézetnek megfelelő táblázatot.
     * @param size A megadott méret.
     */
    void createGameTable(int size);

    /**
     * Kiválasztja, azaz kiemeli a megadott koordinátákon lévő cellát.
     * @param col A cella X koordinátája.
     * @param row A cella Y koordinátája.
     */
    void selectCell(int col, int row);

    /**
     * Megjeleníti a "nyertél" üzenetet.
     */
    void displayWin();

    /**
     * Az eddig megtett lépések számát frissíti, ezzel a játékos számára is láthatóvá teszi.
     * @param count A lépések új száma.
     */
    void updateStepCount(int count);
}
