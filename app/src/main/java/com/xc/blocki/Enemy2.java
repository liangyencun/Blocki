package com.xc.blocki;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by Aaron on 2016-05-17.
 */
public class Enemy2 extends Enemy {
    public Enemy2(int xPos, int yPos, int xSpeed, int ySpeed, int gravity, int health,
                  int getWidth, int getHeight, Context context, GameView gameView) {
        super(xPos, yPos, 2, 2, gravity, health, getWidth, getHeight, context, gameView);
        type = Type.ENEMY;
        bitmap = gameView.bitmaps.enemy2;
        this.health = 3;
        movingRight = false;
    }

    public void update(){
        super.update();
        if (health <= 0){
            if (isAlive)
                gameView.addScore(2);
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
