package de.luiskun.apps.games.spaceblaster;

import de.luiskun.tools.Vektor;
import de.luiskun.apps.sims.Application;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Background {

    int amountTiles = 100;

    List<BackgroundElement> elems;

    public Background() {
        elems = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < amountTiles; i++) {
            elems.add(new BackgroundElement(r.nextInt(3), r.nextInt(255), r.nextDouble()* (365), new Vektor(r.nextDouble()* Application.WIDTH, r.nextDouble()*Application.HEIGHT)));
        }
    }

    public void update() {
        for (BackgroundElement elem : elems) {
            elem.update();
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, Application.WIDTH, Application.HEIGHT);
        for (BackgroundElement elem : elems) {
            elem.draw(g2);
        }
    }

}
