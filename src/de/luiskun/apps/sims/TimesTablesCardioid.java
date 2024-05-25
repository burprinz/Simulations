package de.luiskun.apps.sims;

import de.luiskun.apps.Application;
import de.luiskun.tools.FileHandler;
import de.luiskun.tools.Vektor;
import de.luiskun.ui.uiparts.ColorPicker;
import de.luiskun.ui.uiparts.RangePicker;
import de.luiskun.ui.uiparts.RangeSelection;

import javax.swing.*;
import java.awt.*;

public class TimesTablesCardioid extends Application implements RangeSelection {

    private double r;
    private double factor = 0;
    private double speed = 0;
    private int offset;
    private boolean colorsAllowed;

    private Vektor center;

    private int maxPoints = 500;
    private int total = 200;
    private int pointR = 6;

    public TimesTablesCardioid() {
        super();

        this.center = new Vektor(HEIGHT / 2, HEIGHT / 2);
        this.offset = 8;
        this.r = HEIGHT / 2 - this.offset;


        initControlBoard();
    }

    //private JSlider amountPointsSlider, speedSlider;

    private RangePicker amountPointsSlider, speedSlider;
    private JCheckBox withColors;
    private JLabel amPInfo, speedInfo;
    ColorPicker color;

    private void initControlBoard() {
        int x = HEIGHT + 5;
        int y = 10;
        amPInfo = new JLabel("Amount Points: " + total);
        amPInfo.setForeground(Color.WHITE);
        amPInfo.setBounds(x, y, 150, 25);
        this.add(amPInfo);

        y += 35;

        amountPointsSlider = new RangePicker(this, 0, maxPoints, total, "amP");
        amountPointsSlider.setBounds(x, y, 190, 45);
        amountPointsSlider.setMajorTickSpacing(100);
        amountPointsSlider.setMinorTickSpacing(1);
        amountPointsSlider.setPaintTicks(true);
        amountPointsSlider.setPaintLabels(true);
        this.add(amountPointsSlider);

        y += 55;

        speedInfo = new JLabel("Speed: " + speed);
        speedInfo.setBounds(x, y, 150, 25);
        speedInfo.setForeground(Color.WHITE);
        this.add(speedInfo);

        y += 35;

        speedSlider = new RangePicker(this, 0, 100, (int)(speed*100), "sp");
        speedSlider.setBounds(x, y, 190, 30);
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

        color = new ColorPicker(150, true);
        color.setBounds(x, y, 150, 50);
        this.add(color);

        y += 55;

        JLabel backInfo = new JLabel("Z = Menu");
        backInfo.setBounds(x, y, 150, 25);
        backInfo.setForeground(Color.WHITE);
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
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        g2.setStroke(new BasicStroke(2));
        g2.drawOval(offset, offset, HEIGHT - 2 * offset, HEIGHT - 2 * offset);
        g2.setStroke(new BasicStroke(1));


        for (int i = 0; i < total; i++) {
            Vektor pointPos = Vektor.getPosOnCircle(i, total, r);
            pointPos = Vektor.toScreen(pointPos, HEIGHT, HEIGHT);
            g2.setColor(colorsAllowed?new Color(Color.HSBtoRGB(getFactor(i), 1f, 1f)):Color.WHITE);
            g2.fillOval(pointPos.xAsInt() - pointR, pointPos.yAsInt() - pointR, 2 * pointR, 2 * pointR);
        }

        for (int i = 0; i < total; i++) {
            Vektor a = Vektor.toScreen(Vektor.getPosOnCircle(i, total, r), HEIGHT, HEIGHT);
            Vektor b = Vektor.toScreen(Vektor.getPosOnCircle(i * factor, total, r), HEIGHT, HEIGHT);
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

        if(color.hue != null) {

            int val = (int) ((color.hue.length / amDots) * pos);

            if (val >= color.hue.length) {
                val = color.hue.length - 1;
            }

            return color.hue[val];
        } else {
            return 1f / amDots * pos;
        }
    }

    private float getLineColor(float color1, float color2) {
        return (color1 + color2) / 2f;
    }

    @Override
    public void rangeChanged(String com, int am) {
        int val;
        switch (com) {
            case "amP":
                val = amountPointsSlider.getValue();
                amPInfo.setText("Amount Points: " + val);
                this.total = val;
                break;
            case "sp":
                val = speedSlider.getValue();
                double speed = (double) val / (double) 100;
                speedInfo.setText("Speed: " + speed);
                this.speed = speed;
                break;
        }
    }
}
