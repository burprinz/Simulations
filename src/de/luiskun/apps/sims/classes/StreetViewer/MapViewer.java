package de.luiskun.apps.sims.classes.StreetViewer;

import de.luiskun.tools.Vektor;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class MapViewer extends JXMapViewer {

    private final TileFactoryInfo info;
    private final DefaultTileFactory tileFactory;

    private final GeoPosition home =
            new GeoPosition(51.93166925444306, 8.635414838790894);

    private double currentAngle = 0.0;

    public MapViewer() {
        info = new OSMTileFactoryInfo(
                "StreetViewer",
                "https://tile.openstreetmap.org"
        );

        tileFactory = new DefaultTileFactory(info);
        tileFactory.setThreadPoolSize(8);

        setTileFactory(tileFactory);
        setAddressLocation(home);

        // Näher an die Straßen heranzoomen.
        setZoom(3);
    }

    public void update() {
        // Keine Konsolenausgabe pro Frame, damit die Fahrt flüssig bleibt.
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        double cx = getWidth() / 2.0;
        double cy = getHeight() / 2.0;

        g2.rotate(Math.toRadians(currentAngle), cx, cy);
        super.paintComponent(g2);

        g2.dispose();
    }

    public void rotate(double degrees) {
        currentAngle = normalizeAngle(currentAngle + degrees);
        repaint();
    }

    public void moveMap(Vektor d) {
        Point2D center = getCenter();

        double angle = Math.toRadians(currentAngle);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        // Bewegungsvektor passend zur Kartenrotation umrechnen.
        double dx = d.x * cos + d.y * sin;
        double dy = -d.x * sin + d.y * cos;

        setCenter(new Point2D.Double(
                center.getX() + dx,
                center.getY() + dy
        ));

        repaint();
    }

    public double getCurrentAngle() {
        return currentAngle;
    }

    private double normalizeAngle(double angle) {
        angle %= 360.0;

        if (angle < 0.0) {
            angle += 360.0;
        }

        return angle;
    }
}