package de.luiskun.apps.sims;

import de.luiskun.apps.Application;
import de.luiskun.tools.FileHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SobelEdgeDetection extends Application {

    private static int[][] sobelX = {{-1,0,1},{-2,0,2},{-1,0,1}};
    private static int[][] sobelY = {{-1,-2,-1},{0,0,0},{1,2,1}};

    BufferedImage image;
    BufferedImage sobel;

    public SobelEdgeDetection() {
        this.image = FileHandler.SetupImage("res/sims/sobeledge/img.png",  300, 200);
        this.sobel = applyFilter(image);
    }

    public static BufferedImage applyFilter(BufferedImage img) {

        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        int[][] grayMatrix = getGrayMatrix(img);

        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {

                int matX = x+1;
                int matY = y+1;

                int gx = 0;
                int gy = 0;

                for (int xx = -1; xx <= 1; xx++) {
                    for (int yy = -1; yy <= 1; yy++) {
                        gx += grayMatrix[matX+xx][matY+yy] * sobelX[xx+1][yy+1];
                        gy += grayMatrix[matX+xx][matY+yy] * sobelY[xx+1][yy+1];
                    }
                }

                int g = (int) Math.sqrt(gx*gx + gy*gy);

                g= Math.min(g, 255);
                g= Math.max(g, 0);

                result.setRGB(x,y,grayToRGB(g).getRGB());
            }
        }

        return result;
    }

    public static int[][] getGrayMatrix(BufferedImage img) {
        int[][] result = new int[img.getWidth()+2][img.getHeight()+2];
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                Color color = new Color(img.getRGB(x,y));
                result[x+1][y+1] = (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());
            }
        }
        return result;
    }

    public static Color grayToRGB(int gray) {
        return new Color(gray, gray, gray);
    }

    @Override
    public void update() {

    }

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2 =  (Graphics2D) g;
        g2.setColor(Color.BLACK);

        g2.fillRect(0,0, WIDTH, HEIGHT);

        g2.drawImage(image, 10, 10,null);

        g2.drawImage(sobel, 320, 10, null);

    }
}
