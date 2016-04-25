package edu.up.cs301.Checkers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

import java.io.Serializable;

import edu.up.cs301.game.infoMsg.GameState;

/**
 * Class draws a organized group of pieces that have been removed from the checkerboard
 *
 * @author Branden Vennes
 * @author Brandon Sit
 * @author Dominic Ferrari
 * @author Sean Tollisen
 */
public class DeadPileView extends SurfaceView implements Serializable{
    private static final long serialVersionUID = 111777888461316L;

    private int piecesDead;

    private int playerIDa;

    private Paint checkerPaint1;
    private Paint checkerPaint2;
    private Paint whiteOutlinePaint;

    private boolean setUp = false;

    public DeadPileView(Context context) {
        super(context);
        this.setWillNotDraw(false);
    }

    public DeadPileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setWillNotDraw(false);
    }

    public DeadPileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setWillNotDraw(false);
    }

    public DeadPileView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setWillNotDraw(false);
    }

    public void setPlayerID(int playerID) {
        if (!setUp) {
            piecesDead = 0;
            checkerPaint1 = new Paint();
            checkerPaint2 = new Paint();
            playerIDa = playerID;
            switch (playerID) {
                case 0:
                    checkerPaint1.setColor(0xff000000);
                    break;
                case 1:
                    checkerPaint2.setColor(0xffdc143c);
                    break;
            }
            setUp = true;
            whiteOutlinePaint = new Paint();
            whiteOutlinePaint.setColor(0xffffffff);
            whiteOutlinePaint.setStrokeWidth(5.0f);
            whiteOutlinePaint.setStyle(Paint.Style.STROKE);
            invalidate();
        }
    }

    public void setDead(int newValue) {
        piecesDead = newValue;
        invalidate();
    }

    public void setPaint1(int paint1) {
        checkerPaint1.setColor(paint1);
        invalidate();
    }

    public void setPaint2(int paint2) {
        checkerPaint2.setColor(paint2);
        invalidate();
    }

    public void onDraw(Canvas canvas) {
        // draw pieces dead
        if (setUp) {
            int segment = canvas.getHeight() / 12;
            Log.i("Drawing Dead", this.piecesDead + ", " + segment);
            if (playerIDa==0) {
                canvas.drawRect(0, 0, canvas.getWidth(), segment * piecesDead, whiteOutlinePaint);
                canvas.drawRect(0, 0, canvas.getWidth(), segment * piecesDead, checkerPaint1);
            }
            if (playerIDa==1) {
                canvas.drawRect(0, canvas.getHeight()-segment * piecesDead, canvas.getWidth(), canvas.getHeight(),  whiteOutlinePaint);
                canvas.drawRect(0, canvas.getHeight()-segment * piecesDead, canvas.getWidth(), canvas.getHeight(),  checkerPaint2);
            }
        }
    }
}
