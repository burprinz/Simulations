package de.luiskun.apps.sims.classes;

import de.luiskun.tools.Vektor;
import de.luiskun.apps.sims.Boids;
import de.luiskun.ui.Screen;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.Random;
import java.util.List;

public class Boid {

    int radius = 8;
    public Vektor posisiton, velocity, acceleration;
    double maxForce = 0.15;
    double maxSpeed = 2;

    double allignSlider = 0, cohesionSlider = 0, separationSlider = 0;

    Boids b;

    public Boid(Boids b, int x, int y) {

        this.b = b;

        Random r = new Random();

        this.posisiton = new Vektor(x, y);
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
        if (posisiton.x - radius> Screen.WIDTH-Boids.settingsW) {
            posisiton.x = 0 - radius;
        } else if (posisiton.x + radius < 0) {
            posisiton.x = Screen.WIDTH-Boids.settingsW + radius;
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

        allignment.mult(b.allImpact);
        cohesion.mult(b.cohImpact);
        separation.mult(b.sepImpact);

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
        g2.setColor(!b.coloredBoids?Color.WHITE:Color.getHSBColor((float) (Vektor.fullAngle(new Vektor(1, 0), velocity) / (2 * Math.PI)), 1, 1));
        if (b.triangleBoid) {
            drawTriangle(g2);
        } else {
            g2.fillOval(posisiton.xAsInt() - radius, posisiton.yAsInt() - radius, 2*radius, 2*radius);
        }
    }

    private void drawTriangle(Graphics2D g2) {
        final int sideLength = 10;

        double startingAngle = Vektor.fullAngle(new Vektor(1, 0), velocity);

        // Erstelle ein gleichseitiges Dreieck
        Path2D.Double triangle = new Path2D.Double();
        triangle.moveTo(
                posisiton.x + sideLength * Math.cos(startingAngle),
                posisiton.y + sideLength * Math.sin(startingAngle)
        );

        // Füge die anderen zwei Punkte hinzu
        for (int i = 1; i < 3; i++) {
            triangle.lineTo(
                    posisiton.x + sideLength * Math.cos(startingAngle + i * 2 * Math.PI / 3),
                    posisiton.y + sideLength * Math.sin(startingAngle + i * 2 * Math.PI / 3)
            );
        }

        triangle.closePath();
        g2.fill(triangle);
    }

    private void drawTriangle_old(Graphics2D g2) {
        Vektor pos1 = new Vektor(posisiton);
        Vektor dist1 = new Vektor(velocity);
        dist1.norm();
        dist1.mult(20);
        pos1.add(dist1);

        Vektor pos2 = new Vektor(posisiton);
        Vektor dist2 = new Vektor(-velocity.x, velocity.y);
        dist2.norm();
        dist2.mult(10);
        pos2.add(dist2);

        Vektor pos3 = new Vektor(posisiton);
        Vektor dist3 = new Vektor(velocity.x, -velocity.y);
        dist3.norm();
        dist3.mult(10);
        pos3.add(dist3);

        int[] xPos = {pos1.xAsInt(), pos2.xAsInt(), pos3.xAsInt()};
        int[] yPos = {pos1.yAsInt(), pos2.yAsInt(), pos3.yAsInt()};

        g2.fillPolygon(xPos, yPos, 3);
    }
}
