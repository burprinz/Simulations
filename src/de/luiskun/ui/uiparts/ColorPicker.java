package de.luiskun.ui.uiparts;

import de.luiskun.tools.FileHandler;

import javax.swing.*;
import java.awt.*;

public class ColorPicker extends JPanel {

    JComboBox selectedPalette;

    public Color[] cur;
    public float[] hue;

    public ColorPicker(int w, boolean isHue) {
        this.setLayout(null);
        this.setBackground(Color.BLACK);

        JLabel colorinfo = new JLabel("Color Palette");
        colorinfo.setBounds(0, 0, w, 15);
        colorinfo.setForeground(Color.WHITE);
        this.add(colorinfo);

        String[] palletes = FileHandler.getPaletteNames();
        String[] goodPallettes = null;

        if (isHue) {
            goodPallettes = new String[palletes.length - 2];
            int c = 0;
            for(int i = 0; i < palletes.length; i++) {
                if (!palletes[i].equals("rainbow") && !palletes[i].equals("darkyellow")) {
                    goodPallettes[c] = palletes[i];
                    c++;
                }
            }
        } else {
            goodPallettes = palletes;
        }

        selectedPalette = new JComboBox(goodPallettes);
        selectedPalette.setBounds(0, 20, 180, 30);
        selectedPalette.addActionListener(listener -> {
            if (isHue) {
                hue = FileHandler.loadHuePalette(selectedPalette.getSelectedItem().toString());
            } else {
                cur = FileHandler.loadPalette(selectedPalette.getSelectedItem().toString());
            }
        });
        if (isHue) {
            hue = FileHandler.loadHuePalette(selectedPalette.getSelectedItem().toString());
        } else {
            cur = FileHandler.loadPalette(selectedPalette.getSelectedItem().toString());
        }
        this.add(selectedPalette);

    }

}
