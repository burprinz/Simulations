package de.luiskun.menu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.luiskun.FileHandler;
import de.luiskun.json.Farbe;
import de.luiskun.json.Farbpalette;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        new Main();
    }
    public Main() {
        JFrame frame = new JFrame();
        Screen s = new Screen();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(s);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        s.startThread();

        /*
        List<Farbe> farben = new ArrayList<>();

        float delta = 1f / 500f;

        for (int i = 0; i < 500; i++) {
            Color c = new Color(Color.HSBtoRGB(delta * i, 1f, 1f));
            farben.add(new Farbe(c.getRed(), c.getGreen(), c.getBlue()));
        }

        Farbpalette pal = new Farbpalette();
        pal.setColors(farben);

        ObjectMapper om = new ObjectMapper();
        try {
            String json = om.writeValueAsString(pal);
            FileHandler.savePalette( "rainbowDetail", json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

         */

    }

}
