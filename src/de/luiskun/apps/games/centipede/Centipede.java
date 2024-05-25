package de.luiskun.apps.games.centipede;

import de.luiskun.apps.games.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Centipede extends Game implements MouseListener {

    protected static final int gridS = 20;
    protected static final int cellS = HEIGHT/gridS;
    CentipedeGame game;
    public Centipede() {

        game = new CentipedeGame();
        this.addMouseListener(this);
    }

    @Override
    public void gameOver(int score) {

    }

    @Override
    public void update() {
        game.update(this.getMousePosition());
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0,0,WIDTH,HEIGHT);

        game.draw(g2);
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
