package de.luiskun;

import java.awt.*;
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
        this.x = v.xAsInt();
        this.y = v.yAsInt();
    }

    public Vektor (Point p) {
        this.x = p.getX();
        this.y = p.getY();
    }

    public void mult(double r) {
        this.x *= r;
        this.y *= r;
    }

    public void add(Vektor v) {
        this.x += v.x;
        this.y += v.y;
    }

    public void sub(Vektor v) {
        this.x -= v.x;
        this.y -= v.y;
    }

    public void divide(double d) {
        this.x = this.x / d;
        this.y = this.y / d;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void norm() {
        double dist = len();

        double cut = 1/dist;

        mult(cut);
    }

    public void limit(double d) {
        norm();
        mult(d);
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
        y += (height/2);

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

    public static Vektor random(double rnd, double offset) {
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
        return Math.atan2(skalar(v1, v2), deteminat(v1, v2)) + Math.PI;
    }

}
