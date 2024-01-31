package de.luiskun;

public class Vektor {

    public double x, y;


    public Vektor() {

    }

    public Vektor(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void mult(double r) {
        this.x *= r;
        this.y *= r;
    }

    public int xAsInt() {
        return (int) x;
    }

    public int yAsInt() {
        return (int) y;
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

}
