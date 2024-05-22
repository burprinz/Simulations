package de.luiskun.apps.sorting;

import de.luiskun.apps.Application;
import de.luiskun.ui.uiparts.RangePicker;
import de.luiskun.ui.uiparts.RangeSelection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public abstract class SortingAlgorithm extends Application implements Runnable, ActionListener, RangeSelection  {

    private static final int maxSize = WIDTH-400;
    private static final int colorW = WIDTH - 10;
    private static final int colorH = HEIGHT - 40;

    protected int am = 250;
    protected int newAm = am;

    protected int speed = 10;

    protected int[] list;

    Thread t;
    boolean sorting;

    JButton startB, resetB;
    RangePicker amountPicker, delayPicker;
    JLabel currentAm, currentSpeed;

    public SortingAlgorithm() {

        int x = 5, y = colorH + 10, h = 25;

        startB = new JButton("Start");
        startB.setBounds(x, y, 75, h);
        startB.addActionListener(this);
        startB.setActionCommand("start");
        this.add(startB);

        x += 80;

        resetB = new JButton("Reset");
        resetB.setBounds(x, y, 75, h);
        resetB.addActionListener(this);
        resetB.setActionCommand("reset");
        this.add(resetB);

        x += 80;

        amountPicker = new RangePicker(this, 5, maxSize, am, "size");
        amountPicker.setBounds(x, y, 300, h);
        amountPicker.setMajorTickSpacing(100);
        amountPicker.setPaintLabels(true);
        amountPicker.setForeground(Color.WHITE);
        this.add(amountPicker);

        x += 305;

        currentAm = new JLabel("Amount: " + newAm);
        currentAm.setBounds(x, y, 75, h);
        currentAm.setForeground(Color.WHITE);
        this.add(currentAm);

        x += 80;

        delayPicker = new RangePicker(this, 1, 500, speed, "speed");
        delayPicker.setBounds(x, y, 200, h);
        delayPicker.setMajorTickSpacing(200);
        delayPicker.setPaintLabels(true);
        delayPicker.setForeground(Color.WHITE);
        this.add(delayPicker);

        x += 205;

        currentSpeed = new JLabel("Speed: " + speed + "ms");
        currentSpeed.setBounds(x, y, 100, h);
        currentSpeed.setForeground(Color.WHITE);
        this.add(currentSpeed);

        reset();
    }

    private void reset() {
        stop();

        am = newAm;

        list = new int[am];

        for (int i = 0; i < am; i++) {
            list[i] = i;
        }

        Random r = new Random();

        for (int i = 0; i < am; i++) {
            int pos = r.nextInt(am);
            int tmp = list[pos];
            list[pos] = list[i];
            list[i] = tmp;
        }

    }

    private void start() {
        sorting = true;
        t = new Thread(this);
        t.start();
    }

    private void stop() {
        sorting = false;
    }

    protected void swap(int i, int j) {
        int temp = list[i];
        list[i] = list[j];
        list[j] = temp;
        try {
            Thread.sleep(speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void paintComponent(Graphics g) {
        final float cW = (float)colorW / (float)am;

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                float hue = 1f / (float) am * (float) list[i];
                g2.setColor(new Color(Color.HSBtoRGB(hue, 1f, 1f)));
                g2.fillRect(5 + (int)(i*cW), 5, (int) cW+30, colorH);
            }
        }

        g2.setColor(Color.BLACK);
        g2.fillRect(WIDTH-5, 0, 5, HEIGHT);
    }

    @Override
    public void run() {
        sort();
    }

    @Override
    public void rangeChanged(String com, int am) {
        switch (com) {
            case "size":
                newAm = am;
                currentAm.setText("Amount: " + newAm);
                break;
            case "speed":
                speed = am;
                currentSpeed.setText("Speed: " + speed + "ms");
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "start":
                if (!sorting) {
                    start();
                }
                break;
            case "reset":
                reset();
                break;
        }
    }

    @Override
    public void close() {
        stop();
    }

    protected abstract void sort();
}
