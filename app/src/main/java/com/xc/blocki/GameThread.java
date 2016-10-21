package com.xc.blocki;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by Aaron on 2016-05-03.
 */
public class GameThread extends Thread {
    boolean gameLoaded;
    GameView gv;
    int level;
    private GameState gameState;
    public enum GameState{INIT, LOADING, RUNNING, OVER, ENDGAME, REVIVE};

    public GameThread(GameView gv) {
        this.gv=gv;
        gameLoaded = false;
        level = 1;
    }

    public void run() {
        SurfaceHolder sh = gv.getHolder();
        Canvas c;
        gameState = GameState.INIT;
        while( !Thread.interrupted() ) {
            switch(gameState){
                case INIT:
                    c = sh.lockCanvas(null);
                    gv.draw(c);
                    sh.unlockCanvasAndPost(c);
                    gv.loadGame(1);
                    gameLoaded = true;
                    setGameState(GameState.RUNNING);
                    break;
                case LOADING:
                    if (level > 3){
                        setGameState(GameState.ENDGAME);
                        break;
                    }
                    c = sh.lockCanvas(null);
                    gv.draw(c);
                    sh.unlockCanvasAndPost(c);
                    gv.loadGame(level);
                    gameLoaded = true;
                    setGameState(GameState.RUNNING);
                    break;
                case RUNNING:
                    c = sh.lockCanvas(null);
                    try {
                        synchronized (sh) {
                            gv.draw(c);
                            //Log.d("Draw", "Frame Complete");
                        }
                    } catch (Exception e) {
                    } finally {
                        if (c != null) {
                            sh.unlockCanvasAndPost(c);
                        }
                    }
                    // Set the frame rate by setting this delay
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        // Thread was interrupted while sleeping.
                        return;
                    }
                    break;
                case REVIVE:
                case ENDGAME:
                case OVER:
                    c = sh.lockCanvas(null);
                    try {
                        synchronized (sh) {
                            gv.draw(c);
                        }
                    } catch (Exception e) {
                    } finally {
                        if (c != null) {
                            sh.unlockCanvasAndPost(c);
                        }
                    }
                    try {
                        if (getGameState() == GameState.REVIVE) {
                            Thread.sleep(900);
                            setGameState(GameThread.GameState.LOADING);
                        }
                        else{
                            Thread.sleep(100);
                        }
                    } catch (InterruptedException e) {
                        // Thread was interrupted while sleeping.
                        return;
                    }
                    break;
            }
        }
    }

    public void setGameState(GameState gameState){
        this.gameState = gameState;
    }
    public GameState getGameState(){
        return gameState;
    }
}
