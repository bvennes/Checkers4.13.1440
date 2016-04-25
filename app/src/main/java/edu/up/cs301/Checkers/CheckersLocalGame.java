package edu.up.cs301.Checkers;

import android.graphics.Canvas;
import android.util.Log;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Class representing the local game and controlling the transmission
 * of information to the local GameState
 *
 * @author Branden Vennes
 * @author Brandon Sit
 * @author Dominic Ferrari
 * @author Sean Tollisen
 */

public class CheckersLocalGame extends LocalGame implements CheckersGame {

    CheckersState checkersState;

    boolean compExists;

    public CheckersLocalGame() {
        super();
        checkersState = new CheckersState();
    }

    // Sends an updated GameState to player p
    // @param p the player to be sent the GameState
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        checkIfGameOver();
        CheckersState newCopy = new CheckersState(checkersState);
        for (GamePlayer j : players) {
            if (j instanceof CheckersComputerPlayer) {
                compExists = true;
            }
        }
        if (compExists) {
            p.sendInfo(checkersState);
            return;
        }
        else if (p instanceof CheckersHumanPlayer) {
            if (((CheckersHumanPlayer) p).getNum() == 1) {
                //newCopy.spinBoard();
            }
        }
        p.sendInfo(newCopy);
    }//sendUpdatedStateTo

    // Returns a boolean representing whether or not the player of playerIdx can make moves
    // @param playerIdx ID of the player in question
    @Override
    protected boolean canMove(int playerIdx) {
        if (playerIdx == checkersState.getPlayerTurn()) {
            return true;
        }
        return false;
    }//canMove

    // Returns a string representing whether or not the game is over
    @Override
    protected String checkIfGameOver() {
        // current checkerboard
        int[][] checkersBoard = checkersState.getBoard();

        // number of checkers of players 1 and 2
        int player1Count = 0;
        int player2Count = 0;

        // check each space of the checkerboard for each player's pieces
        for (int i = 0; i < checkersBoard.length; i++) {
            for (int j = 0; j < checkersBoard[i].length; j++) {
                if (checkersBoard[i][j] == 1 || checkersBoard[i][j] == 3) {
                    player1Count++;
                } else if (checkersBoard[i][j] == 2 || checkersBoard[i][j] == 4) {
                    player2Count++;
                }
            }
        }
        if (player1Count == 0) {
            // player 2 wins
            return playerNames[1] + " has won!";
        } else if (player2Count == 0) {
            // player 1 wins
            return playerNames[0] + " has won!";
        }
        if(!checkForMoves()&&!checkForJumps()){
            if (checkersState.getPlayerTurn() == 0) {
                return playerNames[1] + " wins because there are no available moves for " + playerNames[0] + "!";
            }
            else {
                return playerNames[0] + " wins because there are no available moves for " + playerNames[1] + "!";
            }
        }
        return null;
    }//checkIfGameOver

    // Attempts an action, and returns a boolean representing whether or not the action was successful
    // @param action Action to execute
    @Override
    protected boolean makeMove(GameAction action) {

        int playerID = getPlayerIdx(action.getPlayer());

        Log.i("posting", playerNames[playerID] + " is trying to post");

        checkersState.setJumpAvailable(false);
        if (!canMove(playerID)) {
            return false;
        }
        // check for move action
        if (action instanceof CheckersMoveAction) {
            CheckersMoveAction moveAction = (CheckersMoveAction) action;
            if(!checkForJumps()) {
                checkersState.movePiece(playerID, moveAction.getJumpX(), moveAction.getJumpY());
                checkersState.nextTurn();
            }
        }
        // check for jump action
        else if (action instanceof CheckersJumpAction) {
            int[] clear = new int[2];
            clear[0] = -1;
            clear[1] = -1;
            checkersState.setRequiredPiece(clear);
            CheckersJumpAction jumpAction = (CheckersJumpAction) action;
            checkersState.removePiece(playerID, jumpAction.getJumpedX(), jumpAction.getJumpedY());
            checkersState.movePiece(playerID, jumpAction.getJumpX(), jumpAction.getJumpY());
            if (checkPieceForJumps(jumpAction.getJumpX(), jumpAction.getJumpY())){
                int[] reqPiece = new int[2];
                reqPiece[0] = jumpAction.getJumpX();
                reqPiece[1] = jumpAction.getJumpY();
                checkersState.setRequiredPiece(reqPiece);
                checkersState.selectPiece(checkersState.getPlayerTurn(), jumpAction.getJumpX(), jumpAction.getJumpY());
            }
            else {
                checkersState.nextTurn();
            }
        }
        // check for select action
        else if (action instanceof CheckersSelectAction) {
            CheckersSelectAction selectAction = (CheckersSelectAction) action;
            checkersState.selectPiece(playerID, selectAction.getJumpX(), selectAction.getJumpY());
        }
        // send updated state to both players
        for (GamePlayer p : players) {
            this.sendUpdatedStateTo(p);
        }
        return true;
    }//makeMove

    // Checks to see if move is valid, otherwise return false
    // @param X the x coordinate on the board of the space to be moved to
    // @param Y the y coordinate on the board of the space to be moved to
    // @param PieceX the X coordinate of the piece being checked
    // @param PieceY the Y coordinate of the piece being checked
    public boolean checkMove(int X, int Y, int PieceX, int PieceY) {
        if (checkersState.jumpAvailable||X<0||X>7||Y<0||Y>7) {
            return false;
        }
        int[][] currentBoard = checkersState.getBoard();
        int currentPlayer = checkersState.getPlayerTurn();
        if (currentBoard[X][Y] == 0) {
            if (currentBoard[PieceX][PieceY] == currentPlayer + 3) {
                if (PieceX - X == 1 || PieceX - X == -1) {
                    if ((PieceY - Y == 1 || PieceY - Y == -1)) {
                        return true;
                    }
                }
            } else {
                if (currentBoard[PieceX][PieceY] == currentPlayer + 1) {
                    if (PieceX - X == 1 || PieceX - X == -1) {
                        if (PieceY - Y == 1&&currentPlayer==0) {
                            return true;
                        }
                        if (PieceY - Y == -1&&currentPlayer==1) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }//checkMove


    // Checks to see if jump is valid, otherwise return false
    // @param X the x coordinate on the board of the space to be jumped to
    // @param Y the y coordinate on the board of the space to be jumped to
    // @param PieceX the X coordinate of the piece being checked
    // @param PieceY the Y coordinate of the piece being checked
    public boolean checkJump(int X, int Y, int PieceX, int PieceY){
        if (X<0||X>7||Y<0||Y>7){
            return false;
        }
        int currentPlayer = checkersState.getPlayerTurn();
        int[][] currentBoard = checkersState.getBoard();
        if (currentBoard[PieceX][PieceY]==currentPlayer+3){
            if (PieceX-X==-2){
                if (PieceY-Y==-2){
                    if ((currentBoard[X-1][Y-1]!=currentPlayer+1&&currentBoard[X-1][Y-1]!=currentPlayer+3&&currentBoard[X-1][Y-1]!=0)&&currentBoard[X][Y]==0){
                        return true;
                    }
                }
                if (PieceY-Y==2){
                    if ((currentBoard[X-1][Y+1]!=currentPlayer+1&&currentBoard[X-1][Y+1]!=currentPlayer+3&&currentBoard[X-1][Y+1]!=0)&&currentBoard[X][Y]==0){
                        return true;
                    }
                }
            }
            if (PieceX-X==2){
                if (PieceY-Y==-2){
                    if ((currentBoard[X+1][Y-1]!=currentPlayer+1&&currentBoard[X+1][Y-1]!=currentPlayer+3&&currentBoard[X+1][Y-1]!=0)&&currentBoard[X][Y]==0){
                        return true;
                    }
                }
                if (PieceY-Y==2){
                    if ((currentBoard[X+1][Y+1]!=currentPlayer+1&&currentBoard[X+1][Y+1]!=currentPlayer+3&&currentBoard[X+1][Y+1]!=0)&&currentBoard[X][Y]==0){
                        return true;
                    }
                }
            }
        }
        else if (currentBoard[PieceX][PieceY]==currentPlayer+1){
            if (PieceX-X==-2){
                if (PieceY-Y==-2&&currentPlayer==1){
                    if ((currentBoard[X-1][Y-1]!=currentPlayer+1&&currentBoard[X-1][Y-1]!=currentPlayer+3&&currentBoard[X-1][Y-1]!=0)&&currentBoard[X][Y]==0){
                        return true;
                    }
                }
                if (PieceY-Y==2&&currentPlayer==0){
                    if ((currentBoard[X-1][Y+1]!=currentPlayer+1&&currentBoard[X-1][Y+1]!=currentPlayer+3&&currentBoard[X-1][Y+1]!=0)&&currentBoard[X][Y]==0){
                        return true;
                    }
                }
            }
            if (PieceX-X==2){
                if (PieceY-Y==-2&&currentPlayer==1){
                    if ((currentBoard[X+1][Y-1]!=currentPlayer+1&&currentBoard[X+1][Y-1]!=currentPlayer+3&&currentBoard[X+1][Y-1]!=0)&&currentBoard[X][Y]==0){
                        return true;
                    }
                }
                if (PieceY-Y==2&&currentPlayer==0){
                    if ((currentBoard[X+1][Y+1]!=currentPlayer+1&&currentBoard[X+1][Y+1]!=currentPlayer+3&&currentBoard[X+1][Y+1]!=0)&&currentBoard[X][Y]==0){
                        return true;
                    }
                }
            }
        }
        return false;
    }//jumpPiece


    //Checks the whole board for if the current player has a move
    public boolean checkForMoves() {
        for (int r=0;r<8;r++){
            for (int j=0;j<8;j++){
                if (checkMove(r-1,j+1,r,j)||checkMove(r+1,j+1,r,j)||checkMove(r-1,j-1,r,j)||checkMove(r+1,j-1,r,j)){
                    return true;
                }
            }
        }
        return false;
    }

    //Checks a specific piece for available jumps
    //@param X the X position of the piece to be checked
    //@param Y the Y position of the piece to be checked
    public boolean checkPieceForJumps(int X, int Y){
        if (checkJump(X-2,Y+2,X,Y)||checkJump(X+2,Y+2,X,Y)||checkJump(X-2,Y-2,X,Y)||checkJump(X+2,Y-2,X,Y)){
            Log.i("Jump found", "Jump available at" + X + ", "+Y);
            return true;
        }
        return false;
    }


    //Checks the whole board for if the current player has a jump
    public boolean checkForJumps() {
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if (checkJump(i-2,j+2,i,j)||checkJump(i+2,j+2,i,j)||checkJump(i-2,j-2,i,j)||checkJump(i+2,j-2,i,j)){
                    Log.i("Jump found", "Jump available at" + i + ", "+j);
                    checkersState.setJumpAvailable(true);
                    return true;
                }
            }
        }
        return false;
    }
}
