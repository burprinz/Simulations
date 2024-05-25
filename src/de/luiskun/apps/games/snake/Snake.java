package de.luiskun.apps.games.snake;

import de.luiskun.apps.Application;
import de.luiskun.apps.games.Game;
import de.luiskun.apps.sims.Mandelbrot;
import de.luiskun.tools.Geometry;
import de.luiskun.tools.Vektor;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Snake extends Game {

    public static final int cellSize = 50;
    public static final int gridS = HEIGHT/cellSize;
    String[] buttons = {"ENTER - PLAY", "Z - QUIT"};
    SnakeGame game;
    int score;

    public Snake() {

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (Mandelbrot.class) {
                    switch (ke.getID()) {
                        case KeyEvent.KEY_PRESSED:
                            if (gamestate == MENUSTATE) {
                                updateMenuKeys(ke);
                            } else if (gamestate == GAMESTATE) {
                                updateGameKeys(ke);
                            } else if (gamestate == GAMEOVERSTATE) {
                                if (ke.getKeyCode() == KeyEvent.VK_ENTER) gamestate = MENUSTATE;
                            }
                            break;
                    }
                    return false;
                }
            }
        });
    }

    public void startGame() {
        game = new SnakeGame(this);
    }

    @Override
    public void gameOver(int score) {
        this.score = score;
        gamestate = GAMEOVERSTATE;
    }

    private void updateGameKeys(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                game.p.dir = new Vektor(-1, 0);
                game.p.dirChanged = true;
                break;
            case KeyEvent.VK_RIGHT:
                game.p.dir = new Vektor(1, 0);
                game.p.dirChanged = true;
                break;
            case KeyEvent.VK_UP:
                game.p.dir = new Vektor(0, -1);
                game.p.dirChanged = true;
                break;
            case KeyEvent.VK_DOWN:
                game.p.dir = new Vektor(0, 1);
                game.p.dirChanged = true;
                break;
        }
    }

    private void updateMenuKeys(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                gamestate = GAMESTATE;
                startGame();
                break;
        }
    }

    @Override
    public void update() {
        if (gamestate == MENUSTATE) {
        } else if (gamestate == GAMESTATE) {
            if (game != null) game.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, Application.WIDTH, Application.HEIGHT);

        if (gamestate == MENUSTATE) {
            int x, y = Game.HEIGHT/2 - (buttons.length/2)*50;
            String text;

            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30));

            for (int i = 0; i < buttons.length; i++) {
                text = buttons[i];
                x = Geometry.getXForCenteredText(g2, buttons[i], Game.WIDTH);
                g2.drawString(text, x, y);
                y += 100;
            }
        } else if (gamestate == GAMESTATE) {
            if (game != null) game.draw(g2);
        } else if (gamestate == GAMEOVERSTATE) {
            g2.setColor(Color.WHITE);
            String text = "GAME OVER";
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50));
            int x, y = Game.HEIGHT/2 - 100;
            x = Geometry.getXForCenteredText(g2, text, WIDTH);
            g2.drawString(text, x, y);
            y += 40;
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15));
            text = "Score: " + score;
            x = Geometry.getXForCenteredText(g2, text, WIDTH);
            g2.drawString(text, x, y);
            y += 60;
            text = "ENTER - MENU";
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20));
            x = Geometry.getXForCenteredText(g2, text, WIDTH);
            g2.drawString(text, x, y);
        }
    }
}
