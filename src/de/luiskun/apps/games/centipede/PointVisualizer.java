package de.luiskun.apps.games.centipede;

import de.luiskun.tools.Vektor;
import de.luiskun.ui.uiparts.Updatable;

import java.awt.*;

public class PointVisualizer implements Updatable {

    private static final Vektor speed = new Vektor(0, -0.8);
    String s;
    Vektor pos;
    int anistate = 0;

    Color showColor;

    public PointVisualizer(Vektor pos, String text, Color c) {
        s = text;
        this.pos = pos;
        showColor = c;
    }

    @Override
    public void update() {
        pos.add(speed);
        anistate++;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(showColor);
        g2.drawString(s, pos.xAsInt(), pos.yAsInt());
    }
}
