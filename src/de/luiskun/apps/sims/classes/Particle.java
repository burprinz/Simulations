package de.luiskun.apps.sims.classes;

import de.luiskun.tools.Vektor;

import java.awt.*;
import java.util.Random;

public class Particle {

    public Vektor pos, vel, acc;
    public static final int minSpeed = 8, maxSpeed = 15;
    boolean exploder;

    double lifespan = 255;
    int fadingSpeed;


    public Particle(double x, double y, boolean exploder, int fadingSpeed, double impact) {
        this.exploder = exploder;
        this.fadingSpeed = fadingSpeed;

        Random r = new Random();
        pos = new Vektor(x,y);
        acc = new Vektor(0,0);



        if(!exploder) {
            vel = new Vektor(0,-minSpeed - r.nextInt(maxSpeed-minSpeed));
        } else {
            vel = Vektor.random();
            vel.mult(r.nextDouble()*impact);
        }

    }

    public void update() {
        if(exploder) {
            this.vel.mult(0.95);
            lifespan-=fadingSpeed;
        }
        this.vel.add(this.acc);
        this.pos.add(this.vel);
        this.acc.mult(0);
    }

    public void applyForce(Vektor forc) {
        acc.add(forc);
    }

    public void show(Graphics2D g2, Color c) {
        if(lifespan < 0 ) {
            lifespan = 0;
        }
        g2.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)lifespan));
        g2.fillOval(pos.xAsInt(), pos.yAsInt(), 5, 5);
    }

    public boolean done() {
        return lifespan <= 0;
    }

}
