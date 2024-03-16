package de.luiskun;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.luiskun.json.Farbe;
import de.luiskun.json.Farbpalette;

import java.util.List;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileHandler {

    public static BufferedImage SetupImage(String imagePath, int width, int heigth) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(imagePath));
            image = ScaleImage(image, width, heigth);
        } catch (IOException e) {
            System.out.println("ERROR LOADING IMAGE. PATH: " + imagePath + ".png");
            e.printStackTrace();
        }
        return image;
    }
    private static BufferedImage ScaleImage(BufferedImage original, int width, int heigth) {
        BufferedImage scaledImage = new BufferedImage(width, heigth, 2);
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, heigth, null);
        g2.dispose();
        return scaledImage;
    }

    public static BufferedImage[] loadSpriteSheet(String path, int cols, int rows, int w, int h) {
        BufferedImage sheet;
        try {
            sheet = ImageIO.read(new File(path));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        int spriteW = sheet.getWidth() / cols;
        int spriteH = sheet.getHeight() / rows;

        BufferedImage[] sprites = new BufferedImage[rows * cols];

        int count = 0;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                sprites[count] = ScaleImage(sheet.getSubimage(x * spriteW, y * spriteH, spriteW, spriteH), w, h);
                count++;
            }
        }

        return sprites;
    }

    public static BufferedImage[][] load2DspriteSheet(String path, int cols, int rows, int w, int h) {
        BufferedImage[] sheet = loadSpriteSheet(path, cols, rows, w, h);

        if(sheet == null) {
            return null;
        }

        BufferedImage[][] res = new BufferedImage[cols][rows];

        int count = 0;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                res[x][y] = sheet[count];
                count++;
            }
        }

        return res;
    }



    /**
     * Dieses Projekt
     * @param name
     * @param json
     */


    private static final String PALETTE_FOLDER = "res/colorpalettes/";

    public static void savePalette(String name, String json) {
        try {
            Files.write(Paths.get(PALETTE_FOLDER + name + ".txt"), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] getPaletteNames() {
        File dir = new File(PALETTE_FOLDER);
        File[] possiblePalletes = dir.listFiles();

        String[] res = new String[possiblePalletes.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = possiblePalletes[i].getName().substring(0, possiblePalletes[i].getName().length()-4);
        }

        return res;
    }

    public static Color[] loadPalette(String name) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(PALETTE_FOLDER + name + ".txt")));
            ObjectMapper om = new ObjectMapper();
            Farbpalette pal = om.readValue(json, Farbpalette.class);
            List<Farbe> farben = pal.getColors();
            Color[] result = new Color[farben.size()];
            for (int i = 0; i < farben.size(); i++) {
                result[i] = new Color(farben.get(i).getR(), farben.get(i).getG(), farben.get(i).getB());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static final float[] loadHuePalette(String name) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(PALETTE_FOLDER + name + ".txt")));
            ObjectMapper om = new ObjectMapper();
            Farbpalette pal = om.readValue(json, Farbpalette.class);
            List<Farbe> farben = pal.getColors();
            float[] result = new float[farben.size()];
            for (int i = 0; i < farben.size(); i++) {
                result[i] = (float) farben.get(i).getHue();
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
