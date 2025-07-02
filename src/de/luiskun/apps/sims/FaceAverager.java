package de.luiskun.apps.sims;

import de.luiskun.apps.Application;
import de.luiskun.tools.FileHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;

public class FaceAverager extends Application {

    File[] images;

    BufferedImage currentImage;
    double[][][] grid;


    public FaceAverager() {
        super();

        images = FileHandler.getFaceFiles();
        grid = new double[images.length][180][200];

        for (int i = 0; i < images.length; i++) {
            BufferedImage img = FileHandler.SetupImage(images[i].getPath(), 180, 200);
            for (int x = 0; x < img.getWidth(); x++) {
                for (int y = 0; y < img.getHeight(); y++) {
                    grid[i][x][y] += img.getRGB(x, y);
                }
            }
        }
        BufferedImage next = new BufferedImage(180, 200, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                next.setRGB(x, y, (int)(grid[images.length-1][x][y] / images.length));
            }
        }
        currentImage = next;
    }

    @Override
    public void update() {



    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);


        if (currentImage != null) {
            g2.drawImage(currentImage, 0, 0, null);
        }

    }
}
