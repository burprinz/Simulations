package de.luiskun.apps.games.centipede;

import de.luiskun.apps.games.Game;
import de.luiskun.tools.FileHandler;
import de.luiskun.tools.Vektor;
import de.luiskun.ui.uiparts.Updatable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Player {

    private static final int heightBorder = Game.HEIGHT/2;
    BufferedImage img;

    public Vektor pos;

    public Player() {
        img = FileHandler.SetupImage("res/games/centipede/player.png", Centipede.cellS, Centipede.cellS);
        pos = new Vektor();
    }

    public void update(Point mousePos) {
        if (mousePos != null) {
            pos = new Vektor(mousePos);
            pos.sub(new Vektor(Centipede.gridS/2, Centipede.gridS/2));
        }

        if (pos.yAsInt() < heightBorder) {
            pos.y = heightBorder;
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(img, pos.xAsInt(), pos.yAsInt(), null);
    }
}
