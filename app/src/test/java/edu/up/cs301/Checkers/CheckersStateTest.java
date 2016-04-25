package edu.up.cs301.Checkers;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Branden Vennes
 * @author Brandon Sit
 * @author Dominic Ferrari
 * @author Sean Tollisen
 */
public class CheckersStateTest {

    @Test
    public void testSelectPiece() throws Exception {
        CheckersState state = new CheckersState();
        state.selectPiece(0, 0, 6);
        assertEquals("Selection Error 1", 0, state.getSelectedPiece()[0]);
        assertEquals("Selection Error 2", 6, state.getSelectedPiece()[1]);
    }

    @Test
    public void testMovePiece() throws Exception {
        CheckersState state = new CheckersState();
        state.selectPiece(0, 1, 5);
        state.movePiece(0, 0, 4);
        assertEquals("Move Error", 1, state.getBoard()[0][4]);
    }

    @Test
    public void testNextTurn() throws Exception {
        CheckersState state = new CheckersState();
        state.nextTurn();
        assertEquals("New Turn Error", 1, state.getPlayerTurn());
    }

    @Test
    public void testRemovePiece() throws Exception {
        CheckersState state = new CheckersState();
        state.removePiece(1, 1, 5);
        assertEquals("Removal Error", 0, state.getBoard()[1][5]);
    }
}