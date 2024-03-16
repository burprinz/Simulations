package de.luiskun.games;

import de.luiskun.apps.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public abstract class Game extends Application {

    public Game() {
        super();

    }

    public abstract void update();

    @Override
    public abstract void paintComponent(Graphics g);
}
