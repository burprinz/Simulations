package de.luiskun.apps.games.centipede;

import de.luiskun.apps.games.Game;
import de.luiskun.tools.Geometry;
import de.luiskun.tools.Vektor;
import de.luiskun.ui.uiparts.Updatable;

import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CentipedeGame {

    final int fireSpeed = 10;
    int fireSpeedPos = fireSpeed;

    Player p;
    List<Enemy> enemies;
    public boolean firing;

    List<Projectile> projectiles;
    Mushroom[][] mushroomGrid;

    public CentipedeGame() {
        p = new Player();
        enemies = new ArrayList<>();
        enemies.add(new Enemy(this));

        mushroomGrid = new Mushroom[Centipede.gridS][Centipede.gridS];

        for (int i = 0; i < 25; i++) {
            spawnRandomMushroom();
        }


        projectiles = new ArrayList<>();
    }

    public void update(Point mousePos) {
        p.update(mousePos);
        for (Enemy e : enemies) e.update();
        updateFire();
        updateCollision();
    }

    private void updateFire() {
        if (firing) {
            fireSpeedPos++;
            if (fireSpeedPos > fireSpeed) {
                fireSpeedPos = 0;
                Vektor startPos = new Vektor(p.pos);
                startPos.add(new Vektor(Centipede.cellS/2, 0));
                projectiles.add(new Projectile(startPos));
            }
        } else {
            fireSpeedPos = fireSpeed;
        }

        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = projectiles.get(i);
            p.update();
            if (p.pos.yAsInt() + 12 < 0) {
                projectiles.remove(i);
            }
        }
    }

    private void updateCollision() {
        for (int pr = 0; pr < projectiles.size(); pr++) {
            Projectile p = projectiles.get(pr);

            boolean shot = false;

            Vektor gridPos = new Vektor(p.pos);
            // Spitze vom Projektil
            gridPos.sub(new Vektor(0, p.size.yAsInt()/2));
            // Auf Raster bringen
            gridPos.divide(Centipede.cellS);

            if (mushroomGrid[gridPos.xAsInt()][gridPos.yAsInt()] != null) {
                projectiles.remove(pr);
                mushroomGrid[gridPos.xAsInt()][gridPos.yAsInt()].state++;
                if (mushroomGrid[gridPos.xAsInt()][gridPos.yAsInt()].state > 2) {
                    mushroomGrid[gridPos.xAsInt()][gridPos.yAsInt()] = null;
                }
                shot = true;
                continue;
            }

            if (!shot) {
                for (int i = 0; i < enemies.size(); i++) {
                    Enemy e = enemies.get(i);
                    for (int ii = 0; ii < e.body.size(); ii++) {
                        Vektor v = e.body.get(ii);
                        Vektor sGrid = new Vektor(v);
                        sGrid.divide(Centipede.cellS);
                        if (Vektor.isSameInt(sGrid, gridPos)) {
                            Enemy newE = enemies.get(i).split(ii);
                            mushroomGrid[gridPos.xAsInt()][gridPos.yAsInt()] = new Mushroom(Mushroom.RED, gridPos);
                            enemies.add(newE);
                            if (pr < projectiles.size()) {
                                projectiles.remove(pr);
                            }
                        }
                    }
                }
            }
        }
    }

    private void spawnRandomMushroom() {
        Random r = new Random();
        boolean placed = false;
        int x, y;
        while(!placed) {
            x = r.nextInt(Centipede.gridS);
            y = 1 + r.nextInt(Centipede.gridS-5);
            if (mushroomGrid[x][y] == null) {
                mushroomGrid[x][y] = new Mushroom(Mushroom.PURPLE, new Vektor(x, y));
                placed = true;
            }
        }
    }

    public void draw(Graphics2D g2) {
        p.draw(g2);
        for (Enemy e : enemies) e.draw(g2);


        for (int y = 0; y < Centipede.gridS; y++) {
            for (int x = 0; x < Centipede.gridS; x++) {
                if (mushroomGrid[x][y] != null) mushroomGrid[x][y].draw(g2);
            }
        }

        for (Projectile p : projectiles) {
            p.draw(g2);
        }


        g2.setColor(Color.WHITE);
        g2.drawLine(Game.HEIGHT, 0, Game.HEIGHT, Game.HEIGHT);
    }
}
