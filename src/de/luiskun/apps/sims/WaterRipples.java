package de.luiskun.apps.sims;

import de.luiskun.apps.Application;
import de.luiskun.tools.Vektor;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class WaterRipples extends Application implements MouseListener {

    int watersize = 175;
    double[][] current = new double[watersize][watersize];
    double[][] previous = new double[watersize][watersize];
    double damping = 0.9;

    boolean moving;

    public WaterRipples() {



        addMouseListener(this);

    }

    @Override
    public void update() {

        if (moving) {
            Point mousePos = this.getMousePosition();

            if (mousePos != null) {
                Vektor v = new Vektor(mousePos);
                v.divide(HEIGHT/watersize);
                if (v.xAsInt() >= 0 && v.xAsInt() < watersize && v.yAsInt() >= 0 && v.yAsInt() < watersize) {
                    previous[v.xAsInt()][v.yAsInt()] = 255;
                }
            }
        }

        for (int y = 1; y < watersize-1; y++) {
            for (int x = 1; x < watersize-1; x++) {
                current[x][y] = ((previous[x-1][y] + previous[x+1][y] + previous[x][y-1] + previous[x][y+1]) / 2) - current[x][y];
                if (current[x][y] < 0) current[x][y] = 0;
                current[x][y] *= damping;
            }
        }

        double tmp;
        for (int y = 0; y < watersize; y++) {
            for (int x = 0; x < watersize; x++) {
                tmp = previous[x][y];
                previous[x][y] = current[x][y];
                current[x][y] = tmp;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        final int cellSize = HEIGHT / watersize;

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        for (int y = 1; y < watersize-1; y++) {
            for (int x = 1; x < watersize-1; x++) {
                g2.setColor(new Color((float)current[x][y]%1f, (float)current[x][y]%1f, (float)current[x][y]%1f));
                g2.fillRect(x*cellSize, y*cellSize, cellSize, cellSize);
            }
        }


    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        moving = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        moving = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
