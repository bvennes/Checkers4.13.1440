package edu.up.cs301.Checkers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

import java.io.Serializable;

/**
 * Class representing a surface view which draws a checkerboard using a 2D array
 *
 * @author Branden Vennes
 * @author Brandon Sit
 * @author Dominic Ferrari
 * @author Sean Tollisen
 */
public class CheckersBoardView extends SurfaceView implements Serializable {
    private static final long serialVersionUID = 444777888461316L;

    private CheckersState currState;
    private boolean setUp;
    private int[][] board;

    private Paint highlighter;
    private Paint boardPaint;
    private Paint redBoardPaint;
    private Paint blackCheckerPaint;
    private Paint redCheckerPaint;
    private Paint whiteOutlinePaint;

    private int[][] moveMarker;
    private int[] lastSelected;
    private int[] jumped;

    private int[][] lastBoard;

    private int playerTurn;

    public CheckersBoardView(Context context) {
        super(context);
        setUp = false;
        this.setWillNotDraw(false);
        getSetup();
    }

    public CheckersBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp = false;
        this.setWillNotDraw(false);
        getSetup();
    }

    public CheckersBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp = false;
        this.setWillNotDraw(false);
        getSetup();
    }

    public CheckersBoardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setUp = false;
        this.setWillNotDraw(false);
        getSetup();
    }


    /**
     External Citation
     Date: 4/8/2016
     Problem: I needed to set the type of paint brush to an outline instead of fill
     Resource:
     http://developer.android.com/reference/android/graphics/Paint.html
     Solution: paint.setStyle(Paint.Style.STROKE);
     */


    /**
     * sets up the CheckersBoardView so that it can be drawn upon its next invalidate()
     * initializes paints and prepares for recieving a new board
     */
    private void getSetup() {
        boardPaint = new Paint();
        boardPaint.setColor(0xffc3834c);

        redBoardPaint = new Paint();
        redBoardPaint.setColor(0xff493626);

        redCheckerPaint = new Paint();
        redCheckerPaint.setColor(0xffdc143c);

        blackCheckerPaint = new Paint();
        blackCheckerPaint.setColor(0xff000000);

        whiteOutlinePaint = new Paint();
        whiteOutlinePaint.setColor(0xffffffff);
        whiteOutlinePaint.setStyle(Paint.Style.STROKE);
        whiteOutlinePaint.setStrokeWidth(5);

        highlighter = new Paint();
        highlighter.setColor(0xffffff00);

        moveMarker = new int[2][2];
        lastSelected = new int[2];
        jumped = new int[2];

        lastSelected[0] = -1;
        lastSelected[1] = -1;

        jumped[0] = -1;
        jumped[1] = -1;

        lastBoard = new int[8][8];

        playerTurn = 0;

        setUp = true;
    }

    /**
     External Citation
     Date: 4/8/2016
     Problem: Needed to know what object gets drawn on
     Resource:
     http://stackoverflow.com/questions/21221649/
     android-how-to-use-the-ondraw-method-in-a-class-extending-activity
     Solution: canvas is drawn on
     */

    @Override
    public void draw(Canvas canvas) {
        if (board != null) {
            if (setUp) {
                int canvasWidth = canvas.getWidth();
                int squareLength = canvasWidth / 8;

                // row 0
                for(int i = 0; i < 8; i++) {
                    if (i % 2 == 0) {
                        canvas.drawRect((float) i * squareLength, 0f, (float) i * squareLength + squareLength, squareLength, boardPaint);
                        canvas.drawRect((float) i * squareLength, 0f, (float) i * squareLength + squareLength, squareLength, whiteOutlinePaint);
                    }
                    else {
                        canvas.drawRect((float) i * squareLength, 0f, (float) i * squareLength + squareLength, squareLength, redBoardPaint);
                        canvas.drawRect((float) i * squareLength, 0f, (float) i * squareLength + squareLength, squareLength, whiteOutlinePaint);
                    }
                }
                // row 1
                for(int i = 0; i < 8; i++) {
                    if (i % 2 == 0) {
                        canvas.drawRect((float) i * squareLength, (float) squareLength, (float) i * squareLength + squareLength, (float) squareLength + squareLength, redBoardPaint);
                        canvas.drawRect((float) i * squareLength, (float) squareLength, (float) i * squareLength + squareLength, (float) squareLength + squareLength, whiteOutlinePaint);
                    }
                    else {
                        canvas.drawRect((float) i * squareLength, (float) squareLength, (float) i * squareLength + squareLength, (float) squareLength + squareLength, boardPaint);
                        canvas.drawRect((float) i * squareLength, (float) squareLength, (float) i * squareLength + squareLength, (float) squareLength + squareLength, whiteOutlinePaint);
                    }
                }
                // row 2
                for(int i = 0; i < 8; i++) {
                    if (i % 2 == 0) {
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 2, (float) i * squareLength + squareLength, (float) squareLength * 2 + squareLength, boardPaint);
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 2, (float) i * squareLength + squareLength, (float) squareLength * 2 + squareLength, whiteOutlinePaint);
                    }
                    else {
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 2, (float) i * squareLength + squareLength, (float) squareLength * 2 + squareLength, redBoardPaint);
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 2, (float) i * squareLength + squareLength, (float) squareLength * 2 + squareLength, whiteOutlinePaint);
                    }
                }
                // row 3
                for(int i = 0; i < 8; i++) {
                    if (i % 2 == 0) {
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 3, (float) i * squareLength + squareLength, (float) squareLength * 3 + squareLength, redBoardPaint);
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 3, (float) i * squareLength + squareLength, (float) squareLength * 3 + squareLength, whiteOutlinePaint);
                    }
                    else {
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 3, (float) i * squareLength + squareLength, (float) squareLength * 3 + squareLength, boardPaint);
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 3, (float) i * squareLength + squareLength, (float) squareLength * 3 + squareLength, whiteOutlinePaint);
                    }
                }
                // row 4
                for(int i = 0; i < 8; i++) {
                    if (i % 2 == 0) {
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 4, (float) i * squareLength + squareLength, (float) squareLength * 4 + squareLength, boardPaint);
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 4, (float) i * squareLength + squareLength, (float) squareLength * 4 + squareLength, whiteOutlinePaint);
                    }
                    else {
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 4, (float) i * squareLength + squareLength, (float) squareLength * 4 + squareLength, redBoardPaint);
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 4, (float) i * squareLength + squareLength, (float) squareLength * 4 + squareLength, whiteOutlinePaint);
                    }
                }
                // row 5
                for(int i = 0; i < 8; i++) {
                    if (i % 2 == 0) {
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 5, (float) i * squareLength + squareLength, (float) squareLength * 5 + squareLength, redBoardPaint);
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 5, (float) i * squareLength + squareLength, (float) squareLength * 5 + squareLength, whiteOutlinePaint);
                    }
                    else {
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 5, (float) i * squareLength + squareLength, (float) squareLength * 5 + squareLength, boardPaint);
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 5, (float) i * squareLength + squareLength, (float) squareLength * 5 + squareLength, whiteOutlinePaint);
                    }
                }
                // row 6
                for(int i = 0; i < 8; i++) {
                    if (i % 2 == 0) {
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 6, (float) i * squareLength + squareLength, (float) squareLength * 6 + squareLength, boardPaint);
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 6, (float) i * squareLength + squareLength, (float) squareLength * 6 + squareLength, whiteOutlinePaint);
                    }
                    else {
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 6, (float) i * squareLength + squareLength, (float) squareLength * 6 + squareLength, redBoardPaint);
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 6, (float) i * squareLength + squareLength, (float) squareLength * 6 + squareLength, whiteOutlinePaint);
                    }
                }
                // row 7
                for(int i = 0; i < 8; i++) {
                    if (i % 2 == 0) {
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 7, (float) i * squareLength + squareLength, (float) squareLength * 7 + squareLength, redBoardPaint);
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 7, (float) i * squareLength + squareLength, (float) squareLength * 7 + squareLength, whiteOutlinePaint);
                    }
                    else {
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 7, (float) i * squareLength + squareLength, (float) squareLength * 7 + squareLength, boardPaint);
                        canvas.drawRect((float) i * squareLength, (float) squareLength * 7, (float) i * squareLength + squareLength, (float) squareLength * 7 + squareLength, whiteOutlinePaint);
                    }
                }

                // draw checkers on the board
                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        switch (board[row][col]) {
                            case 1:
                                canvas.drawOval((float) row * squareLength + squareLength / 8, (float) col * squareLength + squareLength / 8,
                                        (float) row * squareLength + squareLength - squareLength / 8, (float) col * squareLength + squareLength - squareLength / 8, redCheckerPaint);
                                break;
                            case 2:
                                canvas.drawOval((float) row * squareLength + squareLength /8, (float) col * squareLength + squareLength / 8,
                                        (float) row * squareLength + squareLength - squareLength / 8, (float) col * squareLength + squareLength - squareLength / 8, blackCheckerPaint);
                                break;
                            case 3:
                                canvas.drawOval((float) row * squareLength + squareLength / 8, (float) col * squareLength + squareLength / 8,
                                        (float) row * squareLength + squareLength - squareLength / 8, (float) col * squareLength + squareLength - squareLength / 8, redCheckerPaint);
                                canvas.drawRect((float) row * squareLength + squareLength / 3, (float) col * squareLength + squareLength / 3,
                                        (float) row * squareLength + squareLength - squareLength / 3, (float) col * squareLength + squareLength - squareLength / 3, blackCheckerPaint);
                                break;
                            case 4:
                                canvas.drawOval((float) row * squareLength + squareLength / 8, (float) col * squareLength + squareLength / 8,
                                        (float) row * squareLength + squareLength - squareLength / 8, (float) col * squareLength + squareLength - squareLength / 8, blackCheckerPaint);
                                canvas.drawRect((float) row * squareLength + squareLength / 3, (float) col * squareLength + squareLength / 3,
                                        (float) row * squareLength + squareLength - squareLength / 3, (float) col * squareLength + squareLength - squareLength / 3, whiteOutlinePaint);
                                break;
                        }
                        if (board[row][col] != 0) {
                            canvas.drawOval((float) row * squareLength + squareLength / 8, (float) col * squareLength + squareLength / 8,
                                    (float) row * squareLength + squareLength - squareLength / 8, (float) col * squareLength + squareLength - squareLength / 8, whiteOutlinePaint);
                        }
                    }
                }

                // highlight current selection
                int[] currSelection = currState.getSelectedPiece();
                if (currSelection[0]!=-1&&currSelection[1]!=-1) {
                    canvas.drawOval((float) currSelection[0] * squareLength + squareLength / 8,
                            (float) currSelection[1] * squareLength + squareLength / 8,
                            (float) currSelection[0] * squareLength + squareLength - squareLength / 8,
                            (float) currSelection[1] * squareLength + squareLength - squareLength / 8, highlighter);
                }

                // number of differences between state before and state after
                int diffCount = 0;

                // a move has been made and a movement line marker must be placed
                if (playerTurn != currState.getPlayerTurn()) {
                    for(int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            if (board[i][j] != lastBoard[i][j]) {
                                switch (diffCount) {
                                    case 0: // first difference found, indicates a simple move start
                                        moveMarker[0][0] = i;
                                        moveMarker[0][1] = j;
                                        diffCount++;
                                        break;
                                    case 1: // second difference found, indicates a simple move end
                                        moveMarker[1][0] = i;
                                        moveMarker[1][1] = j;
                                        diffCount++;
                                        break;
                                    case 2: // three differences, indicates a jump has been made
                                        int tempX, tempY;
                                        tempX = moveMarker[1][0];
                                        tempY = moveMarker[1][1];
                                        jumped[0] = tempX;
                                        jumped[1] = tempY;
                                        moveMarker[1][0] = i;
                                        moveMarker[1][1] = j;
                                        diffCount++;
                                        break;
                                }
                            }
                        }
                    }
                    if (diffCount == 2) {
                        jumped[0] = -1;
                        jumped[1] = -1;
                    }
                    playerTurn = currState.getPlayerTurn();
                }

                if (!(moveMarker == null)) {
                    highlighter.setStyle(Paint.Style.STROKE);
                    highlighter.setStrokeWidth(10);

                    canvas.drawLine((float) squareLength * moveMarker[0][0] + (squareLength / 2), (float) squareLength * moveMarker[0][1] + (squareLength / 2),
                            (float) squareLength * moveMarker[1][0] + (squareLength / 2), (float) squareLength * moveMarker[1][1] + (squareLength / 2), highlighter);

                    if (jumped[0] > -1 && jumped[1] > -1 && jumped != null) {
                        if (moveMarker[0][0] < moveMarker[1][0] && moveMarker[0][1] < moveMarker[1][1]) {
                            canvas.drawLine((float) jumped[0] * squareLength, (float) jumped[1] * squareLength + squareLength, (float) jumped[0] * squareLength + squareLength, (float) jumped[1] * squareLength, highlighter);
                        }
                        else if (moveMarker[0][0] < moveMarker[1][0] && moveMarker[0][1] > moveMarker[1][1]) { // check
                            canvas.drawLine((float) jumped[0] * squareLength, (float) jumped[1] * squareLength, (float) jumped[0] * squareLength + squareLength, (float) jumped[1] * squareLength + squareLength, highlighter);
                        }
                        else if (moveMarker[0][0] > moveMarker[1][0] && moveMarker[0][1] < moveMarker[1][1]) { // check
                            canvas.drawLine((float) jumped[0] * squareLength, (float) jumped[1] * squareLength, (float) jumped[0] * squareLength + squareLength, (float) jumped[1] * squareLength + squareLength, highlighter);
                        }
                        else if (moveMarker[0][0] > moveMarker[1][0] && moveMarker[0][1] > moveMarker[1][1]) {
                            canvas.drawLine((float) jumped[0] * squareLength, (float) jumped[1] * squareLength + squareLength, (float) jumped[0] * squareLength + squareLength, (float) jumped[1] * squareLength, highlighter);
                        }
                    }
                }
                highlighter.setStyle(Paint.Style.FILL);

                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        lastBoard[i][j] = board[i][j];
                    }
                }
            }
        }
    }

    public void giveState(CheckersState newState) {
        currState = newState;
        board = currState.getBoard();
        invalidate();
    }

    public void setHumColor(int col)
    {
        switch (col)
        {
            case 0:redCheckerPaint.setColor(0xffdc143c);
                break;

            case 1:redCheckerPaint.setColor(0xff21fb24);
                break;

            case 2:redCheckerPaint.setColor(0xff14F9FF);
                break;
        }
        invalidate();
    }
    public void setOppColor(int col)
    {
        switch (col)
        {
            case 0:blackCheckerPaint.setColor(0xff000000);
                break;

            case 1:blackCheckerPaint.setColor(0xfFF900EE);
                break;

            case 2:blackCheckerPaint.setColor(0xff0016BD);
                break;
        }
        invalidate();
    }

}
