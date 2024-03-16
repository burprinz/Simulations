package de.luiskun.games.spaceblaster;

import de.luiskun.Geometry;
import de.luiskun.Vektor;
import de.luiskun.apps.Application;
import de.luiskun.games.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpaceBlaster extends Game {

    Background background;
    Player p;

    private int aGW = 9, aGH = 7;

    List<Comet> comets;
    List<Projectile> bullets;

    List<Comet>[][] cometGrid;

    int timePassed = 0;

    int bigComets;

    int shotTimer = 0;
    int shotSpeed = 20;

    public SpaceBlaster() {
        super();

        /*

        cometGrid = new ArrayList[aGW][aGH];
        for (int y = 0; y < cometGrid[0].length; y++) {
            for (int x = 0; x < cometGrid.length; x++) {
                cometGrid[x][y] = new ArrayList<>();
            }
        }



        cometGrid[0][0].add(new Comet(50, 10, new Vektor(100, 100)));
    */
        comets = new ArrayList<>();
        bullets = new ArrayList<>();

        background = new Background();

        p = new Player(new Vektor(WIDTH/2, HEIGHT/2));
    }

    @Override
    public void update() {
        background.update();

        p.update();

        if (leftPressed) {
            p.rotateLeft();
        }

        if (rightPressed) {
            p.rotateRight();
        }

        for(int i = 0; i < comets.size(); i++) {
            Comet c = comets.get(i);

            c.update();


            if (Vektor.dist(p.pos, c.pos) < p.radius + c.radius) {
                System.exit(0);
            }

            if (c.pos.x + c.radius < 0 || c.pos.x - c.radius > Application.WIDTH || c.pos.y + c.radius < 0 || c.pos.y - c.radius > Application.HEIGHT) {
                comets.remove(i);
                i--;
            }
        }

        List<Comet> toAdd = new ArrayList<>();

        for (int ii = 0; ii < bullets.size(); ii++) {
            Projectile pr = bullets.get(ii);
            for (int i = 0; i < comets.size(); i++) {
                Comet c = comets.get(i);
                if (Vektor.dist(pr.pos, c.pos) < pr.radius + c.radius) {
                    if (!c.split) {
                        Random r = new Random();
                        double angleOne = r.nextDouble()*Math.PI*2;
                        double angleTwo = r.nextDouble()*Math.PI*2;
                        double angleThree = r.nextDouble()*Math.PI*2;

                        Comet c1 = spawnComet(true, c, angleOne);
                        Comet c2 = spawnComet(true, c, angleTwo);
                        Comet c3 = spawnComet(true, c, angleThree);

                        toAdd.add(c1);
                        toAdd.add(c2);
                        toAdd.add(c3);

                    }
                    bullets.remove(ii);
                    ii--;
                    comets.remove(i);
                    i--;
                }
            }
        }

        for(Comet c : toAdd) {
            comets.add(c);
        }

        timePassed++;

        int amountComets = (timePassed / 1200) + 5;

        while (comets.size() < amountComets) {
            Comet c = spawnComet(false, null, 0);
            comets.add(c);
        }

        if (spacePressed) {
            if (shotTimer == 0) {
                Vektor mid = new Vektor(p.pos);
                Vektor dir = Vektor.createFromAngle(p.angle - Math.PI/2);
                dir.mult(p.radius);
                mid.add(dir);
                bullets.add(new Projectile(5, mid, p.angle - Math.PI/2));
            }
            shotTimer++;
            if(shotTimer > shotSpeed) {
                shotTimer = 0;
            }
        } else {
            shotTimer = 0;
        }

        for (Projectile p : bullets) {
            p.update();
        }

    }

    private Comet spawnComet(boolean b, Comet c, double ang) {

        Random r = new Random();

        int radius;

        Vektor start = null;
        Vektor vel = null;

        if (!b) {
            radius = 35 + r.nextInt(15);

            double x = 0, y = 0;
            int pos = r.nextInt(4);
            switch (pos) {
                case 0:
                    // Oben
                    y = -radius;
                    x = r.nextInt(WIDTH);
                    break;
                case 1:
                    // Unten
                    y = HEIGHT + radius;
                    x = r.nextInt(WIDTH);
                    break;
                case 2:
                    // Links
                    y = r.nextInt(HEIGHT);
                    x = -radius;
                    break;
                case 3:
                    // Rechts
                    y = r.nextInt(HEIGHT);
                    x = WIDTH + radius;
                    break;
            }

            start = new Vektor(x, y);

            Vektor fixPoint = new Vektor(150 + r.nextInt(600), 100 + r.nextInt(250));
            vel = Vektor.dir(start, fixPoint);

        } else {
            radius = 15 + r.nextInt(15);
            start = new Vektor(c.pos);
            vel = Vektor.createFromAngle(ang);
            vel.mult(2);
        }

        return new Comet(radius, 8 , start, vel, b);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        background.draw(g2);
        p.draw(g2);

        for (Comet c : comets) {
            c.draw(g2);
        }

        for (Projectile p : bullets) {
            p.draw(g2);
        }

        /*
        for (int y = 0; y < cometGrid[0].length; y++) {
            for (int x = 0; x < cometGrid.length; x++) {
                for (Comet c : cometGrid[x][y]) {
                    c.draw(g2);
                }
            }
        }

         */
    }

}
