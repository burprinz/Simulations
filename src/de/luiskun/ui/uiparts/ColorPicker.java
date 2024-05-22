package de.luiskun.ui.uiparts;

import de.luiskun.tools.FileHandler;

import javax.swing.*;
import java.awt.*;

public class ColorPicker extends JPanel {

    JComboBox selectedPalette;

    public Color[] cur;

    public ColorPicker(int w) {
        this.setLayout(null);
        this.setBackground(Color.BLACK);

        JLabel colorinfo = new JLabel("Color Palette");
        colorinfo.setBounds(0, 0, w, 15);
        colorinfo.setForeground(Color.WHITE);
        this.add(colorinfo);

        String[] palletes = FileHandler.getPaletteNames();
        selectedPalette = new JComboBox(palletes);
        selectedPalette.setBounds(0, 20, 180, 30);
        selectedPalette.addActionListener(listener -> {
            cur = FileHandler.loadPalette(selectedPalette.getSelectedItem().toString());
        });
        cur = FileHandler.loadPalette(selectedPalette.getSelectedItem().toString());
        this.add(selectedPalette);

    }

}
