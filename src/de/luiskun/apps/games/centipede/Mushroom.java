package de.luiskun.apps.games.centipede;

import de.luiskun.tools.FileHandler;
import de.luiskun.tools.Vektor;
import de.luiskun.ui.uiparts.Updatable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Mushroom implements Updatable {

    public static final int RED = 0;
    public static final int YELLOW = 1;
    public static final int PURPLE = 2;
    int type;

    Vektor pos;

    BufferedImage[][] imgs;

    public int state = 0;

    public Mushroom(int type, Vektor pos) {
        this.type = type;
        this.pos = pos;
        imgs = new BufferedImage[3][3];
        imgs[0] = FileHandler.loadRow("res/games/centipede/greenmushroom.png", Centipede.cellS, 3, 48, 16);
        imgs[1] = FileHandler.loadRow("res/games/centipede/purplemushroom.png", Centipede.cellS, 3, 48, 16);
        imgs[2] = FileHandler.loadRow("res/games/centipede/yellowmushroom.png", Centipede.cellS, 3, 48, 16);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(imgs[type][state], pos.xAsInt()*Centipede.cellS, pos.yAsInt()*Centipede.cellS, null);
    }
}
