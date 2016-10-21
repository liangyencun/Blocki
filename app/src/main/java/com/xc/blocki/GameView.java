package com.xc.blocki;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Aaron on 2016-05-03.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    public GameView(Context context) { super(context) ;
        this.context = context;
        getHolder (). addCallback(this);
        setFocusable(true);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int height = metrics.heightPixels/4;
        Bitmap tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftbutton);
        leftarrow = Bitmap.createScaledBitmap(tmp, height, height, false);
        tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightbutton);
        rightarrow = Bitmap.createScaledBitmap(tmp, height, height, false);
        tmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.shootbutton);
        shoot = Bitmap.createScaledBitmap(tmp, height, height, false);
        buttonSize = height;
        blocks = new ArrayList<>();
        enemies = new ArrayList<>();
    }

    Context context;
    boolean gameLoaded = false;
    GameThread gt;
    Bitmaps bitmaps;
    int score=0;
    int levelScore = 0;
    String scoreText;
    String livesText;
    Level level;
    Player player;
    Background background;
    boolean STOPPED = true;
    int endX; //rightmost position in game coordinates
    ArrayList<Block> blocks; //contains all blocks except player
    ArrayList<Enemy> enemies; //contains all enemies
    int maxNumOfBullet = 10; // you can set the maximum number of bullets
    int bulletDamage = 1;   //in case bullet damage needs to be modified
    Bullet[] bullet;
    int numOfBullet = 0;
    Bitmap leftarrow;
    Bitmap rightarrow;
    Bitmap shoot;
    Paint p = new Paint();
    int buttonSize;

    public void addBlock(Block block){
        blocks.add(block);
    }

    public Block getBlock(int i){
         return blocks.get(i);
    }

    public void loadGame(int gameLevel){
        if (gt.getGameState() == GameThread.GameState.INIT){
            bitmaps = new Bitmaps(context, getWidth(), getHeight());
        }
        //clear all fields
        blocks.clear();
        enemies.clear();
        if (bullet == null) {
            bullet = new Bullet[maxNumOfBullet];
        }
        else{
            for (int i=0; i < bullet.length; i++){
                bullet[i] = null;
            }
        }
        numOfBullet = 0;
        System.gc();    //call garbage collection to free any unnecessary objects
        //create level
        level = new Level(this);
        level.loadLevel(gameLevel);
        //bullet creation
        for(int i=0; i<maxNumOfBullet; i++) {
            bullet[numOfBullet] = new Bullet(this, getWidth(), getHeight(), player.hitbox.right, player.hitbox.centerY());
            numOfBullet++;
        }
        for (Block i : blocks){
            if (i instanceof Enemy){
                enemies.add((Enemy)i);
            }
        }
        background = new Background(getWidth(), getHeight(),context, this);
        loadTouchHandler();
        gt.setGameState(GameThread.GameState.RUNNING);
    }

    public void loadTouchHandler(){
        setOnTouchListener(new OnTouchListener() {
            int lastAction = -1;
            //todo: maybe add swipe detection for changing direction?
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                for (int i = 0; i < event.getPointerCount(); i++){
                    if (event.getX(i) > getWidth()/2) {
                        if (event.getActionMasked() == MotionEvent.ACTION_DOWN || event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
                            for (int x = 0; x < maxNumOfBullet; x++) {
                                if (bullet[x]!=null && !bullet[x].isShooting && bullet[x].getX() < getWidth() && bullet[x].getX() > 0) { //prevent bug with bullet being launched from offscreen
                                    bullet[x].setShooting(true, player.facingRight);
                                    Log.i("Bullet", bullet[x].getX() + "," + bullet[x].getY());
                                    player.drawShooting = 5; //draw 5 frames of player shooting image
                                    break;
                                }
                            }
                        }
                    }
                }
                switch (event.getActionIndex()) {
                    case 0:
                        switch (event.getActionMasked()) {
                            case MotionEvent.ACTION_DOWN:
                                Log.d("Log.debug", "ACTION_DOWN at X=" + Float.toString(event.getX()) + ", Y=" + Float.toString(event.getY()));
                                if (event.getX() > buttonSize && event.getX() < getWidth()/2) {
                                    player.setState(Block.State.RIGHT);
                                    background.setState(Block.State.LEFT);
                                    for (Block block : blocks) {
                                        block.setState(Block.State.LEFT);
                                    }
                                    lastAction = 0;
                                } else if (event.getX() < buttonSize){
                                    player.setState(Block.State.LEFT);
                                    background.setState(Block.State.RIGHT);
                                    for (Block block : blocks) {
                                        block.setState(Block.State.RIGHT);
                                    }
                                    lastAction = 1;
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                player.setState(Block.State.STOPPED);
                                background.setState(Block.State.STOPPED);
                                for (Block block : blocks) {
                                    block.setState(Block.State.STOPPED);
                                }
                                lastAction = -1;
                                break;
                            case MotionEvent.ACTION_POINTER_UP: //two buttons pressed, first one released
                                Log.d("Log.debug", "First button released");
                                if (event.getX(1) > buttonSize && event.getX(1) < getWidth()/2) {
                                    player.setState(Block.State.RIGHT);
                                    background.setState(Block.State.LEFT);
                                    for (Block block : blocks) {
                                        block.setState(Block.State.LEFT);
                                    }
                                } else if (event.getX(1) < buttonSize) {
                                    player.setState(Block.State.LEFT);
                                    background.setState(Block.State.RIGHT);
                                    for (Block block : blocks) {
                                        block.setState(Block.State.RIGHT);
                                    }
                                }
                                break;
                        }
                        return true;
                    case 1: //for handling two-touch events
                        switch (event.getActionMasked()) {
                            case MotionEvent.ACTION_POINTER_DOWN:
                                Log.d("Log.debug", "ACTION_POINTER_DOWN at X= " + Float.toString(event.getX(1)));
                                if (event.getX(1) < buttonSize && player.state == Block.State.RIGHT) { //cancel right movement
                                    player.setState(Block.State.STOPPED);
                                    background.setState(Block.State.STOPPED);
                                    for (Block block : blocks) {
                                        block.setState(Block.State.STOPPED);
                                    }
                                } else if (event.getX(1) > buttonSize && event.getX(1) < getWidth()/2 && player.state == Block.State.LEFT) { //cancel left movement
                                    player.setState(Block.State.STOPPED);
                                    background.setState(Block.State.STOPPED);
                                    for (Block block : blocks) {
                                        block.setState(Block.State.STOPPED);
                                    }
                                }
                                break;
                            case MotionEvent.ACTION_POINTER_UP:
                                switch (lastAction) {
                                    case 0:
                                        player.setState(Block.State.RIGHT);
                                        background.setState(Block.State.LEFT);
                                        for (Block block : blocks) {
                                            block.setState(Block.State.LEFT);
                                        }
                                        break;
                                    case 1:
                                        player.setState(Block.State.LEFT);
                                        background.setState(Block.State.RIGHT);
                                        for (Block block : blocks) {
                                            block.setState(Block.State.RIGHT);
                                        }
                                }
                                break;
                        }
                        return true;
                }
                return false;
            }
        });
    }

    public void addScore(int score){
        this.score+=score;
        this.levelScore+=score;
    }

    public void collisionDetection(){
        //if a bullet collides with a non-item block, bullet.isAlive = false
        for(int j=0; j<numOfBullet; j++){
            if (bullet[j].isShooting) {
                RectF b = new RectF(bullet[j].getX(), bullet[j].getY(), bullet[j].getX()+bullet[j].bulletWidth, bullet[j].getY()+bullet[j].bulletHeight);
                bulletDied:
                for (Block block : blocks){
                    if(RectF.intersects(b, block.hitbox)){
                        switch(block.type){
                            case GROUND:
                                Log.i("Bullet", "hit ground");
                                bullet[j].isAlive = false;
                                bullet[j].update(player.getX(), player.getY(), player.facingRight);
                                break bulletDied;
                            case ENEMY:
                                if (block.isAlive) {
                                    bullet[j].isAlive = false;
                                    bullet[j].update(player.getX(), player.getY(), player.facingRight);
                                    ((Enemy)block).decreaseHealth(bulletDamage);
                                    block.update();
                                    Log.i("Bullet", "hit enemy, enemy health at "+block.health);
                                    break bulletDied;
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    public void updateAI(){
        for (Enemy i : enemies){
            for (Block j : blocks){
                if (RectF.intersects(i.hitbox, j.hitbox) && i != j && j.type == Block.Type.GROUND && i.isAlive && j.isAlive){
                    if (i.movingRight){
                        i.x -= i.speedX;
                    }
                    else{
                        i.x += i.speedX;
                    }
                    i.movingRight = !i.movingRight;
                    break;
                }
            }
            i.updateAI();
        }
    }

    public void update(){
        scoreText = "Score: "+score;
        livesText = "Lives: "+player.health;
        player.update(background.x);
        Log.i("backgroundx", String.valueOf(background.x));
        if (gt.getGameState() == GameThread.GameState.OVER){
            return;
        }
        updateAI();
        if(!STOPPED) { background.update(); }
        if(!STOPPED){ for (Block block : blocks) { block.update(); } }

        collisionDetection(); //bullet collision detection
        for (int i = 0; i < numOfBullet; i++) {
            //variable xi used here for the initial location of the bullet
            if (player.facingRight){
                bullet[i].xi = player.hitbox.right;
            }
            else{
                bullet[i].xi = player.hitbox.left - bullet[i].margin;
            }
            bullet[i].update(bullet[i].xi, player.hitbox.centerY()-bullet[i].bulletHeight, player.facingRight);
        }

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        switch(gt.getGameState()){
            case RUNNING:
                update();
                background.draw(canvas);

                //canvas.drawRect(player.hitbox, p); //for debug
                for (Block block : blocks){
                    if (block.isAlive)
                        block.draw(canvas);
                }
                for (int i = 0; i < numOfBullet; i++) {
                    bullet[i].draw(canvas);
                }
                player.draw(canvas);
                p.setTextSize(getHeight()/12);
                p.setAntiAlias(true);
                p.setColor(Color.BLACK);
                p.setAlpha(255);
                p.setTextAlign(Paint.Align.LEFT);
                p.setFakeBoldText(false);
                canvas.drawText(scoreText, 10, getHeight()/12+5, p);
                p.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(livesText, getWidth()-10, getHeight()/12+5, p);

                p.setAlpha(200);
                int buttonrow = getHeight()-buttonSize-10;

                canvas.drawBitmap(leftarrow, 10, buttonrow, p);
                canvas.drawBitmap(rightarrow, buttonSize+10, buttonrow, p);
                canvas.drawBitmap(shoot, getWidth()-buttonSize-10, buttonrow, p);

                break;
            case INIT:
                p.setTextSize(getHeight()/6);
                p.setAntiAlias(true);
                p.setColor(Color.WHITE);
                p.setTextAlign(Paint.Align.CENTER);
                p.setAlpha(255);
                canvas.drawText("Loading Level "+gt.level, getWidth()/2, getHeight()/2, p);
                Log.d("draw", "init");
                break;
            case LOADING:
                levelScore=0;
                p.setTextSize(getHeight()/6);
                p.setAntiAlias(true);
                p.setColor(Color.WHITE);
                p.setTextAlign(Paint.Align.CENTER);
                p.setAlpha(255);
                canvas.drawText("Loading Level "+gt.level, getWidth()/2, getHeight()/2, p);
                break;
            case OVER:
                canvas.drawColor(Color.BLACK);
                p.setTextSize(getHeight()/8);
                p.setAntiAlias(true);
                p.setColor(Color.WHITE);
                p.setTextAlign(Paint.Align.CENTER);
                p.setFakeBoldText(true);
                canvas.drawText("Game Over!", getWidth()/2, getHeight()/2, p);
                p.setTextSize(getHeight()/12);
                canvas.drawText(scoreText, getWidth()/2, getHeight()*3/4, p);
                break;
            case ENDGAME:
                canvas.drawColor(Color.BLACK);
                p.setTextSize(getHeight()/8);
                p.setAntiAlias(true);
                p.setColor(Color.WHITE);
                p.setTextAlign(Paint.Align.CENTER);
                p.setFakeBoldText(true);
                canvas.drawText("You Win!", getWidth()/2, getHeight()/2, p);
                p.setTextSize(getHeight()/12);
                canvas.drawText(scoreText, getWidth()/2, getHeight()*3/4, p);
                break;
            case REVIVE:
                score -= levelScore;
                livesText = "Lives Left: "+player.health;
                canvas.drawColor(Color.BLACK);
                p.setTextSize(getHeight()/8);
                p.setAntiAlias(true);
                p.setColor(Color.WHITE);
                p.setTextAlign(Paint.Align.CENTER);
                p.setFakeBoldText(true);
                canvas.drawText("You Died!", getWidth()/2, getHeight()/2, p);
                p.setTextSize(getHeight()/12);
                canvas.drawText(livesText, getWidth()/2, getHeight()*3/4, p);
                break;
        }
    }

    @Override
    public void surfaceCreated ( SurfaceHolder holder ) {
        // Launch animator thread
        if (!gameLoaded) {
            gt = new GameThread(this);
            gt.start();
            gameLoaded = true;
        }
        Log.d("Load", "surfaceView/Thread");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
