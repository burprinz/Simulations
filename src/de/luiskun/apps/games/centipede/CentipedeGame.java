package de.luiskun.apps.games.centipede;

import de.luiskun.tools.Geometry;
import de.luiskun.tools.Vektor;
import de.luiskun.ui.uiparts.Updatable;

import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class CentipedeGame {

    final int fireSpeed = 10;
    int fireSpeedPos = fireSpeed;

    Player p;
    Mushroom m;
    public boolean firing;

    List<Projectile> projectiles;
    List<Mushroom> mushrooms;

    public CentipedeGame() {
        p = new Player();

        projectiles = new ArrayList<>();
        mushrooms = new ArrayList<>();
        mushrooms.add(new Mushroom(Mushroom.RED));
    }

    public void update(Point mousePos) {
        p.update(mousePos);
        updateFire();
        updateCollision();
    }

    private void updateFire() {
        if (firing) {
            fireSpeedPos++;
            if (fireSpeedPos > fireSpeed) {
                fireSpeedPos = 0;
                projectiles.add(new Projectile(p.pos));
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
            for (int mu = 0; mu < mushrooms.size(); mu++) {
                Mushroom m = mushrooms.get(mu);
                if (Geometry.rectangleCollision(p.pos.xAsInt(), p.pos.yAsInt(), p.size.xAsInt(), p.size.yAsInt(),
                        m.pos.xAsInt(), m.pos.yAsInt(), Centipede.cellS, Centipede.cellS)) {
                    projectiles.remove(pr);
                    m.state++;
                    if (m.state > 2) {
                        mushrooms.remove(mu);
                    }
                    break;
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        p.draw(g2);

        for (Mushroom m : mushrooms) {
            m.draw(g2);
        }

        for (Projectile p : projectiles) {
            p.draw(g2);
        }
    }
}
