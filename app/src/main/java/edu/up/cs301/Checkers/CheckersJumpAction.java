package edu.up.cs301.Checkers;

import java.io.Serializable;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 *
 * Action representing a request to jump a piece
 *
 * @author Branden Vennes
 * @author Brandon Sit
 * @author Dominic Ferrari
 * @author Sean Tollisen
 */
public class CheckersJumpAction extends GameAction implements Serializable{
    private static final long serialVersionUID = 888777888461316L;

    protected int jumpx;
    protected int jumpy;
    protected int jumpedx;
    protected int jumpedy;

    // Constructor for CheckersJumpAction
    // @param player the player who created the action
    // @param coordinates The tile coordinates of the jumping piece and piece to be jumped
    public CheckersJumpAction(GamePlayer player, int[] coordinates) {
        super(player);
        jumpx = coordinates[0];
        jumpy = coordinates[1];
        try {
            jumpedx = coordinates[2];
        }
        catch (IndexOutOfBoundsException e){
            System.out.println("Someone's trying to make a jump action with a coordinate array too short");
        }
        try {
            jumpedy = coordinates[3];
        }
        catch (IndexOutOfBoundsException e){
            System.out.println("Someone's trying to make a jump action with a coordinate array too short");
        }
    }//CheckersJumpAction

    // Returns the X-coordinate of the jumping piece
    public int getJumpX(){return jumpx;}//getJumpX

    // Returns the Y-coordinate of the jumping piece
    public int getJumpY(){return jumpy;}//getJumpY

    // Returns the X-coordinate of the jumped piece
    public int getJumpedX(){return jumpedx;}//getJumpedX

    // Returns the Y-coordinate of the jumped piece
    public int getJumpedY(){return jumpedy;}//getJumpedY
}
