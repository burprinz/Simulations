package de.luiskun.apps;

import de.luiskun.Konstanten;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;


public class ElementaryCellularAutomata extends Application {

    public static final int WIDTH = 900;
    public static final int HEIGHT = 700;


    // Cells and Generation
    private int cellSize = 2;
    private int genSize = HEIGHT / cellSize;
    private List<int[]> generations;
    private boolean isInverted;

    // Current Number
    private String currentRuleset;

    // Swing
    private JSpinner ruleSpinner;
    private JButton restartB, invertButton;

    // Listener
    private ButtonHandler bh = new ButtonHandler();

    public ElementaryCellularAutomata() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDoubleBuffered(true);
        this.setLayout(null);
        generations = new ArrayList<>();
        changeRuleset(0);

        JLabel ruleInfo = new JLabel("Current Rule");
        ruleInfo.setBounds(5, 5, 150, 25);
        this.add(ruleInfo);

        ruleSpinner = new JSpinner();
        ruleSpinner.setBounds(5,35,150,30);
        this.add(ruleSpinner);

        restartB = new JButton("Repaint");
        restartB.setBounds(5, 70, 150, 30);
        restartB.setActionCommand("restart");
        restartB.addActionListener(bh);
        this.add(restartB);

        invertButton = new JButton("Invert [" + Konstanten.UNCHECKMARK + "]");
        invertButton.setBounds(5, 105, 150, 30);
        invertButton.setActionCommand("invert");
        invertButton.addActionListener(bh);
        this.add(invertButton);

    }

    private  void changeRuleset(int num) {
        currentRuleset = Integer.toString(num, 2);
        while (currentRuleset.length() < 8) {
            currentRuleset = "0" + currentRuleset;
        }

        generations.clear();

        int[] cells = new int[genSize];
        for(int i = 0; i < genSize; i++) {
            cells[i] = 0;
        }
        cells[genSize/2] = 1;
        generations.add(cells);
    }

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0,0,WIDTH,HEIGHT);

        g2.setColor(Color.BLACK);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 13));
        g2.drawString("BACKSPACE = Back to menu", 5, HEIGHT - 25);

        for (int y_ = 0; y_ < generations.size(); y_++) {
            int[] cells = generations.get(y_);
            for (int i = 0; i < genSize; i++) {
                if (cells[i] == 1) {
                    // Black
                    g2.fillRect(200 + i*cellSize,y_ * cellSize,cellSize,cellSize);
                }
            }
        }
    }

    public void update() {

            if(generations.size() < genSize) {

                int[] cells = generations.get(generations.size()-1);
                int[] nextGen = new int[genSize];

                for(int i = 0; i < genSize; i++) {
                    int left = i-1<0?0:cells[i-1];
                    int right = i+1>=genSize?0:cells[i+1];
                    int state = cells[i];
                    int newState = calculateState(left, state, right);
                    nextGen[i] = newState;
                }

                generations.add(nextGen);
            }
    }

    private int calculateState(int left, int state, int right) {
        String in = left + "" + state + "" + right;
        int value = Integer.parseInt(in, 2);
        if(!isInverted) {
            value = 7 - value;
        }
        return Integer.parseInt(String.valueOf(currentRuleset.charAt(value)));
    }


    private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int val = 0;
            switch (e.getActionCommand()) {
                case "restart":
                    val = Integer.parseInt(ruleSpinner.getValue().toString());
                    if (val < 0) {
                        val = 0;
                        ruleSpinner.setValue(0);
                    }
                    if (val >= 256) {
                        val = 255;
                        ruleSpinner.setValue(255);
                    }
                    changeRuleset(val);
                    break;
                case "invert":
                    isInverted = !isInverted;
                    if(isInverted) {
                        invertButton.setText("Invert [" + Konstanten.CHECKMARK + "]");
                    } else {
                        invertButton.setText("Invert [" + Konstanten.UNCHECKMARK + "]");
                    }
                    val = Integer.parseInt(ruleSpinner.getValue().toString());
                    if (val < 0) {
                        val = 0;
                        ruleSpinner.setValue(0);
                    }
                    if (val >= 256) {
                        val = 255;
                        ruleSpinner.setValue(255);
                    }
                    changeRuleset(val);
                    break;
            }
        }
    }
}
