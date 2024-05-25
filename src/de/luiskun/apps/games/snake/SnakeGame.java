package de.luiskun.apps.games.snake;

import de.luiskun.apps.Application;
import de.luiskun.apps.games.Game;
import de.luiskun.apps.sims.Mandelbrot;
import de.luiskun.tools.FileHandler;
import de.luiskun.tools.Vektor;
import de.luiskun.ui.uiparts.Updatable;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGame implements Updatable {

    BufferedImage apple;

    Player p;
    Vektor food;

    public SnakeGame(Snake s) {
        p = new Player(s);

        food = new Vektor();

        apple = FileHandler.SetupImage("res/games/snake/apple.png", Snake.cellSize, Snake.cellSize);

        foodLocation();

    }

    private void foodLocation() {
        Random r = new Random();

        int pos = r.nextInt(Snake.gridS*Snake.gridS - p.body.size());

        int x = 0, y = 0;

        while (pos > 0) {
            if (p.isFree(new Vektor(x, y))) {
                pos--;
            }
            if (pos == 0) {
                break;
            }
            x++;
            if (x >= Snake.gridS) {
                x = 0;
                y++;
            }
        }

        food = new Vektor(x, y);
    }

    @Override
    public void update() {
        p.update();
        if (Vektor.isSameInt(food, p.body.get(0))) {
            p.grow();
            foodLocation();
        }
    }

    @Override
    public void draw(Graphics2D g2) {

        g2.setColor(Color.DARK_GRAY);
        for (int y = 0; y < Snake.gridS; y++) {
            for (int x = 0; x < Snake.gridS; x++) {
                g2.drawRect(x*Snake.cellSize, y*Snake.cellSize, Snake.cellSize, Snake.cellSize);
            }
        }

        if (food != null) {
            g2.setColor(Color.ORANGE);
            g2.drawImage(apple, food.xAsInt()*Snake.cellSize, food.yAsInt()*Snake.cellSize, null);
        }

        p.draw(g2);
    }
}
