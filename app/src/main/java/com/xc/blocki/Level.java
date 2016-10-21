package com.xc.blocki;

import android.content.Context;

/**
 * Created by Aaron on 2016-05-04.
 */
public class Level {
    GameView gameView;
    final int BLOCK_WIDTH;
    final int BLOCK_HEIGHT;

    public Level(GameView gameView){
        this.gameView = gameView;
        BLOCK_HEIGHT = gameView.getHeight()/6; //all maps are 6 blocks high
        BLOCK_WIDTH = gameView.getWidth()/10;
    }

    public String createLevel(int level){
        switch(level){
            case 1:
                return  "                         g\n"+
                        " p                       g\n"+
                        "gg    2         1        g\n"+
                        "ggggggggg  gg   ggg      g\n"+
                        "gg  c    1  1     c    f g\n"+
                        "gggggggggggggggggggggggggg\n";

            case 2:
                return  "                                 g\n"+
                        "  p                2   g1        g\n"+
                        "ggg  c 2    1 2 cggggggggggggg   g\n"+
                        "g    gggggggggg                  g\n"+
                        "gc   1    1 c           2   2  f g\n"+
                        "gggggggggggggggggggggggggggggggggg\n";

            case 3:
                return  " p               1      2 g      2   g           g\n"+
                        "gg    2 c       gggg   gggg   gggggggg           g\n"+
                        "g   ggg   2   c   1     c                        g\n"+
                        "g      gggggg  ggggggggggg c                     g\n"+
                        "gc   2     c      2    1    2        1    c    f g\n"+
                        "gggggggggggggggggggggggggggggggggggggggggggggggggg\n";
        }
        return null;
    }

    public void loadLevel(int level){
        String map = createLevel(level);
        int x=0;
        int y=0;
        for (int i = 0; i < map.length(); i++){
            char tmp = map.charAt(i);
            switch (tmp){
                case '\n':
                    y++;
                    gameView.endX = x*BLOCK_WIDTH;
                    x=0;
                    break;
                case ' ':
                    x++;
                    break;
                case 'f':
                    gameView.addBlock(new FinishLine(x*BLOCK_WIDTH, y*BLOCK_HEIGHT, gameView.getWidth(), gameView.getHeight(), gameView.context, gameView));
                    x++;
                    break;
                case 'g':
                    gameView.addBlock(new Ground(x*BLOCK_WIDTH, y*BLOCK_HEIGHT,
                            gameView.getWidth(), gameView.getHeight(), gameView.context, gameView));
                    x++;
                    break;
                case 'p':
                    if (gameView.player == null)
                        //2 lives
                        gameView.player = new Player(x*BLOCK_WIDTH, y*BLOCK_HEIGHT, 10, 10, 10, 2, gameView.getWidth(), gameView.getHeight(), gameView.context, gameView);
                    else {
                        gameView.player.setX(x * BLOCK_WIDTH);
                        gameView.player.setY(y*BLOCK_HEIGHT);
                    }
                    x++;
                    break;
                case '1':
                    gameView.addBlock(new Enemy1(x*BLOCK_WIDTH, y*BLOCK_HEIGHT, 1, 1, 10, 1,
                            gameView.getWidth(), gameView.getHeight(), gameView.context, gameView));
                    //speed and health not controlled here, overridden in Enemy1.java
                    x++;
                    break;
                case '2':
                    gameView.addBlock(new Enemy2(x*BLOCK_WIDTH, y*BLOCK_HEIGHT, 2, 2, 10, 1,
                            gameView.getWidth(), gameView.getHeight(), gameView.context, gameView));
                    //speed and health not controlled here, overridden in Enemy2.java
                    x++;
                    break;
                case 'c':
                    gameView.addBlock(new Coin(x*BLOCK_WIDTH, y*BLOCK_HEIGHT, gameView.getWidth(), gameView.getHeight(), gameView.context, gameView));
                    x++;
                    break;
            }
        }
    }
}
