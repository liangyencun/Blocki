package com.xc.blocki;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.provider.Settings;
import android.util.Log;
import android.view.SurfaceView;

/**
 * Created by LiangYenChun on 5/4/2016.
 */
public class Background extends Block {

    boolean backgroundStopped;



    public Background(int getWidth, int getHeight, Context context, GameView gameView){
        super(0, 0, 0, 0, 0, 0, getWidth, getHeight, context);
        this.gameView = gameView;
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.background2);
        Log.d("gameView.endX = ", String.valueOf(gameView.endX));
        bitmap =  Bitmap.createScaledBitmap(tmp, gameView.endX, getHeight, false);
        backgroundStopped = true;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void update(){
        if (state == state.LEFT) {
            x -= 10;
            Log.d("background", state.toString());
        }
        if (state == state.RIGHT) {
            x += 10;
            if(x > 0){
                x = 0;
                gameView.STOPPED = true;
            }
        }

        if(x <= -(gameView.endX - gameView.getWidth())){
            gameView.STOPPED = true;

        }
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, x, y, null);
    }
}
