package de.luiskun.apps;

import de.luiskun.FileHandler;
import de.luiskun.Konstanten;
import de.luiskun.Vektor;
import de.luiskun.apps.classes.Dotinfo;
import de.luiskun.menu.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Phyllotaxis extends Application{

    int n;
    int c = 2;
    int dotR = 8;

    double angle = 137.5;

    int dotsPerStep = 1;
    boolean withColor = false;

    Color[] currentPalette;
    List<Dotinfo> dots;

    ButtonHandler bh = new ButtonHandler();

    JSlider dotRadiusS, expSpeed, dotStep;

    JCheckBox colorAllowed;
    JComboBox selectedPalette;

    boolean isRunning = false;

    public Phyllotaxis() {
        super();

        dots = new ArrayList<>();

        int x = HEIGHT + 10;
        int y = 10;

        JLabel dotRInfo = new JLabel("Dot Radius: " + dotR);
        dotRInfo.setBounds(x, y, 150, 15);
        dotRInfo.setForeground(Color.WHITE);
        this.add(dotRInfo);

        y += 20;

        dotRadiusS = new JSlider();
        dotRadiusS.setBounds(x, y, 170, 25);
        dotRadiusS.setBackground(Color.BLACK);
        dotRadiusS.setMinimum(1);
        dotRadiusS.setMaximum(20);
        dotRadiusS.setValue(dotR);
        dotRadiusS.setBackground(Color.BLACK);
        dotRadiusS.addChangeListener(listener -> {
            dotR = dotRadiusS.getValue();
            dotRInfo.setText("Dot Radius: " + dotR);
        });
        this.add(dotRadiusS);

        y += 30;

        JLabel expSpeInfo = new JLabel("Expansion speed: " + c);
        expSpeInfo.setBounds(x, y, 150, 15);
        expSpeInfo.setForeground(Color.WHITE);
        this.add(expSpeInfo);

        y += 20;

        expSpeed = new JSlider();
        expSpeed.setBounds(x, y, 170, 25);
        expSpeed.setBackground(Color.BLACK);
        expSpeed.setMinimum(1);
        expSpeed.setMaximum(20);
        expSpeed.setValue(c);
        expSpeed.setBackground(Color.BLACK);
        expSpeed.addChangeListener(listener -> {
            c = expSpeed.getValue();
            expSpeInfo.setText("Expansion speed: " + c);
            reset();
        });
        this.add(expSpeed);

        y += 30;
        JLabel dotStepInfo = new JLabel("Calculations per step: " + dotsPerStep);
        dotStepInfo.setBounds(x, y, 150, 15);
        dotStepInfo.setForeground(Color.WHITE);
        this.add(dotStepInfo);

        y += 20;

        dotStep = new JSlider();
        dotStep.setBounds(x, y, 170, 25);
        dotStep.setBackground(Color.BLACK);
        dotStep.setMinimum(0);
        dotStep.setMaximum(100);
        dotStep.setValue(dotsPerStep);
        dotStep.setBackground(Color.BLACK);
        dotStep.addChangeListener(listener -> {
            dotsPerStep = dotStep.getValue();
            dotStepInfo.setText("Calculations per step: " + dotsPerStep);
        });
        this.add(dotStep);

        y += 30;

        colorAllowed = new JCheckBox("Colors");
        colorAllowed.setBounds(x, y, 150, 25);
        colorAllowed.addActionListener(listener -> {
            withColor = colorAllowed.isSelected();
        });
        this.add(colorAllowed);

        y += 30;

        JLabel colorinfo = new JLabel("Color Palette");
        colorinfo.setBounds(x, y, 150, 15);
        colorinfo.setForeground(Color.WHITE);
        this.add(colorinfo);

        y += 20;

        String[] palletes = FileHandler.getPaletteNames();
        selectedPalette = new JComboBox(palletes);
        selectedPalette.setBounds(x, y, 180, 30);
        selectedPalette.addActionListener(listener -> {
            currentPalette = FileHandler.loadPalette(selectedPalette.getSelectedItem().toString());
        });
        currentPalette = FileHandler.loadPalette(selectedPalette.getSelectedItem().toString());
        this.add(selectedPalette);

        y += 35;

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
            if (withColor && currentPalette != null)  {
                int colorpos = (int)(((double)currentPalette.length / (double) (HEIGHT/2)) * dots.get(i).r);
                colorpos = colorpos<currentPalette.length?colorpos:currentPalette.length-1;
                g2.setColor(currentPalette[colorpos]);
            }

            g2.fillOval(dots.get(i).x-dotR, dots.get(i).y-dotR, 2*dotR, 2*dotR);
        }

        g2.setColor(Color.BLACK);
        g2.fillRect(HEIGHT, 0,  200, HEIGHT);

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
