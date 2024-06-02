package de.luiskun.apps.sims;

import de.luiskun.apps.Application;
import de.luiskun.tools.Vektor;
import de.luiskun.ui.uiparts.ActionFunction;
import de.luiskun.ui.uiparts.RangePicker;
import de.luiskun.ui.uiparts.RangeSelection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BouncingCircle extends Application implements ActionListener, RangeSelection {

    Vektor middle = new Vektor(0, 0);
    int bigRadius = 300;
    int smallRadius = 25;
    Vektor smallPos = new Vektor(0, 0);
    Vektor direction = new Vektor(5, 1);
    Vektor gravity = new Vektor(0, -1);

    List<Vektor> collisionPoints;
    int amountColors = 60;
    int maxSpeed = 60;
    Map<String, ActionFunction> commands;

    JButton startButton, stopButton, resetButton;
    RangePicker xStart, yStart, vXStart, vYStart;

    // Edit
    boolean running = false;
    boolean firstStop = false;
    Vektor startPos = new Vektor(0, 0);
    Vektor startDirection = new Vektor(5, 1);
    Vektor newGravity = new Vektor(0, -1);

    public BouncingCircle() {
        collisionPoints = new ArrayList<>();
        commands = new ConcurrentHashMap<>();

        int w = WIDTH-HEIGHT-20;
        int x = HEIGHT + 10;
        int y = 10;

        startButton = new JButton("Start");
        startButton.setBounds(x, y, w, 25);
        startButton.setActionCommand("start");
        startButton.addActionListener(this);
        commands.put("start", startSim);
        this.add(startButton);
        y += 30;

        stopButton = new JButton("Stop");
        stopButton.setBounds(x, y, w, 25);
        stopButton.setActionCommand("stop");
        stopButton.addActionListener(this);
        commands.put("stop", stopSim);
        this.add(stopButton);
        y += 30;

        resetButton = new JButton("Reset");
        resetButton.setBounds(x, y, w, 25);
        resetButton.setActionCommand("reset");
        resetButton.addActionListener(this);
        commands.put("reset", resetSim);
        this.add(resetButton);
        y += 30;

        JLabel startPosInfo = new JLabel("Start position");
        startPosInfo.setForeground(Color.WHITE);
        startPosInfo.setBounds(x, y, w, 15);
        this.add(startPosInfo);
        y += 20;

        JLabel xStartInfo = new JLabel("X:");
        xStartInfo.setForeground(Color.WHITE);
        xStartInfo.setBounds(x, y, 15, 25);
        this.add(xStartInfo);
        y += 30;

        JLabel yStartInfo = new JLabel("Y:");
        yStartInfo.setForeground(Color.WHITE);
        yStartInfo.setBounds(x, y, 15, 25);
        this.add(yStartInfo);
        y -= 30;
        x += 15;

        xStart = new RangePicker(this, 0, HEIGHT, HEIGHT/2, "xStart");
        xStart.setBounds(x, y, w-15, 25);
        this.add(xStart);
        y += 30;

        yStart = new RangePicker(this, 0, HEIGHT, HEIGHT/2, "yStart");
        yStart.setBounds(x, y, w-15, 25);
        this.add(yStart);
        x = HEIGHT + 10;
        y += 30;

        JLabel startDirInfo = new JLabel("Start direction");
        startDirInfo.setForeground(Color.WHITE);
        startDirInfo.setBounds(x, y, w, 15);
        this.add(startDirInfo);
        y += 20;

        JLabel xVelInfo = new JLabel("X:");
        xVelInfo.setForeground(Color.WHITE);
        xVelInfo.setBounds(x, y, 15, 25);
        this.add(xVelInfo);
        y += 30;

        JLabel yVelInfo = new JLabel("Y:");
        yVelInfo.setForeground(Color.WHITE);
        yVelInfo.setBounds(x, y, 15, 25);
        this.add(yVelInfo);
        y -= 30;
        x += 15;

        vXStart = new RangePicker(this, 0, maxSpeed, maxSpeed/2+5, "vxStart");
        vXStart.setBounds(x, y, w-15, 25);
        this.add(vXStart);
        y += 30;

        vYStart = new RangePicker(this, 0, maxSpeed, maxSpeed/2+1, "vyStart");
        vYStart.setBounds(x, y, w-15, 25);
        this.add(vYStart);
        x = HEIGHT + 10;
        y += 30;
    }

    private void resetGame() {
        smallPos = new Vektor(startPos);
        gravity = new Vektor(newGravity);
        direction = new Vektor(startDirection);
        collisionPoints.clear();
    }

    private void adjustPos() {
        double distance = Math.sqrt(Math.pow(smallPos.x, 2) + Math.pow(smallPos.y, 2));
        if (distance + smallRadius > bigRadius) {
            Vektor collisionPos = new Vektor(middle);
            collisionPos.add(smallPos);
            collisionPos.limit(bigRadius-smallRadius);
            startPos = new Vektor(collisionPos);
            smallPos = new Vektor(collisionPos);
        }
    }

    @Override
    public void update() {

        if (running) {
            firstStop = true;

            direction.add(gravity);

            smallPos.add(direction);

            double distance = Math.sqrt(Math.pow(smallPos.x, 2) + Math.pow(smallPos.y, 2));

            if(distance + smallRadius > bigRadius) {

                Vektor collisionPos = new Vektor(middle);
                collisionPos.add(smallPos);
                collisionPos.limit(bigRadius);
                collisionPoints.add(collisionPos);

                smallRadius += 1;
                // Berechnung des Kollisionswinkels
                double collisionAngle = Math.atan2(smallPos.y, smallPos.x);

                double speed = Math.sqrt(direction.x * direction.x + direction.y * direction.y);
                double angleOfIncidence = Math.atan2(direction.y, direction.x);
                double angleOfReflection = 2 * collisionAngle - angleOfIncidence - Math.PI;

                direction.x = speed * Math.cos(angleOfReflection);
                direction.y = speed * Math.sin(angleOfReflection);

                direction.mult(1.02);

                // Korrigieren der Position des kleinen Kreises
                smallPos.x = (bigRadius - smallRadius) * Math.cos(collisionAngle);
                smallPos.y = (bigRadius - smallRadius) * Math.sin(collisionAngle);
            }
        } else if (firstStop) {
            firstStop = false;
            vXStart.setValue(direction.xAsInt() + maxSpeed/2);
            if (vXStart.getValue() < 0) {
                vXStart.setValue(0);
            } else if (vXStart.getValue() > maxSpeed) {
                vXStart.setValue(maxSpeed);
            }
            vYStart.setValue(direction.yAsInt() + maxSpeed/2);
            if (vYStart.getValue() < 0) {
                vYStart.setValue(0);
            } else if (vYStart.getValue() > maxSpeed) {
                vYStart.setValue(maxSpeed);
            }
            startDirection = new Vektor(vXStart.getValue(), vYStart.getValue());


        }

    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        Vektor middleCoords = Vektor.toScreen(middle, HEIGHT, HEIGHT);

        g2.setColor(Color.WHITE);
        g2.drawOval(middleCoords.xAsInt()-bigRadius, middleCoords.yAsInt()-bigRadius, 2*bigRadius, 2*bigRadius);

        g2.setColor(new Color(Color.HSBtoRGB(1f / amountColors * (smallRadius%amountColors) ,1f, 1f)));

        Vektor smallCoords = Vektor.toScreen(smallPos, HEIGHT, HEIGHT);
        g2.fillOval(smallCoords.xAsInt()-smallRadius, smallCoords.yAsInt()-smallRadius, 2*smallRadius, 2*smallRadius);

        for (Vektor vv : collisionPoints) {
            Vektor v = Vektor.toScreen(vv, HEIGHT, HEIGHT);
            g2.drawLine(v.xAsInt(), v.yAsInt(), smallCoords.xAsInt(), smallCoords.yAsInt());
        }

        drawControl(g2);

        if (!running) {
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            Vektor finishPos = new Vektor(smallPos);
            finishPos.add(direction);
            finishPos.mult(2);
            finishPos = Vektor.toScreen(finishPos, HEIGHT, HEIGHT);
            g2.drawLine(smallCoords.xAsInt(), smallCoords.yAsInt(), finishPos.xAsInt(), finishPos.yAsInt());
        }
    }

    private void drawControl(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(HEIGHT, 0, WIDTH-HEIGHT, HEIGHT);
    }

    @Override
    public void rangeChanged(String com, int am) {

        if (!running) {
            switch (com) {
                case "xStart":
                    startPos = Vektor.toKates(new Vektor(am, yStart.getValue()), HEIGHT, HEIGHT);
                    smallPos = new Vektor(startPos);
                    adjustPos();
                    break;
                case "yStart":
                    startPos = Vektor.toKates(new Vektor(xStart.getValue(), am), HEIGHT, HEIGHT);
                    smallPos = new Vektor(startPos);
                    adjustPos();
                    break;
                case "vxStart":
                    startDirection = new Vektor(am-maxSpeed/2, vYStart.getValue());
                    direction = new Vektor(startDirection);
                    break;
                case "vyStart":
                    startDirection = new Vektor(vXStart.getValue(), am-maxSpeed/2);
                    direction = new Vektor(startDirection);
                    break;
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        commands.get(e.getActionCommand()).func();
    }

    private ActionFunction startSim = () -> {
        running = true;
    };

    private ActionFunction stopSim = () -> {
        Vektor v = Vektor.toScreen(smallPos, HEIGHT, HEIGHT);
        xStart.setValue(v.xAsInt());
        yStart.setValue(v.yAsInt());
        running = false;
    };

    private ActionFunction resetSim = () -> {
        resetGame();
    };
}
