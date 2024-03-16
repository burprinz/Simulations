package de.luiskun.apps;

import de.luiskun.Vektor;
import de.luiskun.apps.Application;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.util.Random;

public class WeightedVoronoi extends Application {

    int fieldsize = HEIGHT;
    List<Vektor> points;
    Coordinate[] coordinates;

    public WeightedVoronoi() {
        super();

        Random r = new Random();

        points = new ArrayList<>();
        List<Coordinate> coords = new ArrayList<>();

        for(int i = 0; i < 100; i++) {
            Vektor v = new Vektor(r.nextInt(fieldsize), r.nextInt(fieldsize));
            points.add(v);
            coords.add(new Coordinate(v.x, v.y));
        }


        VoronoiDiagramBuilder vdb = new VoronoiDiagramBuilder();
        vdb.setSites(coords);

        Geometry geo = vdb.getDiagram(new GeometryFactory());
        coordinates = geo.getCoordinates();

        System.out.println(coordinates.length);
        for(Coordinate c : coordinates) {
            System.out.println(c.x + " " + c.y);
        }

    }

    @Override
    public void update() {

    }

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.WHITE);
        g2.fillRect(0,0,WIDTH,HEIGHT);

        g2.setColor(Color.BLACK);
        for (Vektor v : points) {
             g2.fillOval(v.xAsInt()-2, v.yAsInt()-2, 4, 4);
        }

        for (Coordinate c : coordinates) {
            g2.fillOval((int)c.x-1,  (int)c.y-1, 2, 2);
        }

    }
}
