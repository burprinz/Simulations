package de.luiskun.games.spaceblaster;

import de.luiskun.Vektor;
import de.luiskun.apps.Application;

import java.awt.*;
import java.util.Random;

public class Comet {

    public double radius;
    public int amountC;

    private Vektor[] corners;

    public Vektor pos;

    private double angle;

    private Vektor velocity;

    public boolean split;

    public Comet(double radius, int amountC, Vektor pos, Vektor velocity, boolean split) {
        this.radius = radius;
        this.amountC = amountC;
        this.pos = pos;
        this.velocity = velocity;
        this.split = split;

        corners = new Vektor[amountC];

        Random r = new Random();

        double angleJump = (Math.PI*2) / amountC;

        for (int i = 0; i < amountC; i++) {
            double angle = i*angleJump;
            double rad = radius - (r.nextDouble()*(radius/3.5d));
            Vektor v = Vektor.createFromAngle(angle);
            v.mult(rad);

            corners[i] = v;
        }

    }

    public void update() {
        pos.add(velocity);

    }

    public void draw(Graphics2D g2) {

        g2.setColor(Color.YELLOW);
        int[] x = new int[amountC];
        int[] y = new int[amountC];
        for (int i = 0; i < amountC; i++) {
            Vektor v = corners[i];

            Vektor ang = Vektor.createFromAngle(angle);

            double xPos = pos.x + v.x;
            double yPos = pos.y + v.y;

            x[i] = (int) xPos;
            y[i] = (int) yPos;

        }


        g2.fillPolygon(x, y, amountC-1);

        g2.setColor(Color.PINK);
        g2.drawOval(pos.xAsInt() - (int) radius, pos.yAsInt() - (int) radius, (int) (2*radius), (int) (2*radius));
    }

}
