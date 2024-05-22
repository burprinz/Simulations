package de.luiskun.apps.sims;

import de.luiskun.apps.Application;
import de.luiskun.tools.FileHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpriteTest extends Application {

    BufferedImage[][] imgs;

    public SpriteTest() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDoubleBuffered(true);
        this.setLayout(null);

        imgs = FileHandler.load2DspriteSheet("res/NinjaAdventure/Actor/Characters/BlueNinja/SpriteSheet.png", 4, 7, 40, 40);
    }

    @Override
    public void update() {

    }

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.WHITE);
        g2.fillRect(0,0,WIDTH, HEIGHT);


        if(imgs != null) {

            for(int y = 0; y < imgs[0].length; y++) {
                for(int x = 0; x < imgs.length; x++) {
                    g2.drawImage(imgs[x][y], x * 50, y*50, null);
                }
            }

        }
    }
}
