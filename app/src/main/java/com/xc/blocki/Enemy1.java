package com.xc.blocki;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by Aaron on 2016-05-03.
 */
public class Enemy1 extends Enemy {

    public Enemy1(int xPos, int yPos, int xSpeed, int ySpeed, int gravity, int health,
                  int getWidth, int getHeight, Context context, GameView gameView) {
        super(xPos, yPos, 1, 1, gravity, health, getWidth, getHeight, context, gameView);
        bitmap = gameView.bitmaps.enemy1;
        this.health = 2;
        movingRight = true;
    }

    public void update(){
        super.update();
        if (health <= 0){
            if (isAlive)
                gameView.addScore(1);
            isAlive = false;
        }
    }

    public void updateAI(){
        if (movingRight){
            x += speedX;
        }
        else{
            x -= speedX;
        }
        boolean onGround = false;
        RectF tmp = new RectF(x, y+gravity, x+width, y+gravity+height);
        for (Block i : gameView.blocks){
            if (RectF.intersects(tmp, i.hitbox) && i.type == Type.GROUND) {
                onGround = true;
            }
        }
        if (!onGround) {
            y += gravity;
        }
        hitbox.set(x, y, x+width, y+height);
    }
}
