package de.luiskun.apps;

import de.luiskun.FileHandler;
import de.luiskun.Vektor;
import de.luiskun.menu.Screen;

import javax.swing.*;
import java.awt.*;

public class TimesTablesCardioid extends Application{

    private int width, height;
    private double r;
    private double factor = 0;
    private double speed = 0;
    private int offset;
    private boolean colorsAllowed;

    private Vektor center;

    private int maxPoints = 500;
    private int total = 200;
    private int pointR = 6;
    private float[] colorset;

    public TimesTablesCardioid() {

        this.width = Screen.WIDTH - 200;
        this.height = Screen.HEIGHT;
        this.center = new Vektor(width / 2, height / 2);
        this.offset = 8;
        this.r = height / 2 - this.offset;

        this.setPreferredSize(new Dimension(Screen.WIDTH, Screen.HEIGHT));
        this.setDoubleBuffered(true);
        this.setLayout(null);

        initControlBoard();
    }

    private JSlider amountPointsSlider, speedSlider;
    private JCheckBox withColors;

    private JComboBox colorSelector;

    private void initControlBoard() {
        int x = width + 5;
        int y = 10;
        JLabel amPInfo = new JLabel("Amount Points: " + total);
        amPInfo.setBounds(x, y, 150, 25);
        this.add(amPInfo);

        y += 35;

        amountPointsSlider = new JSlider();
        amountPointsSlider.setBounds(x, y, 190, 45);
        amountPointsSlider.setMinimum(0);
        amountPointsSlider.setMaximum(maxPoints);
        amountPointsSlider.setValue(total);
        amountPointsSlider.setMajorTickSpacing(100);
        amountPointsSlider.setMinorTickSpacing(1);
        amountPointsSlider.setPaintTicks(true);
        amountPointsSlider.setPaintLabels(true);
        amountPointsSlider.addChangeListener(listener -> {
            int val = amountPointsSlider.getValue();
            amPInfo.setText("Amount Points: " + val);
            this.total = val;
        });
        this.add(amountPointsSlider);

        y += 55;

        JLabel speedInfo = new JLabel("Speed: " + speed);
        speedInfo.setBounds(x, y, 150, 25);
        this.add(speedInfo);

        y += 35;

        speedSlider = new JSlider();
        speedSlider.setBounds(x, y, 190, 30);
        speedSlider.setMinimum(0);
        speedSlider.setMaximum(100);
        speedSlider.setValue((int)(speed*100));
        speedSlider.addChangeListener(listener -> {
            int val = speedSlider.getValue();
            double speed = (double) val / (double) 100;
            speedInfo.setText("Speed: " + speed);
            this.speed = speed;
        });
        this.add(speedSlider);

        y += 40;

        withColors = new JCheckBox("Colors");
        withColors.setSelected(false);
        withColors.addChangeListener(listener -> {
            this.colorsAllowed = withColors.isSelected();
        });
        withColors.setBounds(x, y, 150, 25);
        this.add(withColors);

        y += 35;

        String[] palletes = FileHandler.getPaletteNames();

        String[] goodPallettes = new String[palletes.length - 2];
        int c = 0;
        for(int i = 0; i < palletes.length; i++) {
            if (!palletes[i].equals("rainbow") && !palletes[i].equals("darkyellow")) {
                goodPallettes[c] = palletes[i];
                c++;
            }
        }

        colorSelector = new JComboBox(goodPallettes);
        colorSelector.setBounds(x, y, 180, 30);
        colorSelector.addActionListener(listener -> {
            colorset = FileHandler.loadHuePalette(colorSelector.getSelectedItem().toString());
        });
        colorset = FileHandler.loadHuePalette(colorSelector.getSelectedItem().toString());
        this.add(colorSelector);

        y += 35;

        JLabel backInfo = new JLabel("Z = Menu");
        backInfo.setBounds(x, y, 150, 25);
        this.add(backInfo);

    }

    @Override
    public void update() {
        factor += speed;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, width, height);

        g2.setColor(Color.WHITE);
        g2.fillRect(width, 0, 200, height);

        g2.setStroke(new BasicStroke(2));
        g2.drawOval(offset, offset, width - 2 * offset, height - 2 * offset);
        g2.setStroke(new BasicStroke(1));


        for (int i = 0; i < total; i++) {
            Vektor pointPos = Vektor.getPosOnCircle(i, total, r);
            pointPos = Vektor.toScreen(pointPos, width, height);
            g2.setColor(colorsAllowed?new Color(Color.HSBtoRGB(getFactor(i), 1f, 1f)):Color.WHITE);
            g2.fillOval(pointPos.xAsInt() - pointR, pointPos.yAsInt() - pointR, 2 * pointR, 2 * pointR);
        }

        for (int i = 0; i < total; i++) {
            Vektor a = Vektor.toScreen(Vektor.getPosOnCircle(i, total, r), width, height);
            Vektor b = Vektor.toScreen(Vektor.getPosOnCircle(i * factor, total, r), width, height);
            if (colorsAllowed) {
                g2.setColor(new Color(Color.HSBtoRGB(getLineColor(getFactor(i), getFactor(i+1)), 1f, 1f)));
            } else {
                g2.setColor(Color.WHITE);
            }

            g2.drawLine(a.xAsInt(), a.yAsInt(), b.xAsInt(), b.yAsInt());
        }

    }

    private float getFactor(float pos) {
        final float amDots = total;

        if(colorset != null) {

            int val = (int) ((colorset.length / amDots) * pos);

            if (val >= colorset.length) {
                val = colorset.length - 1;
            }

            return colorset[val];
        } else {
            return 1f / amDots * pos;
        }
    }

    private float getLineColor(float color1, float color2) {
        return (color1 + color2) / 2f;
    }

}
