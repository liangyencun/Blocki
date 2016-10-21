package com.xc.blocki;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by Aaron on 2016-05-11.
 */
public class FinishLine extends Block {
    public FinishLine(int xPos, int yPos, int xSpeed, int ySpeed, int gravity, int health, int getWidth, int getHeight, Context context, GameView gameView) {
        super(xPos, yPos, xSpeed, ySpeed, gravity, health, getWidth, getHeight, context, gameView);
    }

    public FinishLine(int xPos, int yPos, int getWidth, int getHeight, Context context, GameView gameView){
        this(xPos, yPos, 0, 0, 0, 0, getWidth, getHeight, context, gameView);
        type = Type.ITEM;
        bitmap =  gameView.bitmaps.finishLine;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void update() {
        if (state == State.LEFT) {
            x -= 10;
        }
        if (state == State.RIGHT) {
            x += 10;
        }
        hitbox.set(x, y, x+width, y+height);
    }
}
