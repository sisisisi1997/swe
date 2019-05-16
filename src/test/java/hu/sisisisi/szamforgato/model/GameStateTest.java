package hu.sisisisi.szamforgato.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {
    GameState state = new GameState(5);
    GameState state2 = new GameState(10);

    @Test
    void getSize()
    {
        assertEquals(5, state.getSize());
        assertEquals(10, state2.getSize());
    }

    @Test
    void getNumberAt()
    {

    }

    @Test
    void modifyState()
    {
        int[][] originalState = new int[5][5];
        for(int col = 0; col < state.getSize(); ++ col)
        {
            for(int row = 0; row < state.getSize(); ++ row)
            {
                originalState[col][row] = state.getNumberAt(col, row);
            }
        }

        state.modifyState(2, Direction.Left);
        state.modifyState(4, Direction.Up);

        int szel = originalState[0][2];
        for(int i = 1; i < state.getSize(); ++ i)
        {
            originalState[i - 1][2] = originalState[i][2];
        }
        originalState[state.getSize() - 1][2] = szel;

        szel = originalState[4][0];
        for(int i = 1; i < state.getSize(); ++ i)
        {
            originalState[4][i - 1] = originalState[4][i];
        }
        originalState[4][state.getSize() - 1] = szel;

        boolean equals = true;
        for(int col = 0; col < state.getSize(); ++ col)
        {
            for(int row = 0; row < state.getSize(); ++ row)
            {
                equals &= originalState[col][row] == state.getNumberAt(col, row);
            }
        }
        assertTrue(equals);
    }
}