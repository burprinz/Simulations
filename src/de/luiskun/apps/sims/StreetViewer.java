package de.luiskun.apps.sims;

import de.luiskun.apps.Application;
import de.luiskun.apps.sims.classes.StreetViewer.MapViewer;
import de.luiskun.tools.Vektor;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.io.*;

public class StreetViewer extends Application {

    int mapSize = HEIGHT;

    JLayeredPane layeredPane;
    MapViewer mapViewer;
    JPanel drawingPanel;

    boolean wPressed, aPressed, sPressed, dPressed;

    double speed = 10.0;

    public StreetViewer() {
        super();

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (Mandelbrot.class) {
                    switch (ke.getID()) {
                        case KeyEvent.KEY_PRESSED:
                            if (ke.getKeyCode() == KeyEvent.VK_W) {
                                wPressed = true;
                            } else if(ke.getKeyCode() == KeyEvent.VK_S) {
                                sPressed = true;
                            } else if(ke.getKeyCode() == KeyEvent.VK_A) {
                                aPressed = true;
                            } else if(ke.getKeyCode() == KeyEvent.VK_D) {
                                dPressed = true;
                            }
                            break;

                        case KeyEvent.KEY_RELEASED:
                            if (ke.getKeyCode() == KeyEvent.VK_W) {
                                wPressed = false;
                            } else if(ke.getKeyCode() == KeyEvent.VK_S) {
                                sPressed = false;
                            } else if(ke.getKeyCode() == KeyEvent.VK_A) {
                                aPressed = false;
                            } else if(ke.getKeyCode() == KeyEvent.VK_D) {
                                dPressed = false;
                            }
                            break;
                    }
                    return false;
                }
            }
        });

        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        layeredPane.setPreferredSize(new Dimension(HEIGHT, HEIGHT));
        layeredPane.setBounds(0, 0, HEIGHT, HEIGHT);

        mapViewer = new MapViewer();
        mapViewer.setBounds(0, 0, HEIGHT, HEIGHT);
        layeredPane.add(mapViewer, JLayeredPane.DEFAULT_LAYER);

        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;

                g2.fillOval(mapSize/2-5, mapSize/2-5, 10, 10);
            }
        };
        drawingPanel.setOpaque(false);
        drawingPanel.setPreferredSize(new Dimension(HEIGHT, HEIGHT));
        drawingPanel.setBounds(0, 0, HEIGHT, HEIGHT);
        layeredPane.add(drawingPanel,  JLayeredPane.PALETTE_LAYER);

        this.add(layeredPane);
    }



    @Override
    public void update() {

        if (aPressed) {
            mapViewer.rotate(1);
        } else if (dPressed) {
            mapViewer.rotate(-1);
        }

        if (wPressed || aPressed || sPressed || dPressed) {
            Vektor moveOffset = new Vektor();
            if (wPressed) {
                moveOffset.y = -1;
            }
            moveOffset.norm().mult(speed);
            mapViewer.moveMap(moveOffset);
        }

        mapViewer.update();

    }


    @Override
    public void paintComponent(Graphics g) {

    }

}
