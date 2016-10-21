package com.xc.blocki;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by Aaron on 2016-05-03.
 */
public class Player extends Block {
    Bitmap shootRight;
    Bitmap shootLeft;
    Bitmap idleRight;
    Bitmap idleLeft;
    int drawShooting;
    boolean facingRight;

    public Player(int xPos, int yPos, int xSpeed, int ySpeed, int gravity, int health,
                  int getWidth, int getHeight, Context context, GameView gameView) {
        super(xPos, yPos, xSpeed, ySpeed, gravity, health, getWidth, getHeight,context, gameView);
        type = Type.PLAYER;
        idleLeft = gameView.bitmaps.idleLeft;
        shootLeft = gameView.bitmaps.shootLeft;
        idleRight = gameView.bitmaps.idleRight;
        shootRight = gameView.bitmaps.shootRight;
        drawShooting = 0;
        facingRight = true;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    public void loseLife(){
        if (--health <= 0){
            isAlive = false;
        }
        else{
            gameView.gt.setGameState(GameThread.GameState.REVIVE);
        }
    }

    @Override
    public void update() {

    }

    public void update(int backgroundX) {
        collisionDetection();
        if (!isAlive){
            gameView.gt.setGameState(GameThread.GameState.OVER);
        }
        if (state == State.LEFT) {
            if(gameView.STOPPED) {
                x -= speedX;
            }
            if(x <= 0){
                x = 0;
            }
            facingRight = false;
        }
        if (state == State.RIGHT){
            if(gameView.STOPPED) {
                x += speedX;
            }
            if(x > getWidth - width) {
                x = getWidth - width;
            }
            facingRight = true;
        }
        boolean onGround = false;
        RectF tmp = new RectF(x, y+gravity, x+width, y+gravity+height);
        for (Block i : gameView.blocks){
            if (RectF.intersects(tmp, i.hitbox) && i.type == Type.GROUND) {
                onGround = true;
            }
        }
        if(x >= getWidth/4 && backgroundX == 0) {
            gameView.STOPPED = false;
        }
        if(x >= getWidth/4 && onGround && (backgroundX > -(gameView.endX-gameView.getWidth()))){
            gameView.STOPPED = false;
        }

        if (!onGround) {
            y += gravity;
        }
        hitbox.set(x, y, x+width, y+height);
    }

    public void collisionDetection(){
        RectF tmp;
        switch(state){
            case RIGHT:
                tmp = new RectF(x+speedX, y, x+speedX+width, y+height);
                break;
            case LEFT:
                tmp = new RectF(x-speedX, y, x-speedX+width, y+height);
                break;
            default: //STOPPED
                tmp = hitbox;
                break;
        }
        blockLoop:
        for (Block i : gameView.blocks){
            if (RectF.intersects(tmp, i.hitbox)){
                switch (i.type){
                    case ENEMY:
                        //for now player dies immediately upon hitting an enemy
                        if (i.isAlive) {
                            loseLife();
                            break blockLoop;
                        }
                        else
                            break;
                    case GROUND:
                        //player cannot move and stops
                        setState(State.STOPPED);
                        gameView.STOPPED=true;
                        break;
                    case ITEM:
                        if (i instanceof FinishLine){
                            gameView.gt.setGameState(GameThread.GameState.LOADING);
                            gameView.gt.gameLoaded = false;
                            gameView.gt.level++;
                            break blockLoop;
                        }
                        if (i instanceof Coin && i.isAlive){
                            gameView.addScore(5);
                            i.isAlive = false;
                        }
                        break;
                }
            }
        }
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    @Override
    public void draw(Canvas canvas) {
        if (drawShooting-- > 0){
            if (facingRight) {
                canvas.drawBitmap(shootRight, x, y, null);
            }
            else{
                canvas.drawBitmap(shootLeft, x, y, null);
            }
        }
        else {
            if (facingRight) {
                canvas.drawBitmap(idleRight, x, y, null);
            }
            else{
                canvas.drawBitmap(idleLeft, x, y, null);
            }
        }
        //Log.d("player", "Draw");
    }
}
