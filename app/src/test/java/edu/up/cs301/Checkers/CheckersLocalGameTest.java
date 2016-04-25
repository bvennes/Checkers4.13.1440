package edu.up.cs301.Checkers;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by vennes19 on 4/24/2016.
 */
public class CheckersLocalGameTest {

    @Test
    public void testCheckMove() throws Exception {
        CheckersLocalGame local = new CheckersLocalGame();
        local.checkersState.playerTurn = 0;

        assertEquals("Check Move Failure", true, local.checkMove(2, 4, 3, 5));
        assertEquals("Check Move Should Fail", false, local.checkMove(1, 5, 0, 0));
    }

    @Test
    public void testCheckJump() throws Exception {
        CheckersLocalGame local = new CheckersLocalGame();
        local.checkersState.playerTurn = 1;
        local.checkersState.selectPiece(1, 0, 0);
        local.checkersState.movePiece(1, 4, 4);
        local.checkersState.playerTurn = 0;
        assertEquals("Check Jump Failure", true, local.checkJump(5, 3, 3, 5));
        assertEquals("Check Jump Failure???", false, local.checkJump(5, 4, 3, 5));
    }
}