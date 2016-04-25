package edu.up.cs301.Checkers;

import java.io.Serializable;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 *
 * Action representing a request to move a piece
 *
 * @author Branden Vennes 
 * @author Brandon Sit 
 * @author Dominic Ferrari 
 * @author Sean Tollisen
 */
public class CheckersMoveAction extends GameAction implements Serializable {
    private static final long serialVersionUID = 777777888461316L;
    protected int movex;
    protected int movey;

    // Constructor for CheckersMoveAction
    // @param player the player who created the action
    // @param coordinates The tile coordinates of the moving piece
    public CheckersMoveAction(GamePlayer player, int[] coordinates) {
        super(player);
        movex = coordinates[0];
        movey = coordinates[1];
    }//CheckersMoveAction

    // Returns the X-coordinate of the moving piece
    public int getJumpX(){return movex;}//getJumpX

    // Returns the Y-coordinate of the moving piece
    public int getJumpY(){return movey;}//getJumpY
}
