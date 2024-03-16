package de.luiskun.apps;

import javax.swing.*;
import java.awt.*;

public class Sandpiles extends Application{


    int[][] grid;
    int fieldsize = 350;
    int sandSize = HEIGHT / fieldsize;

    int speed = 1;

    JSlider updateSpeed;

    public Sandpiles() {
        super();

        resetField();

        int x = HEIGHT + 5;
        int y = 5;

        JLabel spL = new JLabel("Update speed:" + speed);
        spL.setBounds(x, y, 190, 15);
        this.add(spL);

        y+=20;

        updateSpeed = new JSlider();
        updateSpeed.setBounds(x, y, 190, 25);
        updateSpeed.setMinimum(0);
        updateSpeed.setMaximum(100);
        updateSpeed.setValue(1);
        updateSpeed.setBackground(Color.BLACK);
        updateSpeed.addChangeListener(listener -> {
            speed = updateSpeed.getValue();
            spL.setText("Update speed:" + speed);
        });
        this.add(updateSpeed);
    }

    private void resetField() {
        sandSize = WIDTH / fieldsize;

        grid = new int[fieldsize][fieldsize];

        grid[fieldsize/2][fieldsize/2] = 10000000;
    }


    @Override
    public void update() {
        for (int i = 0; i < speed; i++) {
            stack();
        }
    }

    private void stack() {
        int[][] nextGrid = new int[fieldsize][fieldsize];

        for (int x = 0; x < fieldsize; x++) {
            for (int y = 0; y < fieldsize; y++) {
                int num = grid[x][y];
                if (num < 4) {
                    nextGrid[x][y] = num;
                }
            }
        }

        for (int x = 0; x < fieldsize; x++) {
            for (int y = 0; y < fieldsize; y++) {
                int num = grid[x][y];
                if (num >= 4) {
                    nextGrid[x][y] += (num - 4);
                    if (x + 1 < fieldsize) {
                        nextGrid[x+1][y]++;
                    }
                    if (y + 1 < fieldsize) {
                        nextGrid[x][y+1]++;
                    }
                    if (x - 1 >= 0) {
                        nextGrid[x-1][y]++;
                    }
                    if (y - 1 >= 0) {
                        nextGrid[x][y-1]++;
                    }
                }
            }
        }

        grid = nextGrid;
    }


    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        for (int x = 0; x < fieldsize; x++) {
            for (int y = 0; y < fieldsize; y++) {

                int num = grid[x][y];

                Color col;

                switch (num) {
                    case 0:
                        col = new Color(0,0,0);
                        break;
                    case 1:
                        col = new Color(255, 0, 255);
                        break;
                    case 2:
                        col = new Color(0, 255, 255);
                        break;
                    case 3:
                    default:
                        col = new Color(255, 255, 255);
                        break;
                }

                g2.setColor(col);
                g2.fillRect(x*sandSize, y*sandSize, sandSize, sandSize);

            }
        }
    }
}
