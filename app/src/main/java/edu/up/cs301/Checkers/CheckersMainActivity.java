package edu.up.cs301.Checkers;

import android.database.Cursor;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.R;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.config.GameConfig;
import edu.up.cs301.game.config.GamePlayerType;

/**
 *
 * Class controlling the interactions between the GUI and framework, amongst other things
 *
 * @author Branden Vennes
 * @author Brandon Sit
 * @author Dominic Ferrari
 * @author Sean Tollisen
 */
public class CheckersMainActivity extends GameMainActivity {

    // the port number that this game will use when playing over the network
    private static final int PORT_NUMBER = 3398;

    // Mr. MediaPlayer
    protected MediaPlayer Sound;

    /**
     External Citation
     Date: 4/24/2016
     Problem: Soundpool wasn't working
     Resource:
     http://stackoverflow.com/questions/5202510/soundpool-sample-not-ready
     Solution: Set an OnLoadCompleteListener
     */

    /**
     External Citation
     Date: 4/24/2016
     Problem: Soundpool only playing first 5 seconds of song on loop
     Resource:
     http://stackoverflow.com/questions/13377604/soundpool-plays-only-first-5-secs-of-file-why
     Solution: Replace with MediaPlayer
     */

    //Create a new GameConfig with variables set to defaults
    @Override
    public GameConfig createDefaultConfig() {

        //Instantiate der MediaPlayer
        Sound = MediaPlayer.create(this, R.raw.sublimin);
        Sound.setLooping(true);
        Sound.start();

        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        // a human player player type (player type 0)
        playerTypes.add(new GamePlayerType("Local Human Player") {
            public GamePlayer createPlayer(String name) {
                return new CheckersHumanPlayer(name);
            }});

        // a computer player type (player type 1)
        playerTypes.add(new GamePlayerType("Dumb Computer Player") {
            public GamePlayer createPlayer(String name) {
                return new CheckersComputerPlayer(name, false);
            }});

        playerTypes.add(new GamePlayerType("Smart Computer Player") {
            public GamePlayer createPlayer(String name) {
                return new CheckersComputerPlayer(name, true);
            }});

        // Create a game configuration class for Checkers:
        // - player types as given above
        // - 2 players
        // - name of game is "Checkers"
        // - port number as defined above
        GameConfig defaultConfig = new GameConfig(playerTypes, 2, 2, "Checkers",
                PORT_NUMBER);

        // Add the default players to the configuration
        defaultConfig.addPlayer("Human", 0); // player 1: a human player
        defaultConfig.addPlayer("Computer", 1); // player 2: a computer player

        // Set the default remote-player setup:
        // - player name: "Remote Player"
        // - IP code: (empty string)
        // - default player type: human player
        defaultConfig.setRemoteData("Remote Player", "", 0);

        // return the configuration
        return defaultConfig;
    }//createDefaultConfig


    //Creates a new LocalGame with variables set to defaults
    @Override
    public LocalGame createLocalGame() {
        return new CheckersLocalGame();
    }//LocalGame

    //Resumes the music when reopened
    @Override
    public void onResume() {
        super.onResume();
        Sound.start();
    }

    //Pauses the music when the game is paused
    @Override
    public void onPause(){
        super.onPause();
        Sound.pause();
    }
}