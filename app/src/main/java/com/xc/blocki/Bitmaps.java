package com.xc.blocki;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Aaron on 2016-05-17.
 */
public class Bitmaps {  //all Bitmaps only need to be loaded once with this class
    Bitmap ground;
    Bitmap enemy1;
    Bitmap enemy2;
    Bitmap coin;
    Bitmap finishLine;
    Bitmap idleLeft;
    Bitmap shootLeft;
    Bitmap idleRight;
    Bitmap shootRight;
    Bitmap bullet;

    public Bitmaps(Context context, int getWidth, int getHeight){
        int width = getWidth/10;
        int height = getHeight/6;
        int bullet = getWidth/30;
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.ground);
        ground  =  Bitmap.createScaledBitmap(tmp, width, height, false);
        tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy1);
        enemy1 =  Bitmap.createScaledBitmap(tmp, width, height, false);
        tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy2);
        enemy2 =  Bitmap.createScaledBitmap(tmp, width, height, false);
        tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_left);
        idleLeft = Bitmap.createScaledBitmap(tmp, width, height, false);
        tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.shoot_left);
        shootLeft = Bitmap.createScaledBitmap(tmp, width, height, false);
        tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_right);
        idleRight = Bitmap.createScaledBitmap(tmp, width, height, false);
        tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.shoot_right);
        shootRight = Bitmap.createScaledBitmap(tmp, width, height, false);
        tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.dogecoin);
        coin =  Bitmap.createScaledBitmap(tmp, width, height, false);
        tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.finish);
        finishLine =  Bitmap.createScaledBitmap(tmp, width, height, false);
        tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
        this.bullet = Bitmap.createScaledBitmap(tmp, bullet, bullet, false);
    }
}
