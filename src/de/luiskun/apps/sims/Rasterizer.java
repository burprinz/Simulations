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

        this.gridSize = 50;
        points = new ArrayList<>();
        resetGrid();
        setBasePoints();
        applyBresenham(this.points.get(0), this.points.get(1), this.points.get(2));
        calculateColors();
        addFromBufferIfNew();
        int h = sortHeight();
        getSideVectors(h);
        calculateColorGaps();
        addFromBufferIfNew();
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
        points.add(new ColorPoint(new Color(255, 0, 0), new Vektor(gridSize/2, 1)));
        points.add(new ColorPoint(new Color(0, 102, 225), new Vektor(2, gridSize-2)));
        points.add(new ColorPoint(new Color(0, 255, 0), new Vektor(gridSize-2, gridSize-2)));
    }

    private void applyBresenham(ColorPoint c1, ColorPoint c2, ColorPoint c3) {

        pointBuffer = new ArrayList[3];
        pointBuffer[0] = new ArrayList<>();
        List<Vektor> list = Vektor.bresenham(c1.v, c2.v);
        for (int i = 0; i < list.size(); i++) {
            pointBuffer[0].add(new ColorPoint(null, new Vektor(list.get(i))));
        }
        pointBuffer[0].get(0).c = c1.c;
        pointBuffer[0].get(pointBuffer[0].size()-1).c = c2.c;
        pointBuffer[1] = new ArrayList<>();
        list = Vektor.bresenham(c1.v, c3.v);
        for (int i = 0; i < list.size(); i++) {
            pointBuffer[1].add(new ColorPoint(null, new Vektor(list.get(i))));
        }
        pointBuffer[1].get(0).c = c1.c;
        pointBuffer[1].get(pointBuffer[1].size()-1).c = c3.c;
        pointBuffer[2] = new ArrayList<>();
        list = Vektor.bresenham(c3.v, c2.v);
        for (int i = 0; i < list.size(); i++) {
            pointBuffer[2].add(new ColorPoint(null, new Vektor(list.get(i))));
        }
        pointBuffer[2].get(0).c = c3.c;
        pointBuffer[2].get(pointBuffer[2].size()-1).c = c2.c;
    }

    private void calculateColors() {
        for (int i = 0; i < pointBuffer.length; i++) {
            Color c1 = pointBuffer[i].get(0).c;
            Color c2 = pointBuffer[i].get(pointBuffer[i].size()-1).c;
            double div = pointBuffer[i].size()-1;
            for (int j = 0; j < pointBuffer[i].size(); j++) {
                int r = (int) (((div-j)/div)*c1.getRed() + (j/div)*c2.getRed());
                int g = (int) (((div-j)/div)*c1.getGreen() + (j/div)*c2.getGreen());
                int b = (int) (((div-j)/div)*c1.getBlue() + (j/div)*c2.getBlue());
                pointBuffer[i].get(j).c = new Color(r, g, b);
            }
        }
    }

    private void addFromBufferIfNew() {
        int check = points.size();
        for (int i = 0; i < pointBuffer.length; i++) {
            for (int j = 0; j < pointBuffer[i].size(); j++) {
                ColorPoint cp = pointBuffer[i].get(j);
                boolean b = false;
                for (int k = 0; k < check; k++) {
                    if (Vektor.isSameInt(points.get(k).v, cp.v)) {
                        b = true;
                        break;
                    }
                }
                if (!b)
                    points.add(new ColorPoint(cp));
            }
        }
        pointBuffer = null;
    }

    private void getSideVectors(int height) {
        pointBuffer = new ArrayList[height];
        int curH = points.get(0).v.yAsInt();
        pointBuffer[0] = new ArrayList<>();
        int c = 0;
        for (int i = 0; i < points.size(); i++) {
            if (curH != points.get(i).v.yAsInt()) {
                curH = points.get(i).v.yAsInt();
                c++;
                pointBuffer[c] = new ArrayList<>();
                pointBuffer[c].add(new ColorPoint(points.get(i)));

            } else {
                pointBuffer[c].add(new ColorPoint(points.get(i)));
            }
        }

        for (int i = 0; i < pointBuffer.length; i++) {
            if (pointBuffer[i].size() == 1) {
                pointBuffer[i].add(new ColorPoint(pointBuffer[i].get(0)));
            } else if (pointBuffer[i].size() > 2) {
                int maxX = 0, minX = 0;
                for (int j = 0; j < pointBuffer[i].size(); j++) {
                    if (pointBuffer[i].get(maxX).v.xAsInt() < pointBuffer[i].get(j).v.yAsInt()) {
                        maxX = j;
                    }
                    if (pointBuffer[i].get(minX).v.xAsInt() > pointBuffer[i].get(j).v.yAsInt()) {
                        minX = j;
                    }
                }
                ColorPoint max = new ColorPoint(pointBuffer[i].get(maxX));
                ColorPoint min = new ColorPoint(pointBuffer[i].get(minX));
                pointBuffer[i].clear();
                pointBuffer[i].add(min);
                pointBuffer[i].add(max);
            } else {
                // = 2;
                if (pointBuffer[i].get(0).v.xAsInt() > pointBuffer[i].get(1).v.xAsInt()) {
                    ColorPoint tmp = new ColorPoint(pointBuffer[i].get(0));
                    ColorPoint tmp2 = new ColorPoint(pointBuffer[i].get(1));
                    pointBuffer[i].clear();
                    pointBuffer[i].add(tmp2);
                    pointBuffer[i].add(tmp);
                }
            }
        }
    }

    private void calculateColorGaps() {
        for (int i = 0; i < pointBuffer.length; i++) {
            ColorPoint left = pointBuffer[i].get(0);
            ColorPoint right = pointBuffer[i].get(1);
            Color c1 = left.c;
            Color c2 = right.c;
            double div = right.v.xAsInt() - left.v.xAsInt();
            for (int j = 0; j < div+1; j++) {
                int r = (int) (((div-j)/div)*c1.getRed() + (j/div)*c2.getRed());
                int g = (int) (((div-j)/div)*c1.getGreen() + (j/div)*c2.getGreen());
                int b = (int) (((div-j)/div)*c1.getBlue() + (j/div)*c2.getBlue());
                pointBuffer[i].add(new ColorPoint(new Color(r,g,b), new Vektor(left.v.x + j, left.v.y)));
            }
        }
    }

    private int sortHeight() {
        boolean found = true;

        int am = points.size();

        for (int i = 0; i < am && found; i++) {
            found = false;

            for (int y = 0; y < am-1; y++) {
                if(points.get(y).v.y > points.get(y+1).v.y) {
                    swap(y, y+1);
                    found = true;
                }
            }
        }

        int c = 0;
        int cy = -1;
        for (int i = 0; i < am; i++) {
            if (points.get(i).v.yAsInt() != cy) {
                cy = points.get(i).v.yAsInt();
                c++;
            }
        }
        return c;
    }

    private void swap(int i, int j) {
        ColorPoint tmp = new ColorPoint(points.get(i));
        points.set(i, new ColorPoint(points.get(j)));
        points.set(j, tmp);
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
            for (int i = 0; i < pointBuffer.length; i++) {
                if (pointBuffer[i] != null) {
                    for (int j = 0; j < pointBuffer[i].size(); j++) {
                        g2.setColor(pointBuffer[i].get(j).c==null?Color.LIGHT_GRAY:pointBuffer[i].get(j).c);
                        g2.fillRect(pointBuffer[i].get(j).v.xAsInt()*cellsize, pointBuffer[i].get(j).v.yAsInt()*cellsize, cellsize, cellsize);
                    }
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
