package de.luiskun.tools;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Geometry {

    public double x, y, width, height;

    public static boolean rectCircleCol(double rx, double ry, double rw, double rh, double cx, double cy, double r) {

        r-=3;

        double testX = cx;
        double testY = cy;

        if (cx < rx)         testX = rx;        // left edge
        else if (cx > rx+rw) testX = rx+rw;     // right edge

        if (cy < ry)         testY = ry;        // top edge
        else if (cy > ry+rh) testY = ry+rh;     // bottom edge

        double distX = cx-testX;
        double distY = cy-testY;
        double distance = Math.sqrt( (distX*distX) + (distY*distY) );

        return distance <= r;
    }

    public static int getXForCenteredText(Graphics2D g2, String text, int width) {
        return width / 2 - ((int) g2.getFontMetrics().getStringBounds(text, g2).getWidth()) / 2;
    }

    public static int getXforAllignToRightText(Graphics2D g2, String text, int tailX) {
        return tailX - ((int) g2.getFontMetrics().getStringBounds(text, g2).getWidth());
    }

    public static BufferedImage rotate(BufferedImage img, double angle) {
        // Getting Dimensions of image
        int width = img.getWidth();
        int height = img.getHeight();

        // Creating a new buffered image
        BufferedImage newImage = new BufferedImage(
                img.getWidth(), img.getHeight(), img.getType());

        // creating Graphics in buffered image
        Graphics2D g2 = newImage.createGraphics();

        // Rotating image by degrees using toradians()
        // method
        // and setting new dimension t it
        g2.rotate(Math.toRadians(angle), width / 2,
                height / 2);
        g2.drawImage(img, null, 0, 0);

        // Return rotated buffer image
        return newImage;
    }

    public static boolean rectangleCollision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
        return x2 + w2 > x1 &&
                        y2 + h2 > y1 &&
                        x1 + w1 > x2 &&
                        y1 + h1 > y2;
    }

}
