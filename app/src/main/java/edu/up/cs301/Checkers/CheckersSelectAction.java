package edu.up.cs301.Checkers;

import java.io.Serializable;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 *
 * Class representing a request to select a piece
 *
 * @author Branden Vennes
 * @author Brandon Sit
 * @author Dominic Ferrari
 * @author Sean Tollisen
 */
public class CheckersSelectAction extends GameAction implements Serializable{
    private static final long serialVersionUID = 666777888461316L;

    protected int choosex;
    protected int choosey;

    // Constructor for CheckersSelectAction
    // @param player the player who created the action
    // @param coordinates The tile coordinates of the piece to be selected
    public CheckersSelectAction(GamePlayer player, int[] coordinates) {
        super(player);
        choosex = coordinates[0];
        choosey = coordinates[1];
    }//CheckersSelectAction

    // Returns the X-coordinate of the chosen piece
    public int getJumpX(){return choosex;}//getJumpX

    // Returns the Y-coordinate of the chosen piece
    public int getJumpY(){return choosey;}//getJumpY
}
