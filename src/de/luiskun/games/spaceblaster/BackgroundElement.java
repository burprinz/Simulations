package de.luiskun.games.spaceblaster;

import de.luiskun.Vektor;
import de.luiskun.apps.Application;

import java.awt.*;
import java.util.List;

public class BackgroundElement {

    public Vektor pos;
    public int radius;

    // X = speed, Y = angle
    public Vektor velocity;
    int gamma;

    public BackgroundElement(int radius, int gamma, double angle, Vektor pos) {
        this.radius = radius;
        this.gamma = gamma;
        velocity = new Vektor((double) radius / 5d, angle);
        this.pos = pos;
    }

    public void update() {
        velocity.y = Vektor.getDeltaDiff(velocity.y, 0.2, 0, 365);
        Vektor vel = Vektor.createFromAngle(velocity.y);
        vel.mult(velocity.x);
        pos.add(vel);

        if (pos.x > Application.WIDTH) {
            pos.x = 0 - 2*radius;
        }

        if (pos.x + 2*radius < 0) {
            pos.x = Application.WIDTH;
        }

        if (pos.y > Application.HEIGHT) {
            pos.y = 0 - 2*radius;
        }

        if (pos.y + 2*radius < 0) {
            pos.y = Application.HEIGHT;
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(255,255,255,gamma));
        g2.fillOval(pos.xAsInt()-radius, pos.yAsInt()-radius, 2*radius, 2*radius);
    }

}
