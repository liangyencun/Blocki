package com.xc.blocki;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by Aaron on 2016-05-03.
 */
public abstract class Block {
    int x;
    int y;
    int width;
    int height;
    int speedX;
    int speedY;
    int gravity;
    int health;
    int getWidth; //screen width
    int getHeight;
    boolean isAlive;
    Context context;
    GameView gameView;
    RectF hitbox;
    State state;
    Type type;
    Bitmap bitmap;
    public enum State{STOPPED, RIGHT, LEFT, FALLING};
    public boolean onGround;
    public enum Type{PLAYER, ENEMY, GROUND, ITEM};

    public Block(int xPos, int yPos, int xSpeed, int ySpeed, int gravity, int health,
                 int getWidth, int getHeight, Context context) {
        this(xPos, yPos, xSpeed, ySpeed, gravity, health, getWidth, getHeight, context, null);
    }

    public Block(int xPos, int yPos, int xSpeed, int ySpeed, int gravity, int health,
                 int getWidth, int getHeight, Context context, GameView gameView) {
        x = xPos;
        y = yPos;
        speedX = xSpeed;
        speedY = ySpeed;
        this.gravity = gravity;
        this.health = health;
        this.getWidth = getWidth;
        this.getHeight = getHeight;
        this.width = getWidth/10;
        this.height = getHeight/6;
        this.context = context;
        state = State.STOPPED;
        this.gameView = gameView;
        isAlive = true;
        hitbox = new RectF(xPos, yPos, xPos+width, yPos+height);
    }

    public Block(){
    }

    public abstract void setState(State state);

    public abstract void update();

    public abstract void draw(Canvas canvas);

}
