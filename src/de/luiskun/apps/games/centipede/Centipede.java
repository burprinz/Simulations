package de.luiskun.apps.games.centipede;

import de.luiskun.apps.games.Game;
import de.luiskun.apps.sims.Mandelbrot;
import de.luiskun.tools.FileHandler;
import de.luiskun.tools.Geometry;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Centipede extends Game implements MouseListener {

    protected static final int gridS = 20;
    protected static final int cellS = HEIGHT/gridS;
    CentipedeGame game;
    Font pixelfont;

    int score = 0;

    public Centipede() {
        pixelfont = FileHandler.getPixelFont();

        this.addMouseListener(this);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (Mandelbrot.class) {
                    switch (ke.getID()) {
                        case KeyEvent.KEY_PRESSED:
                            if (gamestate == MENUSTATE) {
                                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                                    startGame();
                                }

                            } else if (gamestate == GAMEOVERSTATE) {
                                if (ke.getKeyCode() == KeyEvent.VK_ENTER) gamestate = MENUSTATE;
                            } else if (gamestate == GAMESTATE) {
                                if (ke.getKeyCode() == KeyEvent.VK_SPACE) game.useUlt();
                            }
                            break;
                    }
                    return false;
                }
            }
        });
    }

    private void startGame() {
        game = new CentipedeGame(this);
        gamestate = GAMESTATE;
    }

    @Override
    public void gameOver(int score) {
        this.score = score;
        gamestate = GAMEOVERSTATE;
        this.game = null;
    }

    @Override
    public void update() {
        super.update();
        if (gamestate == GAMESTATE) game.update(this.getMousePosition());
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setFont(pixelfont.deriveFont(Font.BOLD, 30));

        g2.setColor(Color.BLACK);
        g2.fillRect(0,0,WIDTH,HEIGHT);

        if (gamestate == MENUSTATE) {
            drawMenu(g2);
        } else if (gamestate == GAMESTATE) {
            game.draw(g2);
        } else if (gamestate == GAMEOVERSTATE) {
            g2.setColor(Color.WHITE);
            String text = "GAME OVER";
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50));
            int x, y = Game.HEIGHT/2 - 100;
            x = Geometry.getXForCenteredText(g2, text, WIDTH);
            g2.drawString(text, x, y);
            y += 40;
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20));
            text = "Score: " + score;
            x = Geometry.getXForCenteredText(g2, text, WIDTH);
            g2.drawString(text, x, y);
            y += 70;
            text = "ENTER - MENU";
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20));
            x = Geometry.getXForCenteredText(g2, text, WIDTH);
            g2.drawString(text, x, y);
        }

    }

    private void drawMenu(Graphics2D g2) {
        g2.setColor(Color.WHITE);

        int x, y = Game.HEIGHT/2 - 50;
        String text;

        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30));

        text = "ENTER - PLAY";
        x = Geometry.getXForCenteredText(g2, text, Game.WIDTH);
        g2.drawString(text, x, y);

        text = "Z - QUIT";
        y += 100;
        x = Geometry.getXForCenteredText(g2, text, Game.WIDTH);
        g2.drawString(text, x, y);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (game != null) game.firing = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (game != null) game.firing = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
