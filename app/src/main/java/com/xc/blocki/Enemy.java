package com.xc.blocki;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by Aaron on 2016-05-17.
 */
public abstract class Enemy extends Block {
    public Enemy(int xPos, int yPos, int xSpeed, int ySpeed, int gravity, int health,
                  int getWidth, int getHeight, Context context, GameView gameView) {
        super(xPos, yPos, xSpeed, ySpeed, gravity, health, getWidth, getHeight, context, gameView);
        type = Type.ENEMY;
    }

    boolean movingRight;

    @Override
    public void update() {
        if (isAlive) {
            if (state == State.LEFT) {
                x -= 10;
            }
            if (state == State.RIGHT) {
                x += 10;
            }
            hitbox.set(x, y, x+width, y+height);
        }
    }

    public abstract void updateAI();

    @Override
    public void setState(State state) {
        this.state = state;
    }

    public void decreaseHealth(int amount){
        health-=amount;
        Log.i("bullet", "health decreased by "+amount);
    }

    @Override
    public void draw(Canvas canvas) {
        if (isAlive){
            canvas.drawBitmap(bitmap, x, y, null);
        }
    }
}
