package de.luiskun.apps.classes;

import de.luiskun.Vektor;
import de.luiskun.menu.Screen;

import java.awt.*;
import java.util.Random;
import java.util.List;

public class Boid {

    int radius = 8;
    Vektor posisiton, velocity, acceleration;
    double maxForce = 0.15;
    double maxSpeed = 2;

    double allignSlider = 0, cohesionSlider = 0, separationSlider = 0;

    public Boid() {

        Random r = new Random();

        this.posisiton = new Vektor(r.nextInt(Screen.WIDTH), r.nextInt(Screen.HEIGHT));
        this.velocity = new Vektor(r.nextDouble()*2-1, r.nextDouble()*2-1);
        this.acceleration = new Vektor();
    }

    public Vektor allign(List<Boid> boids) {
        final double perceptionRadius = 50;

        int total = 0;

        Vektor steering = new Vektor();
        for (Boid other : boids) {
            if (other != this && Vektor.dist(posisiton, other.posisiton) < perceptionRadius) {
                steering.add(other.velocity);
                total++;
            }
        }
        if (total > 0) {
            steering.divide(total);
            steering.sub(velocity);
            steering.limit(maxForce);
        }

        return steering;
    }

    public Vektor cohesion(List<Boid> boids) {
        final double perceptionRadius = 100;

        int total = 0;

        Vektor steering = new Vektor();
        for (Boid other : boids) {
            if (other != this && Vektor.dist(posisiton, other.posisiton) < perceptionRadius) {
                steering.add(other.posisiton);
                total++;
            }
        }
        if (total > 0) {
            steering.divide(total);
            steering.sub(posisiton);
            steering.sub(velocity);
            steering.limit(maxForce);
        }

        return steering;
    }

    public Vektor separation(List<Boid> boids) {
        final double perceptionRadius = 100;

        int total = 0;

        Vektor steering = new Vektor();
        for (Boid other : boids) {

            double d = Vektor.dist(posisiton, other.posisiton);

            if (other != this && d < perceptionRadius) {
                Vektor diff = new Vektor(posisiton);
                diff.sub(other.posisiton);
                diff.divide(d);
                steering.add(diff);
                total++;
            }
        }
        if (total > 0) {
            steering.divide(total);
            steering.sub(velocity);
            steering.limit(maxForce);
        }

        return steering;
    }

    public void edges() {
        if (posisiton.x - radius> Screen.WIDTH) {
            posisiton.x = 0 - radius;
        } else if (posisiton.x + radius < 0) {
            posisiton.x = Screen.WIDTH + radius;
        }
        if (posisiton.y - radius > Screen.HEIGHT) {
            posisiton.y = 0 - radius;
        } else if (posisiton.y + radius< 0) {
            posisiton.y = Screen.HEIGHT + radius;
        }
    }

    public void flock(List<Boid> boids) {
        Vektor allignment = allign(boids);
        Vektor cohesion = cohesion(boids);
        Vektor separation = separation(boids);

        separation.mult(4.5);

        acceleration.add(allignment);
        acceleration.add(cohesion);
        acceleration.add(separation);
    }

    public void update() {
        posisiton.add(velocity);
        velocity.add(acceleration);
        velocity.limit(maxSpeed);
        acceleration.set(0,0);

        allignSlider += 0.1;
        if (allignSlider > 5) {
            allignSlider = 0;
        }
        cohesionSlider += 0.1;
        if (cohesionSlider > 5) {
            cohesionSlider = 0;
        }
        separationSlider += 0.1;
        if (separationSlider > 5) {
            separationSlider = 0;
        }

    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.WHITE);

        Vektor pos1 = new Vektor(posisiton);
        Vektor dist1 = new Vektor(velocity);
        dist1.norm();
        pos1.add(dist1);

        Vektor pos2 = new Vektor(posisiton);
        Vektor dist2 = new Vektor(-velocity.x, velocity.y);
        dist2.norm();
        dist2.mult(4);
        pos2.add(dist2);

        Vektor pos3 = new Vektor(posisiton);
        Vektor dist3 = new Vektor(velocity.x, -velocity.y);
        dist3.norm();
        dist3.mult(4);
        pos3.add(dist3);


        int[] xPos = {pos1.xAsInt(), pos2.xAsInt(), pos3.xAsInt()};
        int[] yPos = {pos1.yAsInt(), pos2.yAsInt(), pos3.yAsInt()};


        g2.drawPolygon(xPos, yPos, 3);

        //g2.fillOval(posisiton.xAsInt() - radius, posisiton.yAsInt() - radius, 2*radius, 2*radius);
    }


}
