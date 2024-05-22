package de.luiskun.apps.games;

import de.luiskun.apps.Application;

import java.awt.*;

public abstract class Game extends Application {

    public Game() {
        super();

    }

    public abstract void update();

    @Override
    public abstract void paintComponent(Graphics g);
}
