package de.luiskun.apps.sims;

import de.luiskun.apps.Application;
import de.luiskun.tools.FileHandler;
import de.luiskun.apps.sims.classes.Dotinfo;
import de.luiskun.ui.uiparts.ColorPicker;
import de.luiskun.ui.uiparts.RangePicker;
import de.luiskun.ui.uiparts.RangeSelection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Phyllotaxis extends Application implements RangeSelection {

    int n;
    int c = 2;
    int dotR = 8;

    double angle = 137.5;

    int dotsPerStep = 1;
    boolean withColor = false;

    List<Dotinfo> dots;

    ButtonHandler bh = new ButtonHandler();

    RangePicker dotRadiusS, expSpeed, dotStep;
    JCheckBox colorAllowed;
    ColorPicker colors;
    JLabel expSpeInfo, dotRInfo, dotStepInfo;

    boolean isRunning = false;

    public Phyllotaxis() {
        super();

        dots = new ArrayList<>();

        int x = HEIGHT + 10;
        int y = 10;

        dotRInfo = new JLabel("Dot Radius: " + dotR);
        dotRInfo.setBounds(x, y, 150, 15);
        dotRInfo.setForeground(Color.WHITE);
        this.add(dotRInfo);

        y += 20;

        dotRadiusS = new RangePicker(this, 1, 20, dotR, "dR");
        dotRadiusS.setBounds(x, y, 170, 25);
        this.add(dotRadiusS);

        y += 30;

        expSpeInfo = new JLabel("Expansion speed: " + c);
        expSpeInfo.setBounds(x, y, 150, 15);
        expSpeInfo.setForeground(Color.WHITE);
        this.add(expSpeInfo);

        y += 20;

        expSpeed = new RangePicker(this, 1, 20, c, "eS");
        expSpeed.setBounds(x, y, 170, 25);
        this.add(expSpeed);

        y += 30;
        dotStepInfo = new JLabel("Calculations per step: " + dotsPerStep);
        dotStepInfo.setBounds(x, y, 150, 15);
        dotStepInfo.setForeground(Color.WHITE);
        this.add(dotStepInfo);

        y += 20;

        dotStep = new RangePicker(this, 0, 100, dotsPerStep, "dS");
        dotStep.setBounds(x, y, 170, 25);
        this.add(dotStep);

        y += 30;

        colorAllowed = new JCheckBox("Colors");
        colorAllowed.setBounds(x, y, 150, 25);
        colorAllowed.addActionListener(listener -> {
            withColor = colorAllowed.isSelected();
        });
        this.add(colorAllowed);

        y += 30;

        colors = new ColorPicker(180, false);
        colors.setBounds(x, y, 180, 50);
        this.add(colors);

        y += 55;

        String[] presets = {
                "Start",
                "Stop",
                "Reset",
                "137.3",
                "137.5",
                "137.6"
        };


        for(int i = 0; i < presets.length; i++) {
            JButton button = new JButton(presets[i]);
            button.setBounds(x, y, 150, 30);
            button.setActionCommand(presets[i]);
            button.addActionListener(bh);
            this.add(button);
            y += 40;
        }

    }

    private void reset() {
        n = 0;
        dots.clear();
    }

    @Override
    public void update() {
        if (isRunning) {
            for(int i = 0; i < dotsPerStep; i++) {
                double a = n * angle;
                double r = c * Math.sqrt(n);

                if(r > HEIGHT) {
                    break;
                }

                if (r < HEIGHT) {
                    dots.add(new Dotinfo((int) (r * Math.cos(a) + HEIGHT/2), (int) (r * Math.sin(a) + HEIGHT/2), r));
                }
                n++;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);
        g2.setColor(Color.WHITE);

        for(int i = 0; i < dots.size(); i++) {
            if (withColor && colors.cur != null)  {
                int colorpos = (int)(((double)colors.cur.length / (double) (HEIGHT/2)) * dots.get(i).r);
                colorpos = colorpos<colors.cur.length?colorpos:colors.cur.length-1;
                g2.setColor(colors.cur[colorpos]);
            }

            g2.fillOval(dots.get(i).x-dotR, dots.get(i).y-dotR, 2*dotR, 2*dotR);
        }

        g2.setColor(Color.BLACK);
        g2.fillRect(HEIGHT, 0,  200, HEIGHT);

    }

    @Override
    public void rangeChanged(String com, int am) {
        switch (com) {
            case "dR":
                dotR = dotRadiusS.getValue();
                dotRInfo.setText("Dot Radius: " + dotR);
                break;
            case "eS":
                c = expSpeed.getValue();
                expSpeInfo.setText("Expansion speed: " + c);
                reset();
                break;
            case "dS":
                dotsPerStep = dotStep.getValue();
                dotStepInfo.setText("Calculations per step: " + dotsPerStep);
                break;
        }
    }

    private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "Start":
                    isRunning = true;
                    break;
                case "Stop":
                    isRunning = false;
                    break;
                case "Reset":
                    isRunning = false;
                    dots.clear();
                    break;
                default:
                    angle = Double.parseDouble(e.getActionCommand());
                    reset();
                    break;
            }
        }
    }
}
