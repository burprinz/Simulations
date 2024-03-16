package de.luiskun;

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


}
