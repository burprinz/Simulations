package de.luiskun.apps.sims;

import de.luiskun.tools.Vektor;
import de.luiskun.apps.sims.classes.Boid;
import de.luiskun.ui.Screen;
import de.luiskun.ui.uiparts.RangePicker;
import de.luiskun.ui.uiparts.RangeSelection;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Boids extends Application implements RangeSelection {

    /**
     * Values
     */
    public static final int settingsW = 200;
    private static final int densitySize = 10;

    /**
     * Standard sizes
     */
    private static final double stdCoh = 1, stdAll = 1, stdSep = 4.5;
    private static final int stdHeatRange = 100;

    /**
     * Gradient
     */
    private static final Color[] gradient = new Color[] {
            new Color(5, 185, 209),
            new Color(46, 155, 174),
            new Color(81, 129, 146),
            new Color(148, 79, 90),
            new Color(193, 47, 53),
            new Color(249, 4, 5)
    };

    /**
     * UI Elements
     */
    JCheckBox boidColor, triangleBoids, heatmap, allCenter;
    RangePicker cohesionImpact, allignImpact, separationImpact, heatRadius;
    JLabel heatRadiusInfo, cohImInfo, allImInfo, sepImInfo;
    JButton standardSettinngB, resetB;

    /**
     * Field
     */
    int amBoids = 100;
    List<Boid> flock;
    int[][] density = new int[WIDTH/densitySize][HEIGHT/densitySize];

    /**
     * Settings
     */
    public boolean coloredBoids, triangleBoid, centerStart;
    boolean heatmapEnabled;
    int heatRange = stdHeatRange;
    public double cohImpact = stdCoh, allImpact = stdAll, sepImpact = stdSep;

    public Boids() {

        initSettings();

        reset();
    }

    private void initSettings() {
        int x = WIDTH - settingsW + 10;
        int w = settingsW - 20;
        int y = 10;

        boidColor = new JCheckBox("Colored Boids");
        boidColor.setBounds(x, y, w, 25);
        boidColor.addChangeListener(listener -> {
            coloredBoids = boidColor.isSelected();
        });
        this.add(boidColor);

        y += 30;

        triangleBoids = new JCheckBox("Circle/Triangle");
        triangleBoids.setBounds(x, y, w, 25);
        triangleBoids.addChangeListener(listener -> {
            triangleBoid = triangleBoids.isSelected();
        });
        this.add(triangleBoids);

        y += 30;

        heatmap = new JCheckBox("Heatmap");
        heatmap.setBounds(x, y, w, 25);
        heatmap.addChangeListener(listener -> {
            heatmapEnabled = heatmap.isSelected();
        });
        this.add(heatmap);

        y += 30;

        allCenter = new JCheckBox("Start from center");
        allCenter.setBounds(x, y, w, 25);
        allCenter.addChangeListener(listener -> {
            centerStart = allCenter.isSelected();
        });
        this.add(allCenter);

        y += 30;
        heatRadiusInfo = new JLabel("Heat radius: " + heatRange);
        heatRadiusInfo.setForeground(Color.WHITE);
        heatRadiusInfo.setBounds(x, y, w, 15);
        this.add(heatRadiusInfo);

        y += 20;

        heatRadius = new RangePicker(this, 1, 200, heatRange, "hRange");
        heatRadius.setBounds(x, y, w, 25);
        this.add(heatRadius);

        y += 30;

        JLabel impactInfo = new JLabel("<html><u>Impact</u></html>");
        impactInfo.setForeground(Color.WHITE);
        impactInfo.setBounds(x, y, w, 15);
        this.add(impactInfo);

        y += 25;

        cohImInfo = new JLabel("Cohesion impact: " + cohImpact);
        cohImInfo.setForeground(Color.WHITE);
        cohImInfo.setBounds(x, y, w, 15);
        this.add(cohImInfo);

        y += 20;

        cohesionImpact = new RangePicker(this, 10, 100, (int)(cohImpact*10), "cIm");
        cohesionImpact.setBounds(x, y, w, 25);
        this.add(cohesionImpact);

        y += 30;

        allImInfo = new JLabel("Alignment impact: " + allImpact);
        allImInfo.setForeground(Color.WHITE);
        allImInfo.setBounds(x, y, w, 15);
        this.add(allImInfo);

        y += 20;

        allignImpact = new RangePicker(this, 10, 100, (int)(allImpact*10), "aIm");
        allignImpact.setBounds(x, y, w, 25);
        this.add(allignImpact);

        y += 30;

        sepImInfo = new JLabel("Separation impact: " + sepImpact);
        sepImInfo.setForeground(Color.WHITE);
        sepImInfo.setBounds(x, y, w, 15);
        this.add(sepImInfo);

        y += 20;

        separationImpact = new RangePicker(this, 10, 150, (int)(sepImpact*10), "sIm");
        separationImpact.setBounds(x, y, w, 25);
        this.add(separationImpact);

        y += 30;

        standardSettinngB = new JButton("Standard settings");
        standardSettinngB.setBounds(x, y, w, 25);
        standardSettinngB.addActionListener(listener -> {
            heatRange = stdHeatRange;
            cohImpact = stdCoh;
            allImpact = stdAll;
            sepImpact = stdSep;
            heatRadius.setValue(stdHeatRange);
            cohesionImpact.setValue((int)(stdCoh*10));
            separationImpact.setValue((int)(stdSep*10));
            allignImpact.setValue((int)(stdAll*10));
            heatRadiusInfo.setText("Heat radius: " + heatRange);
            cohImInfo.setText("Cohesion impact:" + cohImpact);
            sepImInfo.setText("Separation impact: " + sepImpact);
            allImInfo.setText("Alignment impact: " + allImpact);
        });
        this.add(standardSettinngB);

        y += 30;

        resetB = new JButton("Reset");
        resetB.setBounds(x, y, w, 25);
        resetB.addActionListener(listener -> {
            reset();
        });
        this.add(resetB);

    }

    private void reset() {
        flock = new ArrayList<>();

        Random r = new Random();

        for (int i = 0; i < amBoids; i++) {

            int x = !centerStart?r.nextInt(Screen.WIDTH- Boids.settingsW):WIDTH/2 - 60 + r.nextInt(120);
            int y = !centerStart?r.nextInt(Screen.HEIGHT):HEIGHT/2 - 60 + r.nextInt(120);

            flock.add(new Boid(this, x, y));
        }
    }

    private int getDensity(Vektor pos) {
        int dens = 0;

        for (Boid b : flock) {
            if (isInRange(pos, b.posisiton , heatRange)) {
                dens++;
            }
        }

        return dens;
    }

    boolean isInRange(Vektor pos, Vektor boid, int radius) {
        return Vektor.dist(pos, boid) <= radius;
    }

    @Override
    public void update() {
        for (Boid b : flock) {
            b.edges();
            b.flock(flock);
            b.update();
        }

        if (heatmapEnabled) {
            for (int y = 0; y < density[0].length; y++) {
                for (int x = 0; x < density.length; x++) {
                    Vektor pos = new Vektor(x*densitySize+densitySize/2, y*densitySize+densitySize/2);
                    density[x][y] = getDensity(pos);
                }
            }
        }

    }

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, Screen.WIDTH, Screen.HEIGHT);

        if (heatmapEnabled) {
            for (int y = 0; y < density[0].length; y++) {
                for (int x = 0; x < density.length; x++) {
                    int col = (int)((double)density[x][y]/10*(gradient.length-1));
                    col = col>=gradient.length?gradient.length-1:col;
                    g2.setColor(gradient[col]);
                    g2.fillRect(x*densitySize, y*densitySize, densitySize, densitySize);
                }
            }
        }

        for (Boid b : flock) {
            b.draw(g2);
        }

        g2.setColor(Color.BLACK);
        g2.fillRect(WIDTH-settingsW,0,settingsW, HEIGHT);
    }

    @Override
    public void rangeChanged(String com, int am) {
        switch (com) {
            case "hRange":
                heatRange = am;
                heatRadiusInfo.setText("Heat radius: " + heatRange);
                break;
            case "cIm":
                cohImpact = am/10d;
                cohImInfo.setText("Cohesion impact:" + cohImpact);
                break;
            case "sIm":
                sepImpact = am/10d;
                sepImInfo.setText("Separation impact: " + sepImpact);
                break;
            case "aIm":
                allImpact = am/10d;
                allImInfo.setText("Alignment impact: " + allImpact);
                break;
        }
    }
}
