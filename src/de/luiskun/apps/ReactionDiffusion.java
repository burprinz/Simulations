package de.luiskun.apps;

import de.luiskun.apps.classes.Chemical;
import de.luiskun.menu.Screen;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ReactionDiffusion extends Application{

    int width, height;

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
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDoubleBuffered(true);
        this.setLayout(null);

        this.width = Screen.WIDTH - 200;
        this.height = Screen.HEIGHT;

        resetGrid();
        initControl();
    }

    private void resetGrid() {


        Chemical[][] grid = new Chemical[width/pixelsize][height/pixelsize];

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


    JSlider formRaidiusS, bFactorS, calcSpeedS;
    JButton startB, stopS, resetB;
    private void initControl() {

        int x = width + 10;
        int y = 10;

        JLabel formRadInfo = new JLabel("Form Radius: " + formRadius);
        formRadInfo.setBounds(x, y, 150, 15);
        this.add(formRadInfo);

        y += 20;

        formRaidiusS = new JSlider();
        formRaidiusS.setBounds(x, y, 190, 45);
        formRaidiusS.setMinimum(0);
        formRaidiusS.setMaximum(150);
        formRaidiusS.setValue(formRadius);
        formRaidiusS.setMajorTickSpacing(50);
        formRaidiusS.setPaintLabels(true);
        formRaidiusS.addChangeListener(listener -> {
            simulationRunning = false;
            resetGrid();
            formRadius = formRaidiusS.getValue();
            formRadInfo.setText("Form Radius: " + formRadius);
        });
        this.add(formRaidiusS);

        y += 50;

        JLabel bFactInfo = new JLabel("B Factor" + dB);
        bFactInfo.setBounds(x, y, 150, 15);
        this.add(bFactInfo);

        y += 20;

        bFactorS = new JSlider();
        bFactorS.setBounds(x, y, 190, 45);
        bFactorS.setMinimum(1);
        bFactorS.setMaximum(13);
        bFactorS.setValue(5);
        bFactorS.setPaintLabels(true);
        bFactorS.addChangeListener(listener -> {
            simulationRunning = false;
            resetGrid();
            dB = (double) bFactorS.getValue() / (double) 10;
            bFactInfo.setText("B Factor" + dB);
        });
        this.add(bFactorS);

        y += 50;

        JLabel calcSpeedInfo = new JLabel("Calc speed: " + calcSpeed);
        calcSpeedInfo.setBounds(x, y, 150, 15);
        this.add(calcSpeedInfo);

        y += 20;

        calcSpeedS = new JSlider();
        calcSpeedS.setBounds(x, y, 190, 45);
        calcSpeedS.setMinimum(1);
        calcSpeedS.setMaximum(15);
        calcSpeedS.setValue(calcSpeed);
        calcSpeedS.setPaintLabels(true);
        calcSpeedS.addChangeListener(listener -> {
            calcSpeed = calcSpeedS.getValue();
            calcSpeedInfo.setText("Calc speed: " + calcSpeed);
        });
        this.add(calcSpeedS);

        y += 50;

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
                Chemical[][]  next = new Chemical[width/pixelsize][height/pixelsize];

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

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width + 200, height);

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
}
