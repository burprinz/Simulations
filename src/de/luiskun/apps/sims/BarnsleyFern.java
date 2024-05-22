package de.luiskun.apps.sims;

import de.luiskun.apps.Application;
import de.luiskun.tools.Vektor;
import de.luiskun.ui.Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class BarnsleyFern extends Application {

    List<Vektor> points;
    boolean first = true;

    Vektor pos;

    int r = 1;

    public BarnsleyFern() {
        pos = new Vektor();
        points = new ArrayList<>();
        points.add(pos);
    }

    @Override
    public void update() {
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, Screen.WIDTH, Screen.HEIGHT);
        g2.setColor(Color.WHITE);

        for (int i = 0; i < 500; i++) {
            double nextX = 0, nextY = 0;

            Random r = new Random();
            double prob = r.nextDouble();

            if (prob < 0.01) {
                nextX = 0;
                nextY = 0.16 * pos.y;
            } else if (prob < 0.86) {
                nextX = 0.85 * pos.x + 0.04 * pos.y;
                nextY = -0.04 * pos.x + 0.85 * pos.y + 1.6;
            } else if (prob < 0.93) {
                nextX = 0.2 * pos.x  - 0.26 * pos.y;
                nextY = 0.23 * pos.x + 0.22 * pos.y + 1.6;
            } else {
                nextX = -0.15 * pos.x + 0.28 * pos.y;
                nextY = 0.26 * pos.x + 0.24 * pos.y + 0.44;
            }


            pos = new Vektor(nextX, nextY);
            points.add(new Vektor(map(nextX, -4, 4, 0, WIDTH), map(nextY, 0, 8, HEIGHT, 0) - HEIGHT/4));
        }

        for (int i = 0; i < points.size(); i++) {
            Vektor sc = points.get(i);
            g2.fillOval(sc.xAsInt() - r, sc.yAsInt() - r + HEIGHT/2, 2*r, 2*r);
        }

    }

    double map (double value, double a, double b, double c, double d) {
        // first map value from (a..b) to (0..1)
        value = (value - a) / (b - a);
        // then map it from (0..1) to (c..d) and return it
        return c + value * (d - c);
    }
}
