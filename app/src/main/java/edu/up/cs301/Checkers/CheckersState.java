package edu.up.cs301.Checkers;

import android.util.Log;

import java.io.Serializable;

import edu.up.cs301.game.infoMsg.GameState;

/**
 *
 * Class representing a game of checkers including current turn, the configuration of the board,
 * etc.,
 *
 * @author Branden Vennes
 * @author Brandon Sit
 * @author Dominic Ferrari
 * @author Sean Tollisen
 */
public class CheckersState extends GameState implements Serializable{

    // to satisfy Serializable interface
    private static final long serialVersionUID = 555777888461316L;

    // checkers on the board -- 0 is null -- 1 is player 1 pieces-- 2 is player 2 pieces --
    // -- 3 is player 1 king -- 4 is player 2 king --
    int[][] board;

    // current player's turn
    int playerTurn;

    // current selected piece
    int[] selectedPiece;

    // required follow up jump piece coordinates
    int[] requiredPiece;

    boolean jumpAvailable;

    /**
     * main constructor -> initializes a checker board and necessary variables
     */
    public CheckersState() {
        super();
        // initialize board size
        board = new int[8][8];

        requiredPiece = new int[2];

        // initialize the requiredPiece
        requiredPiece[0] = -1;
        requiredPiece[1] = -1;

        // fill board with 0s, 1s, and 2s
        for(int i=0;i<3;i++)//top 3 rows
        {
            // even row
            if(i%2==0) {for(int j=0;j<7;j+=2) {board[j][i]=2;}}
            // odd row
            else {for(int j=1;j<8;j+=2) {board[j][i]=2;}}
        }

        for(int i=3;i<5;i++)//middle 2 rows
        {
            // even row
            if(i%2==0) {for(int j=0;j<6;j+=2) {board[j][i]=0;}}
            // odd row
            else {for(int j=1;j<6;j+=2) {board[j][i]=0;}}
        }

        for(int i=5;i<8;i++)//bottom 3 rows
        {
            // even row
            if(i%2==0) {for(int j=0;j<7;j+=2) {board[j][i]=1;}}
            // odd row
            else {for(int j=1;j<8;j+=2) {board[j][i]=1;}}
        }
        playerTurn = 0;
        selectedPiece = new int[2];
        selectedPiece[0]=-1;
        selectedPiece[1]=-1;
        jumpAvailable = false;
    }//CheckersState

    /**
     * deep copy constructor
     * @param state
     */
    public CheckersState(CheckersState state) {
        super();

        this.board = new int[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i][j] = state.getBoard()[i][j];
            }
        }

        this.requiredPiece = new int[2];

        this.selectedPiece = new int[2];

        this.playerTurn = state.getPlayerTurn();

        this.requiredPiece[0] = state.getRequiredPiece()[0];
        this.requiredPiece[1] = state.getRequiredPiece()[1];

        this.selectedPiece[0] = state.getSelectedPiece()[0];
        this.selectedPiece[1] = state.getSelectedPiece()[1];

        jumpAvailable = state.getJumpAvailable();
    }//CheckersState

    /**
     *
     * @param player - current player taking action
     * @param x - horizantal coordinate on board
     * @param y - vertical coordiante on board
     * @return success in selecting piece
     */
    public boolean selectPiece(int player, int x, int y) {
        if (player == playerTurn) {
            if (x<8&&x>-1&&y<8&&y>-1) {
                if (board[x][y] == player + 1 || board[x][y] == player + 3) {
                    selectedPiece[0] = x;
                    selectedPiece[1] = y;
                    return true;
                } else {
                    return false;
                }
            }
            else if (x==-1&&y==-1){
                selectedPiece[0] = x;
                selectedPiece[1] = y;
                return true;
            }
        }
        return false;
    }//selectPiece

    /**
     * make a move with a piece
     * @param player - current player taking action
     * @param xFin - new horizantal coord
     * @param yFin - new vertical coord
     * @return success in moving piece
     */
    public boolean movePiece(int player, int xFin, int yFin) {
        if (board[selectedPiece[0]][selectedPiece[1]]==1){
              if (player == playerTurn) {
                  board[xFin][yFin] = 1;
                  board[selectedPiece[0]][selectedPiece[1]] = 0;
                  selectedPiece[0] = -1;
                  selectedPiece[1] = -1;
                  return true;
              }
        }
        if (board[selectedPiece[0]][selectedPiece[1]]==2){
            if (player == playerTurn) {
                board[xFin][yFin] = 2;
                board[selectedPiece[0]][selectedPiece[1]] = 0;
                selectedPiece[0] = -1;
                selectedPiece[1] = -1;
                return true;
            }
        }
        if (board[selectedPiece[0]][selectedPiece[1]]==3){
            if (player == playerTurn) {
                board[xFin][yFin] = 3;
                board[selectedPiece[0]][selectedPiece[1]] = 0;
                selectedPiece[0] = -1;
                selectedPiece[1] = -1;
                return true;
            }
        }
        if (board[selectedPiece[0]][selectedPiece[1]]==4){
            if (player == playerTurn) {
                board[xFin][yFin] = 4;
                board[selectedPiece[0]][selectedPiece[1]] = 0;
                selectedPiece[0] = -1;
                selectedPiece[1] = -1;
                return true;
            }
        }
        return false;
    }//movePiece

    public void nextTurn() {
        for (int j=0;j<8;j++) {
            if (board[j][0] == 1) {
                board[j][0] = 3;
            }
        }
        for (int j=0;j<8;j++){
            if (board[j][7]==2){
                board[j][7]=4;
            }
        }
        if (playerTurn == 1) {
            playerTurn = 0;
        }
        else {
            playerTurn = 1;
        }
    }//nextTurn

    public boolean removePiece(int playerID, int x, int y) {
        if (playerID==0) {
            if (board[x][y] == 2||board[x][y] == 4) {
                board[x][y] = 0;
                return true;
            }
        }
        if (playerID==1){
            if (board[x][y] == 1||board[x][y] == 3){
                board[x][y] = 0;
            }
        }
        return false;
    }

    public int[][] getBoard() {
        return board;
    }//getBoard

    public int getPlayerTurn() {
        return playerTurn;
    }//getPlayerTurn

    public int[] getSelectedPiece() {
        return selectedPiece;
    }//getSelectedPiece

    public boolean getJumpAvailable() {
        return jumpAvailable;
    }

    public int[] getRequiredPiece() { return requiredPiece; }//getRequiredPiece

    public void setJumpAvailable(boolean bool) {jumpAvailable = bool;}//setJumpAvailable

    public void setRequiredPiece(int[] coords){ requiredPiece = coords;}//setRequiredPiece
}
