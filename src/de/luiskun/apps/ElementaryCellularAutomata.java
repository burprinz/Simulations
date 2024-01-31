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

    private JButton hundredEightyTwo;

    private JSlider dotSizeSlider;

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

        String[] presets = {
                "13",
                "45",
                "62",
                "73",
                "124",
                "179",
                "182",
                "193",
                "214",
                "225",
                "255"
        };

        int x = 5;
        int y = 145;

        for(int i = 0; i < presets.length; i++) {
            JButton button = new JButton(presets[i]);
            button.setBounds(x, y, 150, 30);
            button.setActionCommand(presets[i]);
            button.addActionListener(bh);
            this.add(button);
            y += 40;
        }

        JLabel sizeInfo = new JLabel("Dot Size");
        sizeInfo.setBounds(x, y, 150, 15);
        this.add(sizeInfo);

        y += 15;

        dotSizeSlider = new JSlider();
        dotSizeSlider.setBounds(x, y, 150, 40);
        dotSizeSlider.setValue(cellSize);
        dotSizeSlider.setMinimum(1);
        dotSizeSlider.setMaximum(15);
        dotSizeSlider.setMinorTickSpacing(1);
        dotSizeSlider.setMajorTickSpacing(2);
        dotSizeSlider.setPaintLabels(true);
        dotSizeSlider.setPaintTicks(true);
        dotSizeSlider.addChangeListener(listener -> {
            int val = dotSizeSlider.getValue();
            this.cellSize = val;
            genSize = HEIGHT / cellSize;
            initNewGen();
            sizeInfo.setText("Dot Size: " + val);
        });
        this.add(dotSizeSlider);
    }

    private  void changeRuleset(int num) {
        currentRuleset = Integer.toString(num, 2);
        while (currentRuleset.length() < 8) {
            currentRuleset = "0" + currentRuleset;
        }

        initNewGen();
    }

    private void initNewGen() {
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
        g2.drawString("Z = Back to menu", 5, HEIGHT - 25);

        if (generations.size() != 0 && genSize == generations.get(0).length) {
            for (int y_ = 0; y_ < generations.size(); y_++) {
                int[] cells = generations.get(y_);
                for (int i = 0; i < genSize; i++) {
                    if (i < cells.length && cells[i] == 1) {
                        // Black
                        g2.fillRect(200 + i*cellSize,y_ * cellSize,cellSize,cellSize);
                    }
                }
            }
        }
    }

    public void update() {

        if (generations.size() != 0 && genSize != generations.get(0).length) {
            generations.clear();
        } else {
            if(generations.size() < genSize) {

                int[] cells = generations.get(generations.size()-1);
                int[] nextGen = new int[genSize];

                for(int i = 0; i < genSize; i++) {
                    if (i < 0 || i >= genSize) {
                        nextGen[i] = cells[i];
                        continue;
                    }

                    int left = i-1<0?0:cells[i-1];
                    int right = i+1>=genSize?0:cells[i+1];
                    int state = cells[i];
                    int newState = calculateState(left, state, right);
                    nextGen[i] = newState;
                }

                generations.add(nextGen);
            }
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
                default:
                    int num = Integer.parseInt(e.getActionCommand());
                    ruleSpinner.setValue(num);
                    changeRuleset(num);
            }
        }
    }
}
