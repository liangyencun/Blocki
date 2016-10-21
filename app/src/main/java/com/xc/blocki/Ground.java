package com.xc.blocki;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by Aaron on 2016-05-03.
 */
public class Ground extends Block {

    public Ground(int xPos, int yPos, int getWidth, int getHeight, Context context, GameView gameView){
        super(xPos, yPos, 0, 0, 0, 0, getWidth, getHeight, context);
        type = Type.GROUND;
        bitmap = gameView.bitmaps.ground;
        Log.i("Ground", hitbox.toShortString());
    }

    @Override
    public void update(){
        if (state == State.LEFT) {
            x -= 10;
        }
        if (state == State.RIGHT) {
            x += 10;
        }
        hitbox.set(x, y, x+width, y+height);
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
        //Log.d("ground", "Draw");
    }
}
