package de.luiskun.games.spaceblaster;

import de.luiskun.Vektor;

import java.awt.*;

public class Projectile {

    Vektor pos;
    int radius;
    double speed = 3;
    double angle;

    public Projectile(int radius, Vektor pos, double angle) {
        this.radius = radius;
        this.pos = pos;
        this.angle = angle;
    }

    public void update() {
        Vektor vel = Vektor.createFromAngle(angle);
        vel.mult(speed);
        pos.add(vel);
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.GREEN);
        g2.drawOval(pos.xAsInt() - radius, pos.yAsInt() - radius, 2*radius, 2*radius);
    }

}
