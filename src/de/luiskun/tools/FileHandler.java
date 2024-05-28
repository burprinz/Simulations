package de.luiskun.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.luiskun.json.Farbe;
import de.luiskun.json.Farbpalette;
import de.luiskun.json.GOL_Preset;
import de.luiskun.json.IntegerPunkt;

import java.util.Arrays;
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

    public static Vektor[] readGOLPreset(boolean isCustom, String name) {
        String path = "res/sims/gameoflife/";
        path+=isCustom?"custom":"pre";
        path += "/" + name;

        try {
            String json = new String(Files.readAllBytes(Paths.get(path)));
            ObjectMapper om = new ObjectMapper();
            GOL_Preset pre = om.readValue(json, GOL_Preset.class);
            List<IntegerPunkt> pts = pre.getPoints();
            Vektor[] result = new Vektor[pts.size()];
            for (int i = 0; i < pts.size(); i++) {
                result[i] = new Vektor(pts.get(i).getX(), pts.get(i).getY());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveGOLPreset(List<Vektor> v, String name) {
        Vektor[] vs = new Vektor[v.size()];
        for (int i = 0; i < v.size(); i++) vs[i] = new Vektor(v.get(i).xAsInt(), v.get(i).yAsInt());

        GOL_Preset pre = new GOL_Preset(vs);
        ObjectMapper om = new ObjectMapper();
        try {
           om.writeValue(new File("res/sims/gameoflife/custom/" + name + ".txt"), pre);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage[] loadRow(String path, int size, int w, int imgw, int imgh) {
        BufferedImage img = SetupImage(path, imgw, imgh);
        BufferedImage[] imgs = new BufferedImage[w];
        for (int i = 0; i < w; i++) {
            imgs[i] = ScaleImage(cropImage(img, i*16, 0, 16, 16), size, size);
        }
        return imgs;
    }

    public static String[] readGOLNAMES(String folder) {

        File[] names = new File("res/sims/gameoflife/" + folder).listFiles();

        String[] res = new String[names.length];

        for (int i = 0; i < names.length; i++) {
            res[i] = names[i].getName();
        }

        return res;

    }


    private static BufferedImage cropImage(BufferedImage sheet, int startX, int startY, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        for (int y = startY; y < startY+h; y++) {
            for (int x = startX; x < startX+w; x++) {
                img.setRGB(x-startX, y-startY, sheet.getRGB(x, y));
            }
        }

        return img;
    }



    public static Font getPixelFont() {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/Pixelon-OGALo.ttf")).deriveFont(12f);
        } catch (FontFormatException | IOException e) {
            return null;
        }
    }

}
