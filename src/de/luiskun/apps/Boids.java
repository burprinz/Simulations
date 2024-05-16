package de.luiskun.apps;

import de.luiskun.apps.classes.Boid;
import de.luiskun.menu.Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Boids extends Application {

    List<Boid> flock;

    public Boids() {

        flock = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            flock.add(new Boid());
        }

    }

    @Override
    public void update() {
        for (Boid b : flock) {
            b.edges();
            b.flock(flock);
            b.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, Screen.WIDTH, Screen.HEIGHT);

        for (Boid b : flock) {
            b.draw(g2);
        }
    }
}
