package de.luiskun.apps.games.centipede;

import de.luiskun.tools.Vektor;
import de.luiskun.ui.uiparts.Updatable;

import java.awt.*;

public class Projectile implements Updatable {

    Vektor velocity;
    public Vektor pos;
    public Vektor size = new Vektor(4, 12);

    public Projectile(Vektor pos) {
        this.pos = pos;
        velocity = new Vektor(0, -10);
    }

    @Override
    public void update() {
        pos.add(velocity);

    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.fillRect(pos.xAsInt()-size.xAsInt()/2, pos.yAsInt()-size.yAsInt()/2, size.xAsInt(), size.yAsInt());
    }
}
