package de.luiskun.fallingSand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class Screen extends JPanel implements Runnable{

    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;

    private Thread thread;
    private int fps = 60;
    private int w = 8;
    public int cols, rows;

    public int[][] grid;

    private MouseHandler mh = new MouseHandler();
    public Screen() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDoubleBuffered(true);
        this.setLayout(null);
        this.addMouseListener(mh);
        cols = WIDTH / w;
        rows = HEIGHT / w;
        grid = new int[cols][rows];
        grid[20][10] = 1;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);


        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                if (grid[x][y] > 0) {
                    switch (grid[x][y]) {
                        case 1:
                            g2.setColor(Color.WHITE);
                            break;
                    }
                    g2.fillRect(x * w, y * w, w, w);
                }
                g2.setColor(Color.WHITE);
               // g2.drawRect(x * w, y * w, w, w);
            }
        }

    }

    private void update() {

        int[][] nextGrid = new int[cols][rows];

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                int state = grid[x][y];
                if (state > 0) {

                    int rndDir = 1;
                    Random r = new Random();
                    if(r.nextBoolean()) {
                        rndDir *= -1;
                    }

                    if (y == rows - 1) {
                        // Boden erreicht
                        nextGrid[x][y] = state;
                    } else if(grid[x][y+1] == 0) {
                        // Unten Frei
                        nextGrid[x][y+1] = state;
                    } else if (x - rndDir >= 0 && grid[x-1][y+1] == 0) {
                        // Unten Links im Def bereich und Frei
                        nextGrid[x - rndDir][y + 1] = state;
                    } else if (x + rndDir < cols && grid[x+1][y+1] == 0) {
                        // Unten Rechts im Def bereich und Frei
                        nextGrid[x + rndDir][y + 1] = state;
                    } else {
                        // Kein Weg nach unten
                        nextGrid[x][y] = state;
                    }
                }
            }
        }

        if (mh.pressed) {
            Point b = this.getMousePosition();

            if (b != null) {
                int mCol = (int) Math.floor(b.getX() / w);
                int mRow = (int) Math.floor(b.getY() / w);
                nextGrid[mCol][mRow] = 1;
            }
        }

        grid = nextGrid;

    }

    public void startThread() {
        thread = new Thread(this);
        thread.start();
    }



    @Override
    public void run() {
        double drawInterval = 1000000000 / fps; // 0.0166s
        double delta = 0;
        double lastTime = System.nanoTime();
        double currentTime;
        while (thread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }


    public class MouseHandler implements MouseListener {

        public boolean pressed = false;

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            pressed = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            pressed = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

}
