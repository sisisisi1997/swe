package hu.sisisisi.szamforgato;

public interface IGameView
{
    void updateCell(int col, int row, int value);
    void createGameTable(int size);
    void selectCell(int col, int row);
    void displayWin();
    void updateStepCount(int count);
}
