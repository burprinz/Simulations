package de.luiskun.apps.sims;

import de.luiskun.apps.Application;
import de.luiskun.apps.sims.classes.Chemical;
import de.luiskun.ui.uiparts.ColorPicker;
import de.luiskun.ui.uiparts.RangePicker;
import de.luiskun.ui.uiparts.RangeSelection;

import javax.swing.*;
import java.awt.*;

public class ReactionDiffusion extends Application implements RangeSelection {

    private Chemical[][] grid;

    int pixelsize = 2;

    double dA = 1;
    double dB = 0.5;
    double feed = 0.055;
    double k = 0.062;

    boolean simulationRunning = true;

    int formRadius = 25;
    int calcSpeed = 1;

    public ReactionDiffusion() {
        super();

        resetGrid();
        initControl();
    }

    private void resetGrid() {


        Chemical[][] grid = new Chemical[HEIGHT/pixelsize][HEIGHT/pixelsize];

        for(int x = 0; x < grid.length; x++) {
            for(int y = 0; y < grid[0].length; y++) {
                grid[x][y] = new Chemical();

                grid[x][y].a = 1;
                grid[x][y].b = 0;

            }
        }

        for(int i = grid.length / 2 - formRadius; i < grid.length / 2 + formRadius; i++) {
            for(int j = grid[0].length / 2 - formRadius; j < grid[0].length / 2  + formRadius; j++) {
                if(i >= 0 && j >= 0 && i < grid.length && j < grid.length) {
                    grid[i][j].b = 1;
                }
            }
        }

        this.grid = grid;
    }


    RangePicker formRaidiusS, bFactorS, calcSpeedS;
    JButton startB, stopS, resetB;
    JLabel formRadInfo, calcSpeedInfo, bFactInfo;
    JCheckBox colorAllowed;
    ColorPicker color;

    boolean withColor;

    private void initControl() {

        int x = HEIGHT + 10;
        int y = 10;

        formRadInfo = new JLabel("Form Radius: " + formRadius);
        formRadInfo.setForeground(Color.WHITE);
        formRadInfo.setBounds(x, y, 150, 15);
        this.add(formRadInfo);

        y += 20;

        formRaidiusS = new RangePicker(this, 0, 150, formRadius, "fR");
        formRaidiusS.setBounds(x, y, 190, 45);
        formRaidiusS.setMajorTickSpacing(50);
        formRaidiusS.setPaintLabels(true);
        this.add(formRaidiusS);

        y += 50;

        bFactInfo = new JLabel("B Factor" + dB);
        bFactInfo.setBounds(x, y, 150, 15);
        bFactInfo.setForeground(Color.WHITE);
        this.add(bFactInfo);

        y += 20;

        bFactorS = new RangePicker(this, 1, 13, 5, "bF");
        bFactorS.setBounds(x, y, 190, 45);
        bFactorS.setPaintLabels(true);
        this.add(bFactorS);

        y += 50;

        calcSpeedInfo = new JLabel("Calc speed: " + calcSpeed);
        calcSpeedInfo.setBounds(x, y, 150, 15);
        calcSpeedInfo.setForeground(Color.WHITE);
        this.add(calcSpeedInfo);

        y += 20;

        calcSpeedS = new RangePicker(this, 1, 15, calcSpeed, "cS");
        calcSpeedS.setBounds(x, y, 190, 45);
        calcSpeedS.setPaintLabels(true);
        this.add(calcSpeedS);

        y += 50;

        colorAllowed = new JCheckBox("Colors");
        colorAllowed.setBounds(x, y, 150, 25);
        colorAllowed.addActionListener(listener -> {
            withColor = colorAllowed.isSelected();
        });
        this.add(colorAllowed);

        y += 30;

        color = new ColorPicker(150, false);
        color.setBounds(x, y, 150, 50);
        this.add(color);

        y += 55;

        startB = new JButton("Start");
        startB.setBounds(x, y, 150, 25);
        startB.addActionListener(listener -> {
            simulationRunning = true;
        });
        this.add(startB);

        y += 30;

        stopS = new JButton("Stop");
        stopS.setBounds(x, y, 150, 25);
        stopS.addActionListener(listener -> {
            simulationRunning = false;
        });
        this.add(stopS);

        y += 30;

        resetB = new JButton("Reset");
        resetB.setBounds(x, y, 150, 25);
        resetB.addActionListener(listener -> {
            simulationRunning = false;
            resetGrid();
        });
        this.add(resetB);

    }

    @Override
    public void update() {

        for(int speed = 0; speed < calcSpeed; speed++) {
            if (simulationRunning && grid != null) {
                Chemical[][]  next = new Chemical[HEIGHT/pixelsize][HEIGHT/pixelsize];

                for(int x = 1; x < grid.length-1; x++) {
                    for (int y = 1; y < grid[0].length - 1; y++) {
                        next[x][y] = new Chemical();

                        double a = 0;
                        double b = 0;

                        if(grid[x][y] != null) {
                            a = grid[x][y].a;
                            b = grid[x][y].b;
                        }
                        next[x][y].a = a + (dA * laplaceA(x, y)) -
                                (a * b * b) +
                                (feed * (1 - a));
                        next[x][y].b = b + (dB * laplaceB(x, y)) +
                                (a * b * b) -
                                ((k + feed) * b);
                    }
                }

                for(int x = 1; x < grid.length-1; x++) {
                    for(int y = 1; y < grid[0].length-1; y++) {
                        grid[x][y] = next[x][y];
                    }
                }

            }
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        if (!withColor) {
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, WIDTH, HEIGHT);
        } else {
            int range = (int)(((float)HEIGHT/(float)color.cur.length)/2f);
            int len = range!=0?color.cur.length:HEIGHT/2;
            range = range!=0?range:1;
            for (int i = 0; i < len; i++) {
                g2.setColor(color.cur[i]);
                g2.fillRect(i*range, i*range, HEIGHT-2*i*range, HEIGHT-2*i*range);
            }
        }


        g2.setColor(Color.BLACK);
        g2.fillRect(HEIGHT, 0, WIDTH-HEIGHT, HEIGHT);

        if(grid != null) {

            for(int x = 0; x < grid.length; x++) {
                for(int y = 0; y < grid[0].length; y++) {

                    int gamma = (int)((grid[x][y].a - grid[x][y].b)*255d);
                    if(gamma > 255) {
                        gamma = 255;
                    }
                    if(gamma < 0) {
                        gamma = 0;
                    }

                    Color c = new Color(0, 0, 0, gamma);
                    g2.setColor(c);
                    g2.fillRect(x*pixelsize, y*pixelsize, pixelsize, pixelsize);
                }
            }
        }

    }

    private double laplaceA(int x, int y) {

        double sumA = 0;

        sumA += grid[x][y].a * -1;
        sumA += grid[x-1][y].a * 0.2;
        sumA += grid[x+1][y].a * 0.2;
        sumA += grid[x][y+1].a * 0.2;
        sumA += grid[x][y-1].a * 0.2;
        sumA += grid[x-1][y-1].a * 0.05;
        sumA += grid[x+1][y-1].a * 0.05;
        sumA += grid[x-1][y+1].a * 0.05;
        sumA += grid[x+1][y+1].a * 0.05;

        return sumA;
    }

    private double laplaceB(int x, int y) {
        double sumB = 0;
        sumB += grid[x][y].b * -1;
        sumB += grid[x-1][y].b * 0.2;
        sumB += grid[x+1][y].b * 0.2;
        sumB += grid[x][y+1].b * 0.2;
        sumB += grid[x][y-1].b * 0.2;
        sumB += grid[x-1][y-1].b * 0.05;
        sumB += grid[x+1][y-1].b * 0.05;
        sumB += grid[x-1][y+1].b * 0.05;
        sumB += grid[x+1][y+1].b * 0.05;

        return sumB;
    }

    @Override
    public void rangeChanged(String com, int am) {
        switch (com) {
            case "fR":
                simulationRunning = false;
                resetGrid();
                formRadius = formRaidiusS.getValue();
                formRadInfo.setText("Form Radius: " + formRadius);
                break;
            case "bF":
                simulationRunning = false;
                resetGrid();
                dB = (double) bFactorS.getValue() / (double) 10;
                bFactInfo.setText("B Factor" + dB);
                break;
            case "cS":
                calcSpeed = calcSpeedS.getValue();
                calcSpeedInfo.setText("Calc speed: " + calcSpeed);
                break;
        }
    }
}
