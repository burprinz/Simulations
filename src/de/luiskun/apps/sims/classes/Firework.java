package de.luiskun.apps.sims.classes;

import de.luiskun.tools.Vektor;
import de.luiskun.apps.sims.Fireworks;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Firework {

    final int am;

    Vektor gravity;

    public Particle firework;
    boolean exploded = false;

    List<Particle> particles;

    Color c;

    int fadingSpeed;
    double impact;

    public Firework(double grav, int amParticles, int fadingSpeed, double impact) {
        gravity = new Vektor(0, grav);
        this.am = amParticles;
        this.fadingSpeed = fadingSpeed;
        this.impact = impact;

        Random r = new Random();
        c = new Color(Color.HSBtoRGB(r.nextFloat(), 1, 1));
        firework = new Particle(r.nextInt(Fireworks.WIDTH), Fireworks.HEIGHT, false, fadingSpeed, impact);
    }

    public void update() {
        if(!exploded) {
            firework.applyForce(gravity);
            firework.update();

            if (this.firework.vel.y >= 0) {
                exploded = true;
                explode();
            }
        }

        if (particles != null) {
            for(int i = particles.size()-1; i >= 0; i--) {
                particles.get(i).applyForce(gravity);
                particles.get(i).update();
                if(particles.get(i).done()) {
                    particles.remove(i);
                }
            }
        }

    }

    public void show(Graphics2D g2) {
        if(!exploded) {
            firework.show(g2, c);
        }
        if (particles != null) {
            for(int i = 0; i < particles.size(); i++) {
                particles.get(i).show(g2, c);
            }
        }
    }

    private void explode() {
        particles = new ArrayList<>();
        for(int i = 0; i < am; i++) {
            particles.add(new Particle(this.firework.pos.x, this.firework.pos.y, true, fadingSpeed, impact));
        }
    }

    public boolean done() {
        return exploded && this.particles.size() == 0;
    }

}
