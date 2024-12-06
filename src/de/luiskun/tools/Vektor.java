package de.luiskun.tools;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Vektor {

    public double x, y;


    public Vektor() {
        this.x = 0;
        this.y = 0;
    }

    public Vektor(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vektor(Vektor v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Vektor (Point p) {
        this.x = p.getX();
        this.y = p.getY();
    }

    public Vektor mult(double r) {
        this.x *= r;
        this.y *= r;
        return this;
    }

    public Vektor add(Vektor v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vektor sub(Vektor v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public Vektor divide(double d) {
        this.x = this.x / d;
        this.y = this.y / d;
        return this;
    }

    public Vektor set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vektor norm() {
        double dist = len();

        double cut = 1/dist;

        mult(cut);
        return this;
    }

    public Vektor limit(double d) {
        norm();
        mult(d);
        return this;
    }

    public int xAsInt() {
        return (int) x;
    }

    public int yAsInt() {
        return (int) y;
    }

    public double len() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public static Vektor toKates(Vektor v, double width, double height) {

        double x = v.x - (width/2);

        // Erst Koordinaten von unten nach oben setzen
        double y = height - v.y;
        y -= (height/2);

        return new Vektor(x, y);
    }

    public static Vektor toScreen(Vektor v, double width, double height) {

        double x = v.x + (width/2);

        double y = -v.y;
        y = y + (height/2);

        return new Vektor(x, y);
    }

    public static Vektor getPosOnCircle(double index, double total, double radius) {
        double angleStep = (Math.PI * 2) / total;
        double angle = angleStep * (index % total);
        Vektor result = createFromAngle(angle);
        result.mult(radius);
        return result;
    }

    public static Vektor createFromAngle(double angle) {
        return new Vektor(Math.cos(angle), Math.sin(angle));
    }

    public static double dist(Vektor v1, Vektor v2) {
        return Math.sqrt(Math.pow(v1.x - v2.x, 2) + Math.pow(v1.y - v2.y, 2));
    }

    public static Vektor random() {
        Random r = new Random();
        //return new Vektor(r.nextDouble()*rnd - offset, r.nextDouble()*rnd - offset);
        return createFromAngle(r.nextDouble()*(Math.PI*2));
    }

    public static double getDeltaDiff(double num, double maxDiff, double min, double max) {
        Random r = new Random();
        double diff = r.nextDouble() * maxDiff;
        int factor = r.nextBoolean()?1:-1;
        double newNum = num + diff*factor;
        double rest = Math.abs(Math.abs(newNum) - Math.abs(num));
        if (newNum < min) {
            num = max-rest;
        } else if (newNum > min) {
            num = min+rest;
        } else {
            num = newNum;
        }
        return num;
    }

    public static Vektor dir(Vektor start, Vektor finish) {
        Vektor v = new Vektor(finish.x - start.x, finish.y - start.y);
        v.norm();

        return v;
    }

    public String asString() {
        return "x:" + x + ",y:" + y;
    }

    public static double deteminat(Vektor v1, Vektor v2) {
        return v1.x * v2.y - v1.y * v2.x;
    }
    public static double skalar(Vektor v1, Vektor v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    public static double angle(Vektor v1, Vektor v2) {
        return Math.acos(Math.abs(skalar(v1, v2) / Math.abs(v1.len()*v2.len())));
    }

    public static double fullAngle(Vektor v1, Vektor v2) {
        return Math.atan2(v1.y-v2.y, v1.x-v2.x);
    }

    public static boolean isSameInt(Vektor v1, Vektor v2) {
        return v1.xAsInt() == v2.xAsInt() && v1.yAsInt() == v2.yAsInt();
    }

    public static int getXForCenteredText(Graphics2D g2, String text, int width) {
        return width / 2 - ((int) g2.getFontMetrics().getStringBounds(text, g2).getWidth()) / 2;
    }

    public static int getYForCenteredText(Graphics2D g2, int height) {
        return (height - g2.getFontMetrics(g2.getFont()).getHeight()) / 2 + g2.getFontMetrics(g2.getFont()).getAscent();
    }


    public static int getWidthForText(Graphics2D g2, String text) {
        return (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
    }
    public static int getXForRightAllignmentText(Graphics2D g2, String text, int x) {
        return (int) (x - g2.getFontMetrics().getStringBounds(text, g2).getWidth());
    }

    public static List<Vektor> bresenham(Vektor v1, Vektor v2) {
        List<Vektor> points = new ArrayList<>();

        v1 = new Vektor(v1);
        v2 = new Vektor(v2);

        int dx = Math.abs(v2.xAsInt() - v1.xAsInt());
        int dy = Math.abs(v2.yAsInt() - v1.yAsInt());
        int sx = v1.xAsInt() < v2.xAsInt() ? 1 : -1;
        int sy = v1.yAsInt() < v2.yAsInt() ? 1 : -1;
        int err = dx - dy;
        int e2;

        while (true) {
            System.out.println("(" + v1.xAsInt() + ", " + v1.yAsInt() + ")");
            points.add(new Vektor(v1));
            if (v1.xAsInt() == v2.xAsInt() && v1.yAsInt() == v2.yAsInt()) break;
            e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                v1.x += sx;
            }
            if (e2 < dx) {
                err += dx;
                v1.y += sy;
            }
        }

        return points;
    }


}
