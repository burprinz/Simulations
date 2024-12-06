package de.luiskun.apps.sims;

import de.luiskun.apps.Application;
import de.luiskun.apps.sims.classes.ColorPoint;
import de.luiskun.tools.Vektor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Rasterizer extends Application {

    private static final int FIELDSIZE = HEIGHT;

    int gridSize;
    int cellsize;
    Color[][] grid;

    //List<ColorPoint> points = new ArrayList<ColorPoint>();

    List<ColorPoint> points;
    List<ColorPoint>[] pointBuffer;

    public Rasterizer() {

        this.gridSize = 12;
        points = new ArrayList<>();
        resetGrid();
        setBasePoints();
        applyBresenham(this.points.get(0), this.points.get(1), this.points.get(2));

    }

    private void resetGrid() {

        points.clear();

        cellsize = FIELDSIZE / gridSize;

        this.grid = new Color[this.gridSize][this.gridSize];
        for (int x = 0; x < this.gridSize; x++) {
            for (int y = 0; y < this.gridSize; y++) {
                this.grid[x][y] = Color.BLACK;
            }
        }

    }

    private void setBasePoints() {
        points.add(new ColorPoint(new Color(255, 0, 0), new Vektor(8, 10)));
        points.add(new ColorPoint(new Color(0, 102, 225), new Vektor(1, 3)));
        points.add(new ColorPoint(new Color(0, 255, 0), new Vektor(9, 1)));
    }

    private void applyBresenham(ColorPoint c1, ColorPoint c2, ColorPoint c3) {

        pointBuffer = new ArrayList[3];
        pointBuffer[0] = new ArrayList<>();
        List<Vektor> list = Vektor.bresenham(c1.v, c2.v);
        for (int i = 0; i < list.size(); i++) {
            pointBuffer[0].add(new ColorPoint(new Color(0,0,0), new Vektor(list.get(i))));
        }
        pointBuffer[1] = new ArrayList<>();
        list = Vektor.bresenham(c1.v, c3.v);
        for (int i = 0; i < list.size(); i++) {
            pointBuffer[1].add(new ColorPoint(new Color(0,0,0), new Vektor(list.get(i))));
        }
        pointBuffer[2] = new ArrayList<>();
        list = Vektor.bresenham(c3.v, c2.v);
        for (int i = 0; i < list.size(); i++) {
            pointBuffer[2].add(new ColorPoint(new Color(0,0,0), new Vektor(list.get(i))));
        }
       /*
        double div = list.size()-1;

        for (int i = 0; i < list.size(); i++) {

            int r = (int) (((div-i)/div)*c1.c.getRed() + (i/div)*c2.c.getRed());
            int g = (int) (((div-i)/div)*c1.c.getGreen() + (i/div)*c2.c.getGreen());
            int b = (int) (((div-i)/div)*c1.c.getBlue() + (i/div)*c2.c.getBlue());

            System.out.println(r + " " + g + " " + b);

            pointBuffer.add(new ColorPoint(new Color(r, g, b), new Vektor(list.get(i))));
        }

        */
    }

    private void calculateColors() {

    }

    @Override
    public void update() {

    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        if (pointBuffer != null) {
            g2.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i < pointBuffer.length; i++) {
                for (int j = 0; j < pointBuffer[i].size(); j++) {
                    g2.fillRect(pointBuffer[i].get(j).v.xAsInt()*cellsize, pointBuffer[i].get(j).v.yAsInt()*cellsize, cellsize, cellsize);
                }
            }
        }

        for (int i = 0; i < points.size(); i++) {
            g2.setColor(points.get(i).c);
            g2.fillRect(points.get(i).v.xAsInt()*cellsize, points.get(i).v.yAsInt()*cellsize, cellsize, cellsize);
        }

        g2.setColor(Color.WHITE);
        for (int x = 0; x < this.gridSize; x++) {
            for (int y = 0; y < this.gridSize; y++) {
                g2.drawRect(x*cellsize, y*cellsize, cellsize, cellsize);
            }
        }

    }
}
