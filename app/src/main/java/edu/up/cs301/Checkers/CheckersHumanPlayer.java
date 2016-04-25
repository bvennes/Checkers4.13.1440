package edu.up.cs301.Checkers;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;

import edu.up.cs301.animation.Animator;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 *
 * Class representing and receiving inputs from one Human player with name.
 *
 * @author Branden Vennes
 * @author Brandon Sit
 * @author Dominic Ferrari
 * @author Sean Tollisen
 */
public class CheckersHumanPlayer extends GameHumanPlayer
        implements View.OnTouchListener, CheckersPlayer, Serializable, AdapterView.OnItemSelectedListener{

    private static final long serialVersionUID = 555666888461316L;

    private DeadPileView opponentDeadPoolSurface;

    private DeadPileView playerDeadPoolSurface;

    private CheckersBoardView gameBoardSurface;

    private TextView playerId;

    private TextView oppId;

    private Paint highlighter;

    protected Spinner spin0;
    protected Spinner spin1;

    // The current checkers state
    protected CheckersState current;

    // The CheckersMainActivity in use
    protected CheckersMainActivity mainActivity;

    // Constructor for CheckersHumanPlayer
    // @param name Name for this player
    public CheckersHumanPlayer(String name){
        super(name);
    }//CheckersHumanPlayer

    // Returns the top view for flashes and such
    @Override
    public View getTopView() {
        return null;
    }//getTopView



    // Receives GameInfo objects and responds to them.
    // @param info The actual GameInfo object received
    @Override
    public void receiveInfo(GameInfo info) {
        if (info instanceof CheckersState){
            current = (CheckersState)info;
            Log.i("GUI", "about to render");
            playerId.setText(allPlayerNames[0]);
            oppId.setText(allPlayerNames[1]);
            if(current.getPlayerTurn() == playerNum) {
                playerId.setTextColor(highlighter.getColor());
                oppId.setTextColor(0xffffffff);
            }
            else {
                oppId.setTextColor(highlighter.getColor());
                playerId.setTextColor(0xffffffff);
            }
            gameBoardSurface.giveState(current);
            updateDead();
        }
    }//receiveInfo

    // Checks if a piece is your piece, and sends a CheckersSelectAction
    // returns false otherwise
    // @param X the x coordinate on the board of the piece to be selected
    // @param Y the y coordinate on the board of the piece to be selected
    public boolean selectPiece(int X, int Y) {
        if (X > 7 || X < 0 || Y > 7 || Y < 0) {
            int[] badCoords = new int[2];
            badCoords[0] = -1;
            badCoords[1] = -1;
            game.sendAction(new CheckersSelectAction(this, badCoords));
            return true;
        }
        int[] coords = new int[2];
        coords[0] = X;
        coords[1] = Y;
        if (current == null) {
            return false;
        }
        if (current.getBoard()[X][Y]==playerNum + 1||current.getBoard()[X][Y]==playerNum+3){
            game.sendAction(new CheckersSelectAction(this, coords));
            return true;
        }
        coords[0]=-1;
        coords[1]=-1;
        game.sendAction(new CheckersSelectAction(this, coords));
        return false;
    }//selectPiece

    public void updateDead() {
        int playerPieces = 0;
        int opponentPieces = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (current.getBoard()[i][j] == 0) {
                    // do nothing
                }
                else if (current.getBoard()[i][j] == this.playerNum || current.getBoard()[i][j] == this.playerNum + 2) {
                    playerPieces++;
                }
                else {
                    opponentPieces++;
                }
            }
        }
        playerDeadPoolSurface.setDead((12 - playerPieces));
        opponentDeadPoolSurface.setDead(12 - opponentPieces);
    }


    // Checks to see if move action is valid and send an action, otherwise return
    // false
    // @param X the x coordinate on the board of the space to be moved to
    // @param Y the y coordinate on the board of the space to be moved to
    public boolean movePiece(int X, int Y){
        if (current == null ||current.getSelectedPiece()[0]==-1||current.getSelectedPiece()[1]==-1){
            return false;
        }
        if (X > 7 || X < 0 || Y > 7 || Y < 0) {
            return false;
        }
        int[] coords = new int[2];
        coords[0] = X;
        coords[1] = Y;
        int selectedX = current.getSelectedPiece()[0];
        int selectedY = current.getSelectedPiece()[1];
        if (playerNum == 0) {
            if (current.getBoard()[selectedX][selectedY]>=3){
                if (current.getBoard()[X][Y]==0){
                    if (selectedX-X==1||selectedX-X==-1){
                        if(selectedY-Y==1||selectedY-Y==-1){
                            game.sendAction(new CheckersMoveAction(this, coords));
                            return true;
                        }
                    }
                }
            }
            else{
                if (current.getBoard()[X][Y]==0){
                    if (selectedX-X==1||selectedX-X==-1){
                        if(selectedY-Y==1){
                            game.sendAction(new CheckersMoveAction(this, coords));
                            return true;
                        }
                    }
                }
            }
        }
        else {
            if (current.getBoard()[selectedX][selectedY]>=4){
                if (current.getBoard()[X][Y]==0){
                    if (selectedX-X==1||selectedX-X==-1){
                        if(selectedY-Y==1||selectedY-Y==-1){
                            game.sendAction(new CheckersMoveAction(this, coords));
                            return true;
                        }
                    }
                }
            }
            else{
                if (current.getBoard()[X][Y]==0){
                    if (selectedX-X==1||selectedX-X==-1){
                        if(selectedY-Y==-1){
                            game.sendAction(new CheckersMoveAction(this, coords));
                            return true;
                        }
                    }
                }
            }
        }
        Log.i("Fat cat 2 pack", "jdksal;f");
        return false;
    }//movePiece


    // Checks if a given jump is possible, and sends the action if so
    // Otherwise, return false
    // @param X The x coordinate of the space to be moved to
    // @param Y The y coordinate of the space to be moved to
    public boolean jumpPiece(int X, int Y){
        if (X > 7 || X < 0 || Y > 7 || Y < 0) {
            return false;
        }
        if (current == null || current.getSelectedPiece()[0]==-1||current.getSelectedPiece()[1]==-1||current.getBoard()[X][Y]!=0){
            return false;
        }
        int[] coords = new int[4];
        coords[0] = X;
        coords[1] = Y;
        int selectedX = current.getSelectedPiece()[0];
        int selectedY = current.getSelectedPiece()[1];
        if (playerNum == 0) {
            if (current.getBoard()[selectedX][selectedY]>=3){
                if (selectedX-X==-2){
                    if (selectedY-Y==-2){
                        if (current.getBoard()[X-1][Y-1]==2||current.getBoard()[X-1][Y-1]==4){
                            coords[2] = X-1;
                            coords[3] = Y-1;
                            game.sendAction(new CheckersJumpAction(this, coords));
                            return true;
                        }
                    }
                    if (selectedY-Y==2){
                        if (current.getBoard()[X-1][Y+1]==2||current.getBoard()[X-1][Y+1]==4){
                            coords[2] = X-1;
                            coords[3] = Y+1;
                            game.sendAction(new CheckersJumpAction(this, coords));
                            return true;
                        }
                    }
                }
                if (selectedX-X==2){
                    if (selectedY-Y==-2){
                        if (current.getBoard()[X+1][Y-1]==2||current.getBoard()[X+1][Y-1]==4){
                            coords[2] = X+1;
                            coords[3] = Y-1;
                            game.sendAction(new CheckersJumpAction(this, coords));
                            return true;
                        }
                    }
                    if (selectedY-Y==2){
                        if (current.getBoard()[X+1][Y+1]==2||current.getBoard()[X+1][Y+1]==4){
                            coords[2] = X+1;
                            coords[3] = Y+1;
                            game.sendAction(new CheckersJumpAction(this, coords));
                            return true;
                        }
                    }
                }
            }
            else{
                if (selectedX-X==-2){
                    if (selectedY-Y==2){
                        if (current.getBoard()[X-1][Y+1]==2||current.getBoard()[X-1][Y+1]==4){
                            coords[2] = X-1;
                            coords[3] = Y+1;
                            game.sendAction(new CheckersJumpAction(this, coords));
                            return true;
                        }
                    }
                }
                if (selectedX-X==2){
                    if (selectedY-Y==2){
                        if (current.getBoard()[X+1][Y+1]==2||current.getBoard()[X+1][Y+1]==4){
                            coords[2] = X+1;
                            coords[3] = Y+1;
                            game.sendAction(new CheckersJumpAction(this, coords));
                            return true;
                        }
                    }
                }
            }
        }
        else {
            if (current.getBoard()[selectedX][selectedY]>=4){
                if (selectedX-X==-2){
                    if (selectedY-Y==-2){
                        if (current.getBoard()[X-1][Y-1]==1||current.getBoard()[X-1][Y-1]==3){
                            coords[2] = X-1;
                            coords[3] = Y-1;
                            game.sendAction(new CheckersJumpAction(this, coords));
                            return true;
                        }
                    }
                    if (selectedY-Y==2){
                        if (current.getBoard()[X-1][Y+1]==1||current.getBoard()[X-1][Y+1]==3){
                            coords[2] = X-1;
                            coords[3] = Y+1;
                            game.sendAction(new CheckersJumpAction(this, coords));
                            return true;
                        }
                    }
                }
                if (selectedX-X==2){
                    if (selectedY-Y==-2){
                        if (current.getBoard()[X+1][Y-1]==1||current.getBoard()[X+1][Y-1]==3){
                            coords[2] = X+1;
                            coords[3] = Y-1;
                            game.sendAction(new CheckersJumpAction(this, coords));
                            return true;
                        }
                    }
                    if (selectedY-Y==2){
                        if (current.getBoard()[X+1][Y+1]==1||current.getBoard()[X+1][Y+1]==3){
                            coords[2] = X+1;
                            coords[3] = Y+1;
                            game.sendAction(new CheckersJumpAction(this, coords));
                            return true;
                        }
                    }
                }
            }
            else{
                if (selectedX-X==-2){
                    if (selectedY-Y==-2){
                        if (current.getBoard()[X-1][Y-1]==1||current.getBoard()[X-1][Y-1]==3){
                            coords[2] = X-1;
                            coords[3] = Y-1;
                            game.sendAction(new CheckersJumpAction(this, coords));
                            return true;
                        }
                    }
                }
                if (selectedX-X==2){
                    if (selectedY-Y==-2){
                        if (current.getBoard()[X+1][Y-1]==1||current.getBoard()[X+1][Y-1]==3){
                            coords[2] = X+1;
                            coords[3] = Y-1;
                            game.sendAction(new CheckersJumpAction(this, coords));
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }//jumpPiece

    // Receives a GUI to display to the player and receive inputs from
    // @param activity MainActivity for the GUI
    public void setAsGui(GameMainActivity activity) {
        mainActivity = (CheckersMainActivity) activity;

        activity.setContentView(R.layout.checkers_main);

        gameBoardSurface = (CheckersBoardView) activity.findViewById(R.id.gameBoardSurfaceView);

        playerDeadPoolSurface = (DeadPileView) activity.findViewById(R.id.playersDeadPileSurfaceView);

        opponentDeadPoolSurface = (DeadPileView) activity.findViewById(R.id.opponentsDeadPileSurfaceView);

        playerDeadPoolSurface.setPlayerID(0);

        opponentDeadPoolSurface.setPlayerID(1);

        gameBoardSurface.setOnTouchListener(this);

        playerId = (TextView) activity.findViewById(R.id.playerID);

        oppId = (TextView) activity.findViewById(R.id.opponentID);

        highlighter = new Paint();
        highlighter.setColor(0xffffff00);

        spin0=(Spinner)activity.findViewById(R.id.player0spin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mainActivity,R.array.colors,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin0.setAdapter(adapter);
        spin0.setOnItemSelectedListener(this);

        spin1=(Spinner)activity.findViewById(R.id.player1spin);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(mainActivity, R.array.colors1,
                android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adapter1);
        spin1.setOnItemSelectedListener(this);


    }//setAsGui

    public boolean onTouch(View v, MotionEvent event) {
        if (event.getDownTime()==event.getEventTime()) {
            int tileX = -1;
            int tileY = -1;
            int squareHeight = gameBoardSurface.getHeight() / 8;
            int squareWidth = gameBoardSurface.getWidth() / 8;
            if (v == gameBoardSurface) {
                for (int i = 1; i <= 8; i++) {
                    if (event.getX() - (squareWidth * i) <= 0) {
                        tileX = i - 1;
                        break;
                    }
                }
                for (int i = 1; i <= 8; i++) {
                    if (event.getY() - (squareHeight * i) <= 0) {
                        tileY = i - 1;
                        break;
                    }
                }
                if (!movePiece(tileX, tileY)&&!jumpPiece(tileX, tileY)) {
                    selectPiece(tileX, tileY);
                    return true;
                }
            } else {
                selectPiece(-1, -1);
                return true;
            }
        }
        return false;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId()==R.id.player0spin)
        {
            gameBoardSurface.setHumColor(position);
            switch (position)
            {
                case 0:
                    spin0.setBackgroundColor(0xffdc143c);
                    playerDeadPoolSurface.setPaint1(0xffdc143c);
                    break;

                case 1:
                    spin0.setBackgroundColor(0xff21fb24);
                    playerDeadPoolSurface.setPaint1(0xff21fb24);
                    break;

                case 2:
                    spin0.setBackgroundColor(0xff14F9FF);
                    playerDeadPoolSurface.setPaint1(0xff14F9FF);
                    break;

            }
        }
        if(parent.getId()==R.id.player1spin)
        {
            gameBoardSurface.setOppColor(position);
            switch (position)
            {
                case 0:spin1.setBackgroundColor(0xffDDE8F0);
                    opponentDeadPoolSurface.setPaint2(0xffDDE8F0);
                    break;

                case 1:spin1.setBackgroundColor(0xfFF900EE);
                    opponentDeadPoolSurface.setPaint2(0xfFF900EE);
                    break;

                case 2:spin1.setBackgroundColor(0xff0016BD);
                    opponentDeadPoolSurface.setPaint2(0xff0016BD);
                    break;
            }
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    public int getNum() {
        return playerNum;
    }



}
