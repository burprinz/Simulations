package de.luiskun.apps.games.centipede;

import de.luiskun.apps.games.Game;
import de.luiskun.tools.FileHandler;
import de.luiskun.tools.Geometry;
import de.luiskun.tools.Vektor;
import de.luiskun.ui.uiparts.Updatable;

import java.awt.*;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CentipedeGame {

    final int fireSpeed = 10;

    final int ultLimit = 15;

    int fireSpeedPos = fireSpeed;

    Player p;
    List<Enemy> enemies;
    public boolean firing;

    List<Projectile> projectiles;
    List<PointVisualizer> shownPoints;
    Mushroom[][] mushroomGrid;
    int score = 0;
    Centipede ce;
    int stage = 0;

    int ultbar = 0;

    public CentipedeGame(Centipede ce) {
        this.ce = ce;
        p = new Player();
        enemies = new ArrayList<>();
        shownPoints = new ArrayList<>();
        mushroomGrid = new Mushroom[Centipede.gridS][Centipede.gridS];
        projectiles = new ArrayList<>();

        resetGame();
    }

    public void update(Point mousePos) {

        p.update(mousePos);

        updateEnemy();
        updateFire();
        updateCollision();
        updateShowPoints();
        checkIfLevelOver();
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
                    gridPos.mult(Centipede.cellS);
                    shownPoints.add(new PointVisualizer(gridPos, "+5P", Color.RED));
                    score += 5;
                    ce.playScoreSound();
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
                            shownPoints.add(new PointVisualizer(v, "+2P", Color.BLUE));
                            score += 2;
                            mushroomGrid[gridPos.xAsInt()][gridPos.yAsInt()] = new Mushroom(Mushroom.RED, gridPos);
                            enemies.add(newE);
                            ce.playScoreSound();
                            if (pr < projectiles.size()) {
                                projectiles.remove(pr);
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateShowPoints() {
        for (int i = 0; i < shownPoints.size(); i++) {
            shownPoints.get(i).update();
            if (shownPoints.get(i).anistate > 20) shownPoints.remove(i);
        }
    }

    private void updateEnemy() {
        for (Enemy e : enemies) e.update();

        for (int i = 0; i < enemies.size(); i++) {
            if (enemies.get(i).body.size() == 0) {
                enemies.remove(i);
            }
        }
    }

    private void checkIfLevelOver() {
        if (enemies.size() == 0) {
            stage++;
            resetGame();
        }
    }

    public void resetGame() {
        final int speedChange = 7;
        final float speed = stage<speedChange?2.5f:5f;
        final int curStage = stage<speedChange?stage:stage-speedChange;
        final int amEnemies = 12 + curStage*4;
        final int color = stage%3;
        int amMushrooms = 25 + curStage*4;

        if (amMushrooms > 50) amMushrooms = 50;

        enemies.clear();
        projectiles.clear();
        shownPoints.clear();
        for (int y = 0; y < Centipede.gridS; y++) {
            for (int x = 0; x < Centipede.gridS; x++) {
                mushroomGrid[x][y] = null;
            }
        }

        enemies.add(new Enemy(this, amEnemies, speed));
        for (int i = 0; i < amMushrooms; i++) {
            spawnRandomMushroom(color);
        }
    }

    public void gameOver() {
        ce.gameOver(score);
    }

    private void spawnRandomMushroom(int color) {
        Random r = new Random();
        boolean placed = false;
        int x, y;
        while(!placed) {
            x = r.nextInt(Centipede.gridS);
            y = 1 + r.nextInt(Centipede.gridS-5);
            if (mushroomGrid[x][y] == null) {
                mushroomGrid[x][y] = new Mushroom(color, new Vektor(x, y));
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

        for (PointVisualizer pv : shownPoints) {
            pv.draw(g2);
        }

        int x = Game.HEIGHT + 10;
        int y = 30;

        g2.setColor(Color.WHITE);
        g2.drawString("Points: " + score, x, y);

        y += 40;
        g2.drawString("Stage " + (stage+1), x, y);
    }

    public void useUlt() {
        if (ultbar > ultLimit) {
            for (int i = 0; i < Centipede.gridS; i++) {
                for (int am = 0; am < 5; am++) {
                    projectiles.add(new Projectile(new Vektor(i*Centipede.cellS + Centipede.cellS/2, Game.HEIGHT)));
                }
            }
            ultbar -= ultLimit;
        }
    }
}
