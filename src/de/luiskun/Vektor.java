package de.luiskun;

import java.util.Random;

public class Vektor {

    public double x, y;


    public Vektor() {

    }

    public Vektor(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vektor(Vektor v) {
        this.x = v.xAsInt();
        this.y = v.yAsInt();
    }

    public void mult(double r) {
        this.x *= r;
        this.y *= r;
    }

    public void add(Vektor v) {
        this.x += v.x;
        this.y += v.y;
    }

    public void divide(double d) {
        this.x = this.x / d;
        this.y = this.y / d;
    }

    public void norm() {
        double dist = len();

        double cut = 1/dist;

        mult(cut);
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
        Vektor v = new Vektor(finish.x - start.x, finish.x - start.y);
        v.norm();

        return v;
    }

}
