package edu.up.cs301.Checkers;

import android.system.Os;
import android.util.Log;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 *
 * Class representing and controlling one computer player with a name and
 * variable difficulty.
 *
 * @author Branden Vennes
 * @author Brandon Sit
 * @author Dominic Ferrari
 * @author Sean Tollisen
 */
public class CheckersComputerPlayer extends GameComputerPlayer implements CheckersPlayer {

    //Boolean showing if the is difficult or not
    protected boolean difficult;

    // The current checkers state
    protected CheckersState current;

    // Whether or not the computer is ready to check for a move
    protected boolean checkMoves = false;

    // Whether or not the computer has made a move yet
    protected boolean moved = false;

    // Whether or not the computer is currently chain jumping
    protected boolean chaining = false;

    private int selectCount;

    // constructor for GameAction
    //@param name the player who created the action
    //@param smart whether or not the computer is difficult
    public CheckersComputerPlayer(String name, boolean smart){
        super(name);
        difficult = smart;
    }//CheckersComputerPlayer


    // Response to receiving a GameInfo object
    //@param info the GameInfo object received
    @Override
    protected void receiveInfo(GameInfo info) {
        try {
            Thread.sleep(50);
        }
        catch(InterruptedException e){
            //peh
        }
        if (info instanceof CheckersState){
            Log.i("Info Received", "" + ((CheckersState) info).getSelectedPiece()[0] + "," + ((CheckersState) info).getSelectedPiece()[1]);
            current = (CheckersState) info;
            if (current.getPlayerTurn() == this.playerNum) {
                //Right now, difficult or not difficult are the same
                if (!difficult || difficult) {
                    //If it's in the middle of a series of jumps, pick a jump and do it
                    if (current.getRequiredPiece()[0] != -1) {
                        //Assuming that, if there was one chainjump, there will be another
                        chaining = true;
                        if (checkJump(current.getRequiredPiece()[0]-2, current.getRequiredPiece()[1]-2)) {
                            jumpPiece(current.getRequiredPiece()[0]-2, current.getRequiredPiece()[1]-2);
                        } else if (checkJump(current.getRequiredPiece()[0]+2, current.getRequiredPiece()[1]-2)) {
                            jumpPiece(current.getRequiredPiece()[0]+2, current.getRequiredPiece()[1] - 2);
                        } else if (checkJump(current.getRequiredPiece()[0]-2, current.getRequiredPiece()[1]+2)) {
                            jumpPiece(current.getRequiredPiece()[0]-2, current.getRequiredPiece()[1]+2);
                        } else if (checkJump(current.getRequiredPiece()[0]+2, current.getRequiredPiece()[1]+2)) {
                            jumpPiece(current.getRequiredPiece()[0]+2, current.getRequiredPiece()[1]+2);
                        }
                    }

                    //If it's already moved and isn't currently chaining, end it's turn
                    if (moved&&!chaining){
                        game.sendAction(new CheckersEndTurnAction(this));
                        moved = false;
                    }

                    //Gives each direction it could possibly move a 3in4 chance to be selected, checks
                    //moves and jumps in that direction, and takes them if available.
                    if (checkMoves && !difficult){
                        if(Math.random()<=1){
                            if (checkMove(current.getSelectedPiece()[0]-1, current.getSelectedPiece()[1]-1)){
                                checkMove(current.getSelectedPiece()[0]-1, current.getSelectedPiece()[1]-1);
                                movePiece(current.getSelectedPiece()[0]-1, current.getSelectedPiece()[1]-1);
                                moved = true;
                            }
                            if (checkJump(current.getSelectedPiece()[0]-2, current.getSelectedPiece()[1]-2)){
                                checkJump(current.getSelectedPiece()[0]-2, current.getSelectedPiece()[1]-2);
                                jumpPiece(current.getSelectedPiece()[0]-2, current.getSelectedPiece()[1]-2);
                                moved = true;
                            }
                        }
                        if(Math.random()<=1){
                            if (checkMove(current.getSelectedPiece()[0]+1, current.getSelectedPiece()[1]-1)){
                                checkMove(current.getSelectedPiece()[0]+1, current.getSelectedPiece()[1]-1);
                                movePiece(current.getSelectedPiece()[0]+1, current.getSelectedPiece()[1]-1);
                                moved = true;
                            }
                            if (checkJump(current.getSelectedPiece()[0]+2, current.getSelectedPiece()[1]-2)){
                                checkJump(current.getSelectedPiece()[0]+2, current.getSelectedPiece()[1]-2);
                                jumpPiece(current.getSelectedPiece()[0]+2, current.getSelectedPiece()[1]-2);
                                moved = true;
                            }
                        }
                        if(Math.random()<=1){
                            if (checkMove(current.getSelectedPiece()[0]-1, current.getSelectedPiece()[1]+1)){
                                checkMove(current.getSelectedPiece()[0]-1, current.getSelectedPiece()[1]+1);
                                movePiece(current.getSelectedPiece()[0]-1, current.getSelectedPiece()[1]+1);
                                moved = true;
                            }
                            if (checkJump(current.getSelectedPiece()[0]-2, current.getSelectedPiece()[1]+2)){
                                checkJump(current.getSelectedPiece()[0]-2, current.getSelectedPiece()[1]+2);
                                jumpPiece(current.getSelectedPiece()[0]-2, current.getSelectedPiece()[1]+2);
                                moved = true;
                            }
                        }
                        if(Math.random()<=1){
                            if (checkMove(current.getSelectedPiece()[0]+1, current.getSelectedPiece()[1]+1)){
                                checkMove(current.getSelectedPiece()[0]+1, current.getSelectedPiece()[1]+1);
                                movePiece(current.getSelectedPiece()[0]+1, current.getSelectedPiece()[1]+1);
                                moved = true;
                            }
                            if (checkJump(current.getSelectedPiece()[0]+2, current.getSelectedPiece()[1]+2)){
                                checkJump(current.getSelectedPiece()[0]+2, current.getSelectedPiece()[1]+2);
                                jumpPiece(current.getSelectedPiece()[0]+2, current.getSelectedPiece()[1]+2);
                                moved = true;
                            }
                        }
                        checkMoves = false;
                    }
                    // difficult AI
                    else if (checkMoves && difficult) {
                        int mistake = (int) (Math.random() * 100);
                        int mistakeChance = 95;

                        if (checkJump(current.getSelectedPiece()[0] + 2, current.getSelectedPiece()[1] - 2)) {
                            if (!checkDanger(current.getSelectedPiece()[0] + 2, current.getSelectedPiece()[1]-2) || mistake > mistakeChance) {
                                jumpPiece(current.getSelectedPiece()[0] + 2, current.getSelectedPiece()[1] - 2);
                            }
                        }
                        if (checkJump(current.getSelectedPiece()[0] + 2, current.getSelectedPiece()[1] + 2)){
                            if (!checkDanger(current.getSelectedPiece()[0]+2, current.getSelectedPiece()[1]+2)) {
                                jumpPiece(current.getSelectedPiece()[0] + 2, current.getSelectedPiece()[1] + 2);
                            }
                        }
                        if (checkJump(current.getSelectedPiece()[0] - 2, current.getSelectedPiece()[1] - 2)) {
                            if (!checkDanger(current.getSelectedPiece()[0]-2, current.getSelectedPiece()[1]-2) || mistake > mistakeChance) {
                                jumpPiece(current.getSelectedPiece()[0] - 2, current.getSelectedPiece()[1] - 2);
                            }
                        }
                        if (checkJump(current.getSelectedPiece()[0] - 2, current.getSelectedPiece()[1] + 2)) {
                            if (!checkDanger(current.getSelectedPiece()[0]-2, current.getSelectedPiece()[1]+2) || mistake > mistakeChance) {
                                jumpPiece(current.getSelectedPiece()[0] - 2, current.getSelectedPiece()[1] + 2);
                            }
                        }
                        if (checkJump(current.getSelectedPiece()[0] + 2, current.getSelectedPiece()[1] - 2)) {
                            moved = jumpPiece(current.getSelectedPiece()[0] + 2, current.getSelectedPiece()[1] - 2);
                        }
                        if (checkJump(current.getSelectedPiece()[0] + 2, current.getSelectedPiece()[1] + 2)){
                            moved = jumpPiece(current.getSelectedPiece()[0] + 2, current.getSelectedPiece()[1] + 2);
                        }
                        if (checkJump(current.getSelectedPiece()[0] - 2, current.getSelectedPiece()[1] - 2)) {
                            moved = jumpPiece(current.getSelectedPiece()[0] - 2, current.getSelectedPiece()[1] - 2);
                        }
                        if (checkJump(current.getSelectedPiece()[0] - 2, current.getSelectedPiece()[1] + 2)) {
                            moved = jumpPiece(current.getSelectedPiece()[0] - 2, current.getSelectedPiece()[1] + 2);
                        }

                        if (current.getSelectedPiece()[0] == 0 && mistake > mistakeChance) {
                            // do not move this piece
                        }
                        else if (current.selectedPiece[0] < 4) {
                            if (checkMove(current.getSelectedPiece()[0]+1, current.getSelectedPiece()[1] - 1)) {
                                if (!checkDanger(current.getSelectedPiece()[0]+1, current.getSelectedPiece()[1]-1) || mistake > mistakeChance) {
                                    moved = movePiece(current.getSelectedPiece()[0]+1, current.getSelectedPiece()[1] - 1);
                                }
                            }
                            if (checkMove(current.getSelectedPiece()[0]+1, current.getSelectedPiece()[1]+1)){
                                if (!checkDanger(current.getSelectedPiece()[0]+1, current.getSelectedPiece()[1]+1) || mistake > mistakeChance) {
                                    moved = movePiece(current.getSelectedPiece()[0]+1, current.getSelectedPiece()[1]+1);
                                }
                            }
                            if (checkMove(current.getSelectedPiece()[0] - 1, current.getSelectedPiece()[1] - 1)) {
                                if (!checkDanger(current.getSelectedPiece()[0]-1, current.getSelectedPiece()[1]-1) || mistake > mistakeChance) {
                                    moved = movePiece(current.getSelectedPiece()[0] - 1, current.getSelectedPiece()[1] - 1);
                                }
                            }
                            if (checkMove(current.getSelectedPiece()[0]-1, current.getSelectedPiece()[1]+1)){
                                if (!checkDanger(current.getSelectedPiece()[0]-1, current.getSelectedPiece()[1]+1) || mistake > mistakeChance) {
                                    moved = movePiece(current.getSelectedPiece()[0]-1, current.getSelectedPiece()[1]+1);
                                }
                            }
                        }
                        else {
                            if (checkJump(current.getSelectedPiece()[0] - 2, current.getSelectedPiece()[1] - 2)) {
                                if (!checkDanger(current.getSelectedPiece()[0]-2, current.getSelectedPiece()[1]-2) || mistake > mistakeChance) {
                                    moved = jumpPiece(current.getSelectedPiece()[0] - 2, current.getSelectedPiece()[1] - 2);
                                }
                            }
                            if (checkJump(current.getSelectedPiece()[0] - 2, current.getSelectedPiece()[1] + 2)){
                                if (!checkDanger(current.getSelectedPiece()[0]-2, current.getSelectedPiece()[1]+2) || mistake > mistakeChance) {
                                    moved = jumpPiece(current.getSelectedPiece()[0] - 2, current.getSelectedPiece()[1] + 2);
                                }
                            }
                            if (checkJump(current.getSelectedPiece()[0] + 2, current.getSelectedPiece()[1] - 2)) {
                                if (!checkDanger(current.getSelectedPiece()[0] + 2, current.getSelectedPiece()[1]-2) || mistake > mistakeChance) {
                                    moved = jumpPiece(current.getSelectedPiece()[0] + 2, current.getSelectedPiece()[1] - 2);
                                }
                            }
                            if (checkJump(current.getSelectedPiece()[0] + 2, current.getSelectedPiece()[1] + 2)) {
                                if (!checkDanger(current.getSelectedPiece()[0]+2, current.getSelectedPiece()[1]+2) || mistake > mistakeChance) {
                                    moved = jumpPiece(current.getSelectedPiece()[0] + 2, current.getSelectedPiece()[1] + 2);
                                }
                            }
                            if (checkMove(current.getSelectedPiece()[0] - 1, current.getSelectedPiece()[1] - 1)) {
                                if (!checkDanger(current.getSelectedPiece()[0]-1, current.getSelectedPiece()[1]-1) || mistake > mistakeChance) {
                                    moved = movePiece(current.getSelectedPiece()[0] - 1, current.getSelectedPiece()[1] - 1);
                                }
                            }
                            if (checkMove(current.getSelectedPiece()[0]-1, current.getSelectedPiece()[1]+1)){
                                if (!checkDanger(current.getSelectedPiece()[0]-1, current.getSelectedPiece()[1]+1) || mistake > mistakeChance) {
                                    moved = movePiece(current.getSelectedPiece()[0]-1, current.getSelectedPiece()[1]+1);
                                }
                            }
                            if (checkMove(current.getSelectedPiece()[0]+1, current.getSelectedPiece()[1] - 1)) {
                                if (!checkDanger(current.getSelectedPiece()[0]+1, current.getSelectedPiece()[1]-1) || mistake > mistakeChance) {
                                    moved = movePiece(current.getSelectedPiece()[0]+1, current.getSelectedPiece()[1] - 1);
                                }
                            }
                            if (checkMove(current.getSelectedPiece()[0]+1, current.getSelectedPiece()[1]+1)){
                                if (!checkDanger(current.getSelectedPiece()[0]+1, current.getSelectedPiece()[1]+1) || mistake > mistakeChance) {
                                    moved = movePiece(current.getSelectedPiece()[0]+1, current.getSelectedPiece()[1]+1);
                                }
                            }
                        }
                    }

                    //If it hasn't moved yet, start trying to select a piece
                        //Run through a loop of checking all the spaces on the board, giving each
                        //friendly piece it sees a 1in10 chance of being selected
                    boolean Select = true;
                    while (Select) {
                        for (int i = 0; i <= 7; i++) {
                            for (int j = 0; j <= 7; j++) {
                                if (current.getBoard()[i][j] == 2 || current.getBoard()[i][j] == 4) {
                                    if (Math.random() <= .25 ) {
                                        int[] coords = new int[2];
                                        coords[0] = i;
                                        coords[1] = j;
                                        game.sendAction(new CheckersSelectAction(this, coords));
                                        checkMoves = true;
                                        Select = false;
                                        break;
                                    }
                                }
                            }
                            if (Select == false) {
                                break;
                            }
                        }
                    }
                    chaining = false;
                }
            }
        }
    }//receiveInfo


    // Checks to see if move action is valid and send an action, otherwise return
    // false
    // @param X the x coordinate on the board of the space to be moved to
    // @param Y the y coordinate on the board of the space to be moved to
    public boolean movePiece(int X, int Y){
        int[] coords = new int[2];
        coords[0] = X;
        coords[1] = Y;
        int selectedX = current.getSelectedPiece()[0];
        int selectedY = current.getSelectedPiece()[1];
        if (current.getBoard()[selectedX][selectedY]>=3){
            if (current.getBoard()[X][Y]==0){
                if (selectedX-X==1||selectedX-X==-1){
                    if(selectedY-Y==1||selectedY-Y==-1){
                        game.sendAction(new CheckersMoveAction(this, coords));
                    }
                }
            }
        }
        else{
            if (current.getBoard()[X][Y]==0){
                if (selectedX-X==1||selectedX-X==-1){
                    if(selectedY-Y==-1){
                        game.sendAction(new CheckersMoveAction(this, coords));
                    }
                }
            }
        }
        return false;
    }//movePiece


    // Checks to see if move action is valid, otherwise return false
    // @param X the x coordinate on the board of the space to be moved to
    // @param Y the y coordinate on the board of the space to be moved to
    public boolean checkMove(int X, int Y){
        int selectedX = current.getSelectedPiece()[0];
        int selectedY = current.getSelectedPiece()[1];
        if (selectedX == -1 && selectedY == -1) {
            return false;
        }
        if (X<0||X>7||Y<0||Y>7){
            return false;
        }
        if (current.getBoard()[selectedX][selectedY]>=3){
            if (current.getBoard()[X][Y]==0){
                if (selectedX-X==1||selectedX-X==-1){
                    if(selectedY-Y==1||selectedY-Y==-1){
                        return true;
                    }
                }
            }
        }
        else{
            if (current.getBoard()[X][Y]==0){
                if (selectedX-X==1||selectedX-X==-1){
                    if(selectedY-Y==-1){
                        return true;
                    }
                }
            }
        }
        return false;
    }//movePiece


    // Checks if a given jump is possible, and sends the action if so
    // Otherwise, return false
    // @param X The x coordinate of the space to be moved to
    // @param Y The y coordinate of the space to be moved to
    public boolean jumpPiece(int X, int Y){
        if (X<0||X>7||Y<0||Y>7){
            return false;
        }
        int[] coords = new int[4];
        coords[0] = X;
        coords[1] = Y;
        int selectedX = current.getSelectedPiece()[0];
        int selectedY = current.getSelectedPiece()[1];
        if (current.getBoard()[selectedX][selectedY]>=3){
            if (selectedX-X==-2){
                if (selectedY-Y==-2){
                    if (current.getBoard()[X-1][Y-1]==1||current.getBoard()[X-1][Y-1]==3){
                        coords[2] = X-1;
                        coords[3] = Y-1;
                        game.sendAction(new CheckersJumpAction(this, coords));
                    }
                }
                if (selectedY-Y==2){
                    if (current.getBoard()[X-1][Y+1]==1||current.getBoard()[X-1][Y+1]==3){
                        coords[2] = X-1;
                        coords[3] = Y+1;
                        game.sendAction(new CheckersJumpAction(this, coords));
                    }
                }
            }
            if (selectedX-X==2){
                if (selectedY-Y==-2){
                    if (current.getBoard()[X+1][Y-1]==1||current.getBoard()[X+1][Y-1]==3){
                        coords[2] = X+1;
                        coords[3] = Y-1;
                        game.sendAction(new CheckersJumpAction(this, coords));
                    }
                }
                if (selectedY-Y==2){
                    if (current.getBoard()[X+1][Y+1]==1||current.getBoard()[X+1][Y+1]==3){
                        coords[2] = X+1;
                        coords[3] = Y+1;
                        game.sendAction(new CheckersJumpAction(this, coords));
                    }
                }
            }
        }
        else{
            if (selectedX-X==2){
                if (selectedY-Y==-2){
                    if (current.getBoard()[X+1][Y-1]==1||current.getBoard()[X+1][Y-1]==3){
                        coords[2] = X+1;
                        coords[3] = Y-1;
                        game.sendAction(new CheckersJumpAction(this, coords));
                    }
                }
            }
            if (selectedX-X==-2){
                if (selectedY-Y==-2){
                    if (current.getBoard()[X-1][Y-1]==1||current.getBoard()[X-1][Y-1]==3){
                        coords[2] = X-1;
                        coords[3] = Y-1;
                        game.sendAction(new CheckersJumpAction(this, coords));
                    }
                }
            }
        }
        return false;
    }//jumpPiece


    // Checks if a given jump is possible, otherwise return false
    // @param X The x coordinate of the space to be moved to
    // @param Y The y coordinate of the space to be moved to
    public boolean checkJump(int X, int Y){
        if (X<0||X>7||Y<0||Y>7||current.getBoard()[X][Y]!=0){
            return false;
        }
        int selectedX = current.getSelectedPiece()[0];
        int selectedY = current.getSelectedPiece()[1];
        if (selectedX<0||selectedX>7||selectedY<0||selectedY>7){
            return false;
        }
        if (current.getBoard()[selectedX][selectedY]>=3){
            if (selectedX-X==-2){
                if (selectedY-Y==-2){
                    if (current.getBoard()[X-1][Y-1]==1||current.getBoard()[X-1][Y-1]==3){
                        return true;
                    }
                }
                if (selectedY-Y==2){
                    if (current.getBoard()[X-1][Y+1]==1||current.getBoard()[X-1][Y+1]==3){
                        return true;
                    }
                }
            }
            if (selectedX-X==2){
                if (selectedY-Y==-2){
                    if (current.getBoard()[X+1][Y-1]==1||current.getBoard()[X+1][Y-1]==3){
                        return true;
                    }
                }
                if (selectedY-Y==2){
                    if (current.getBoard()[X+1][Y+1]==1||current.getBoard()[X+1][Y+1]==3){
                        return true;
                    }
                }
            }
        }
        else{
            if (selectedX-X==-2){
                if (selectedY-Y==-2){
                    if (current.getBoard()[X-1][Y-1]==1||current.getBoard()[X-1][Y-1]==3){
                        return true;
                    }
                }
            }
            if (selectedX-X==2){
                if (selectedY-Y==-2){
                    if (current.getBoard()[X+1][Y-1]==1||current.getBoard()[X+1][Y-1]==3){
                        return true;
                    }
                }
            }
        }
        return false;
    }//jumpPiece

    /**
     *
     * @param xSel
     * @param ySel
     * @return true if dangerous spot, false otherwise
     */
    public boolean checkDanger(int xSel, int ySel) {
        // check all four surrounding areas for opponents pieces
        int[] cSelect = new int[2];
        cSelect[0] = current.getSelectedPiece()[0];
        cSelect[1] = current.getSelectedPiece()[1];
        if (xSel == 0 || xSel == 7) {
            return  false;
        }
        if (xSel > 0 && ySel > 0
                && current.getBoard()[xSel-1][ySel-1] == current.getPlayerTurn() + 2) { // opponent king
            if (xSel < 7 && ySel < 7
                    && (current.getBoard()[xSel+1][ySel+1] == 0 || (xSel+1 == cSelect[0] && ySel+1 == cSelect[1]))) {
                // determine if a piece will become open if jump is made
                return true;
            }
        }
        if (xSel < 7 && ySel > 0
                && current.getBoard()[xSel+1][ySel-1] == current.getPlayerTurn() + 2) { // opponent king
            if(xSel > 0 && ySel < 7
                    && current.getBoard()[xSel-1][ySel+1] == 0 || (xSel-1 == cSelect[0] && ySel+1 == cSelect[1])) {
                    return true;
            }
        }
        if (xSel > 0 && ySel < 7
                && (current.getBoard()[xSel-1][ySel+1] == current.getPlayerTurn() // opponent piece
                || current.getBoard()[xSel-1][ySel+1] == current.getPlayerTurn() + 2)) { // opponent king
            if(xSel < 7 && ySel > 0
                    && current.getBoard()[xSel+1][ySel-1] == 0 || (xSel+1 == cSelect[0] && ySel-1 == cSelect[1])) {
                    return true;
            }
        }
        if (xSel < 7 && ySel < 7
                && (current.getBoard()[xSel+1][ySel+1] == current.getPlayerTurn() // opponent piece
                || current.getBoard()[xSel+1][ySel+1] == current.getPlayerTurn() + 2)) { // opponent king
            if(xSel > 0 && ySel > 0
                    && current.getBoard()[xSel-1][ySel-1] == 0 || (xSel-1 == cSelect[0] && ySel-1 == cSelect[1])) {
                return true;
            }
        }
        return checkOpening(xSel, ySel);
            // check for empty space opposite of the opponent's piece
        // NOTE: can assume moving in down direction
    }

    public boolean checkOpening(int xMove, int yMove) {
        int[] select = new int[2];
        select[0] = current.getSelectedPiece()[0];
        select[1] = current.getSelectedPiece()[1];

        if (xMove > select[0] && yMove > select[1]) {
            if (select[0] > 1
                    && (current.getBoard()[xMove - 2][yMove] == playerNum + 1 || current.getBoard()[xMove - 2][yMove] == playerNum + 3)) {
                if (yMove < 7
                        && (current.getBoard()[xMove - 3][yMove + 1] == playerNum
                        || current.getBoard()[xMove - 3][yMove + 1] == playerNum + 2)) {
                    return true;
                }
            }
        }
        if (xMove < select[0] && yMove > select[1]) {
            if (select[0] < 6
                    && (current.getBoard()[xMove + 2][yMove] == playerNum + 1 || current.getBoard()[xMove + 2][yMove] == playerNum + 3)) {
                if (yMove < 7
                        && (current.getBoard()[xMove + 3][yMove + 1] == playerNum
                        || current.getBoard()[xMove + 3][yMove + 1] == playerNum + 2)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Sets whether or not the computer player has made a move this turn
    // @param moved Whether or not the computer player has moved this turn
    public void setMoved(boolean m){
        moved = m;
    }

}
