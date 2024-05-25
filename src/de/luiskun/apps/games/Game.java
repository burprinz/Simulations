package de.luiskun.apps.games;

import de.luiskun.apps.Application;

import java.awt.*;

public abstract class Game extends Application {

    protected static final int MENUSTATE = 0;
    protected static final int GAMESTATE = 1;
    protected static final int GAMEOVERSTATE = 2;

    protected int gamestate = 0;

    public Game() {
        super();

    }


    public abstract void gameOver(int score);
    public abstract void update();

    @Override
    public abstract void paintComponent(Graphics g);
}
