package de.luiskun.apps.games.centipede;

import de.luiskun.tools.FileHandler;
import de.luiskun.tools.Vektor;
import de.luiskun.ui.uiparts.Updatable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Mushroom implements Updatable {

    public static final int RED = 0;
    public static final int PURPLE = 1;
    int type;

    Vektor pos;

    BufferedImage[][] imgs;

    public int state = 0;

    public Mushroom(int type) {
        this.type = type;
        pos = new Vektor();
        imgs = new BufferedImage[2][3];
        imgs[0] = FileHandler.loadRow("res/games/centipede/greenmushroom.png", Centipede.cellS, 3, 48, 16);
        imgs[1] = FileHandler.loadRow("res/games/centipede/purplemushroom.png", Centipede.cellS, 3, 48, 16);

    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(imgs[type][state], pos.xAsInt(), pos.yAsInt(), null);
    }
}
