package com.xc.blocki;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Taken and modified from SpaceyInvaders
 */
public class Bullet {

    int width; //screenWidth
    int height; //screenHeight
    int bulletWidth;
    int bulletHeight;
    int margin;
    float xi, yi; // initial position of bullets
    float x, y;
    float vx; //speed of bullets
    Bitmap bitmapBullet;
    boolean isShooting;
    boolean isAlive;
    boolean right;

    public Bullet(GameView gameView, int width, int height, float xPosition, float yPosition) {
        bulletWidth = width/30;
        bulletHeight = bulletWidth;
        bitmapBullet = gameView.bitmaps.bullet;
        vx = 15;
        this.width = width;
        this.height = height;
        margin = bulletWidth/2;
        xi = xPosition;
        yi = yPosition;
        x = xi;
        y = yi;
        isShooting = false;
        isAlive = true;
        Log.d("Load", "Bullet");
    }

    public void draw(Canvas c){
        Paint p = new Paint();
        if (isShooting) {
            c.drawBitmap(bitmapBullet, x, y, p);
        }
    }

    // @param xPosition and yPosition keeps track of the player's (gun) current position
    // @param curRight is used when the bullet collides, can be moved to bullet collisionDetection
    public void update(float xPosition, float yPosition, boolean curRight){
        y = yPosition;
        if (isShooting) {
            float tmpX;
            if (right) {
                tmpX = x + vx;
                if (tmpX > width || (!isAlive)) { //isAlive is linked to collision detection, which is in GameView's update()
                    x = xPosition;
                    setShooting(false, curRight);
                }
            }
            else{
                tmpX = x - vx;
                if (tmpX < 0 || (!isAlive)) { //isAlive is linked to collision detection, which is in GameView's update()
                    x = xPosition;
                    setShooting(false, curRight);
                }
            }
            x = tmpX;
        }
        else{
            x = xPosition;
            isAlive = true;
        }
    }
    // boolean right indicates whether the shot was taken when the player was facing right
    public void setShooting(boolean shooting, boolean right){
        isShooting = shooting;
        this.right = right;
    }

    public float getX(){return x;}
    public float getY(){return y;}
}
