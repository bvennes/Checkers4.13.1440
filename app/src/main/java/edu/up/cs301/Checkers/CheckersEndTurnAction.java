package edu.up.cs301.Checkers;

import java.io.Serializable;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 *
 * Action which cues the switching of the current turn.
 * NOTE: This is a seperate action for actions like Jumping, which
 * don't necessitate the end of a turn.
 *
 * @author Branden Vennes
 * @author Brandon Sit
 * @author Dominic Ferrari
 * @author Sean Tollisen
 */
public class CheckersEndTurnAction extends GameAction implements Serializable{

    private static final long serialVersionUID = 555777999461316L;

    // Constructor for CheckersEndTurnAction
    //@param player the player who created the action
    public CheckersEndTurnAction(GamePlayer player) {
        super(player);
    }//CheckersEndTurnAction
}
