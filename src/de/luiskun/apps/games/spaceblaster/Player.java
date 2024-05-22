package de.luiskun.apps.games.spaceblaster;

import de.luiskun.tools.FileHandler;
import de.luiskun.tools.Vektor;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Player {

    BufferedImage img;
    Vektor pos; 
    int radius = 25;
    double speed = 2;
    double angle = 0;

    public Player(Vektor pos) {
        this.pos = pos;
        this.img = FileHandler.SetupImage("res/pixelart/spaceBlaster/roundShip.png", 2*radius, 2*radius);
    }

    public void rotateLeft() {
       angle -= 0.05;
    }

    public void rotateRight() {
        angle += 0.05;
    }

    public void update() {
        Vektor vel = Vektor.createFromAngle(angle - + (Math.PI/2));
        vel.mult(speed);
        pos.add(vel);
    }

    public void draw (Graphics2D g2) {
        AffineTransform tx = AffineTransform.getRotateInstance(angle, radius, radius);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        g2.drawImage(op.filter(img, null), pos.xAsInt()-radius, pos.yAsInt()-radius, null);}

}
