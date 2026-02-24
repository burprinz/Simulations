package de.luiskun.apps.sims.classes.StreetViewer;

import de.luiskun.tools.Vektor;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import java.awt.*;
import java.awt.geom.Point2D;

public class MapViewer extends JXMapViewer {

    /**
     * Initialization
     */
    TileFactoryInfo info;
    DefaultTileFactory tileFactory;

    GeoPosition bielefeld = new GeoPosition(52.02111, 8.53472);
    GeoPosition badWildungen = new GeoPosition(51.117, 9.117);
    GeoPosition oerlinghausen = new GeoPosition(51.96667, 8.66667);
    GeoPosition home = new GeoPosition(51.93166925444306, 8.635414838790894);

    double currentAngle = 0;

    public MapViewer() {
        info = new OSMTileFactoryInfo("StreetViewer", "https://tile.openstreetmap.org");
        tileFactory = new DefaultTileFactory(info);
        this.setTileFactory(tileFactory);

        this.setAddressLocation(home);
        this.setZoom(0);
    }

    public void update() {
        Rectangle bounds = getViewportBounds();
        Point2D pixelCenter = new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
        System.out.println(getTileFactory().pixelToGeo(pixelCenter, getZoom()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Zentrum der Karte berechnen
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        // Die Rotation um den Mittelpunkt anwenden
        g2d.rotate(Math.toRadians(currentAngle), cx, cy);


        // Die ursprüngliche Zeichenlogik mit dem rotierten Grafik-Objekt aufrufen
        super.paintComponent(g2d);

        g2d.dispose();
    }

    public void rotate(int d) {
        currentAngle += d;
    }

    public void moveMap(Vektor d) {
        Point2D center = getCenter();

        double len = d.len();
        double angle = Math.toRadians(currentAngle-90);
        Vektor dir = new Vektor(Math.cos(angle), Math.sin(angle)).mult(len);

        System.out.println(dir.asString());

        setCenter(new Point2D.Double((center.getX() + dir.x), (center.getY() + dir.y)));
    }
}
