package de.luiskun.apps.sims;

import de.luiskun.apps.Application;
import de.luiskun.apps.sims.classes.GOL_Objects;
import de.luiskun.tools.FileHandler;
import de.luiskun.tools.Vektor;
import de.luiskun.ui.uiparts.RangePicker;
import de.luiskun.ui.uiparts.RangeSelection;

import java.io.File;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class GameOfLife extends Application implements MouseListener, ActionListener, RangeSelection {

    int[] gridsizes = {35, 50, 70, 100, 140, 175, 350};

    int gridSize;
    int cellSize;

    int speed = 1;
    int curPos = 0;

    boolean[][] currentGen;
    boolean[][] createGrid;

    JCheckBox createMode, borders;
    JButton saveCreation, resetField, loadField, resetCreator;
    RangePicker speedPicker, sizePicker;
    JTextField nameField;
    JComboBox customSelection, prebuiltSelection;
    JRadioButton customSel, preSel;
    ButtonGroup typeSel;
    JLabel sizeInfo;
    JButton shiftLeft, shiftRight, shiftUp, shiftDown;

    boolean creating, withBorders;
    boolean leftClicking, rightClicking;
    Vektor[] currentStarterGrid;
    Vektor[] currentCreationGrid;

    Vektor midPos;

    boolean changeGrid = false;
    boolean sizeChange = false;
    int nextAm;

    public GameOfLife() {
        this.addMouseListener(this);

        int x = HEIGHT + 10;
        int y = 10;

        JLabel gridLabel = new JLabel("<html><u>Grid Settings</u></html>");
        gridLabel.setBounds(x, y, 180, 20);
        gridLabel.setForeground(Color.WHITE);
        gridLabel.setFont(gridLabel.getFont().deriveFont(Font.BOLD, 20));
        this.add(gridLabel);
        y += 25;

        JLabel speedInfo = new JLabel("Speed");
        speedInfo.setBounds(x, y, 180, 15);
        speedInfo.setForeground(Color.WHITE);
        this.add(speedInfo);
        y += 20;

        speedPicker = new RangePicker(this, 1,  30, 1, "speed");
        speedPicker.setBounds(x, y, 180, 30);
        this.add(speedPicker);
        y += 35;

        sizeInfo = new JLabel("Gridsize: 50x50");
        sizeInfo.setBounds(x, y, 180, 15);
        sizeInfo.setForeground(Color.WHITE);
        this.add(sizeInfo);
        y += 20;

        sizePicker = new RangePicker(this, 0, 6, 2, "size");
        gridSize = gridsizes[2];
        nextAm = 2;
        sizePicker.setBounds(x, y, 180, 30);
        this.add(sizePicker);
        y += 35;

        borders = new JCheckBox("Borders");
        borders.setBounds(x, y, 180, 25);
        borders.addChangeListener(list -> {
            withBorders = borders.isSelected();
        });
        this.add(borders);
        y += 30;

        JLabel presetInfo = new JLabel("<html><u>Presets</u></html>");
        presetInfo.setBounds(x,y,180,15);
        presetInfo.setForeground(Color.WHITE);
        this.add(presetInfo);
        {
            y += 20;
            preSel = new JRadioButton("Prebuilt");
            preSel.setBounds(x,y,180,15);
            preSel.setSelected(true);
            this.add(preSel);

            y += 20;
            prebuiltSelection = new JComboBox(FileHandler.readGOLNAMES("pre"));
            prebuiltSelection.setBounds(x,y,180, 25);
            this.add(prebuiltSelection);

            y += 30;
            customSel = new JRadioButton("Custom");
            customSel.setBounds(x,y,180,15);
            this.add(customSel);

            typeSel = new ButtonGroup();
            typeSel.add(preSel);
            typeSel.add(customSel);

            y += 20;
            customSelection = new JComboBox(FileHandler.readGOLNAMES("custom"));
            customSelection.setBounds(x,y,180, 25);
            this.add(customSelection);

            y += 30;
            resetField = new JButton("Reset");
            resetField.setBounds(x, y, 180, 25);
            resetField.setActionCommand("reset");
            resetField.addActionListener(this);
            this.add(resetField);

            y += 30;
        }

        //shiftLeft, shiftRight, shiftUp, shiftDown
        JLabel shiftInfo = new JLabel("<html><u>Shift</u></html>");
        shiftInfo.setBounds(x,y,180,15);
        shiftInfo.setForeground(Color.WHITE);
        this.add(shiftInfo);
        y += 20;

        shiftUp = new JButton("▲");
        shiftUp.setBounds(x+60, y, 60, 25);
        shiftUp.setActionCommand("up");
        shiftUp.addActionListener(this);
        this.add(shiftUp);
        y += 25;

        shiftLeft = new JButton("◄");
        shiftLeft.setBounds(x, y, 60, 25);
        shiftLeft.setActionCommand("left");
        shiftLeft.addActionListener(this);
        this.add(shiftLeft);

        shiftRight = new JButton("►");
        shiftRight.setBounds(x+120, y, 60, 25);
        shiftRight.setActionCommand("right");
        shiftRight.addActionListener(this);
        this.add(shiftRight);
        y += 25;

        shiftDown = new JButton("▼");
        shiftDown.setBounds(x+60, y, 60, 25);
        shiftDown.setActionCommand("down");
        shiftDown.addActionListener(this);
        this.add(shiftDown);
        y += 55;

        JLabel createLabel = new JLabel("<html><u>Create Settings</u></html>");
        createLabel.setBounds(x, y, 180, 20);
        createLabel.setForeground(Color.WHITE);
        createLabel.setFont(createLabel.getFont().deriveFont(Font.BOLD, 20));
        this.add(createLabel);
        y += 25;

        createMode = new JCheckBox("Create Mode");
        createMode.setBounds(x, y, 180, 30);
        createMode.addChangeListener(list -> {
            creating = createMode.isSelected();
        });
        this.add(createMode);
        y += 35;

        JLabel nameInfo = new JLabel("Filename");
        nameInfo.setBounds(x, y, 180, 15);
        nameInfo.setForeground(Color.WHITE);
        this.add(nameInfo);
        y += 20;

        nameField = new JTextField();
        nameField.setBounds(x, y, 180, 25);
        this.add(nameField);
        y += 30;

        saveCreation = new JButton("Save");
        saveCreation.setBounds(x, y, 180, 25);
        saveCreation.addActionListener(this);
        saveCreation.setActionCommand("cr-save");
        this.add(saveCreation);
        y += 30;

        loadField = new JButton("Load");
        loadField.setBounds(x, y, 180, 25);
        loadField.addActionListener(this);
        loadField.setActionCommand("cr-load");
        this.add(loadField);
        y += 30;

        resetCreator = new JButton("Reset");
        resetCreator.setBounds(x, y, 180, 25);
        resetCreator.addActionListener(this);
        resetCreator.setActionCommand("cr-reset");
        this.add(resetCreator);

        midPos = new Vektor(gridSize / 2, gridSize / 2);

        changeGrid = true;
    }

    private void resetGrid() {
        cellSize = HEIGHT / gridSize;
        currentGen = new boolean[gridSize][gridSize];
        createGrid = new boolean[gridSize][gridSize];

        if (preSel.isSelected()) {
            currentStarterGrid = FileHandler.readGOLPreset(false, prebuiltSelection.getSelectedItem().toString());
        } else {
            currentStarterGrid = FileHandler.readGOLPreset(true, customSelection.getSelectedItem().toString());
        }

        if (currentCreationGrid != null) {
            for (Vektor v : currentCreationGrid) {
                createGrid[(v.xAsInt() + gridSize + gridSize/2) % gridSize][(v.yAsInt() + gridSize + gridSize/2) % gridSize] = true;
            }
        }

        if (currentStarterGrid != null) {

            for (Vektor v : currentStarterGrid) {
                currentGen[(v.xAsInt() + gridSize + midPos.xAsInt()) % gridSize][(v.yAsInt() + gridSize + midPos.yAsInt()) % gridSize] = true;
            }

        }
    }

    @Override
    public void update() {
        if (changeGrid) {
            changeGrid = false;
            gridSize = gridsizes[nextAm];
            sizeInfo.setText("Gridsize: " + gridSize + "x" + gridSize);

            if (sizeChange) {
                sizeChange = false;
                midPos = new Vektor(gridSize/2, gridSize/2);
            }

            resetGrid();
        }

        if (!creating) {
            curPos++;
            if (curPos > speed) {
                curPos = 0;
                nextGen();
            }
        } else {
            if (leftClicking || rightClicking) {
                Point mP = this.getMousePosition();
                if (mP != null) {
                    Vektor v = new Vektor(mP);
                    v.divide(cellSize);
                    if (v.xAsInt() < gridSize && v.yAsInt() < gridSize) {
                        if (leftClicking) {
                            createGrid[v.xAsInt()][v.yAsInt()] = true;
                        } else if (rightClicking){
                            createGrid[v.xAsInt()][v.yAsInt()] = false;
                        }
                    }
                }
            }
        }
    }

    private void nextGen() {
        boolean[][] nextGen = new boolean[gridSize][gridSize];
        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                nextGen[x][y] = isCellAlive(new Vektor(x,y));
            }
        }
        currentGen = nextGen;
    }

    private boolean isCellAlive(Vektor pos) {
        int am = getNeighborsAm(pos);
        return (currentGen[pos.xAsInt()][pos.yAsInt()] && (am == 2 || am == 3)) ||
                (!currentGen[pos.xAsInt()][pos.yAsInt()] && am == 3);
    }

    private int getNeighborsAm(Vektor pos) {
        int am = 0;

        int x = pos.xAsInt()-1;
        int y = pos.yAsInt()-1;
        if (withBorders) {
            if (x >= 0 && y >= 0) {
                if (currentGen.length == gridSize && currentGen[x][y]) am++;
            }
        } else {
            if (currentGen.length == gridSize && currentGen[(x+gridSize)%gridSize][(y+gridSize)%gridSize]) am++;
        }

        x = pos.xAsInt();
        y = pos.yAsInt()-1;
        if (withBorders && y >= 0) {
            if (x >= 0 && y >= 0) {
                if (currentGen.length == gridSize && currentGen[x][y]) am++;
            }
        } else {
            if (currentGen.length == gridSize && currentGen[(x+gridSize)%gridSize][(y+gridSize)%gridSize]) am++;
        }

        x = pos.xAsInt()+1;
        y = pos.yAsInt()-1;
        if (withBorders) {
            if (y >= 0 && x < gridSize) {
                if (currentGen.length == gridSize && currentGen[x][y]) am++;
            }        } else {
            if (currentGen.length == gridSize && currentGen[(x+gridSize)%gridSize][(y+gridSize)%gridSize]) am++;
        }

        x = pos.xAsInt()-1;
        y = pos.yAsInt();
        if (withBorders) {
            if (x >= 0) {
                if (currentGen.length == gridSize && currentGen[x][y]) am++;
            }        } else {
            if (currentGen.length == gridSize && currentGen[(x+gridSize)%gridSize][(y+gridSize)%gridSize]) am++;
        }

        x = pos.xAsInt()+1;
        y = pos.yAsInt();
        if (withBorders) {
            if (x < gridSize) {
                if (currentGen.length == gridSize && currentGen[x][y]) am++;
            }        } else {
            if (currentGen.length == gridSize && currentGen[(x+gridSize)%gridSize][(y+gridSize)%gridSize]) am++;
        }

        x = pos.xAsInt()-1;
        y = pos.yAsInt()+1;
        if (withBorders) {
            if (y < gridSize && x >= 0) {
                if (currentGen.length == gridSize && currentGen[x][y]) am++;
            }        } else {
            if (currentGen.length == gridSize && currentGen[(x+gridSize)%gridSize][(y+gridSize)%gridSize]) am++;
        }

        x = pos.xAsInt();
        y = pos.yAsInt()+1;
        if (withBorders) {
            if (y < gridSize) {
                if (currentGen.length == gridSize && currentGen[x][y]) am++;
            }        } else {
            if (currentGen.length == gridSize && currentGen[(x+gridSize)%gridSize][(y+gridSize)%gridSize]) am++;
        }

        x = pos.xAsInt()+1;
        y = pos.yAsInt()+1;
        if (withBorders) {
            if (y < gridSize && x < gridSize) {
                if (currentGen.length == gridSize && currentGen[x][y]) am++;
            }        } else {
            if (currentGen.length == gridSize && currentGen[(x+gridSize)%gridSize][(y+gridSize)%gridSize]) am++;
        }

        return am;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0,0,WIDTH, HEIGHT);

        if (!creating) {
            for (int y = 0; y < gridSize; y++) {
                for (int x = 0; x < gridSize; x++) {
                    if (x < currentGen.length && y < currentGen.length) {
                        if (currentGen[x][y]) {
                            g2.setColor(new Color(200, 30, 30));
                            g2.fillRect(x*cellSize, y*cellSize, cellSize, cellSize);
                        }
                        g2.setColor(Color.DARK_GRAY);
                        g2.drawRect(x*cellSize, y*cellSize, cellSize, cellSize);
                    }
                }
            }
        } else {
            for (int y = 0; y < gridSize; y++) {
                for (int x = 0; x < gridSize; x++) {
                    if (x < currentGen.length && y < currentGen.length) {
                        if (createGrid[x][y]) {
                            g2.setColor(new Color(200, 30, 30));
                            g2.fillRect(x*cellSize, y*cellSize, cellSize, cellSize);
                        }
                        g2.setColor(Color.DARK_GRAY);
                        g2.drawRect(x*cellSize, y*cellSize, cellSize, cellSize);
                    }
                }
            }
            g2.setColor(Color.GREEN);
            g2.drawRect(gridSize/2*cellSize, gridSize/2*cellSize, cellSize, cellSize);

        }

        g2.setColor(Color.WHITE);
        g2.fillRect(HEIGHT, 435, 200, 2);
    }

    private void saveCreation() {
        List<Vektor> vs = new ArrayList();
        Vektor midPoint = new Vektor(gridSize/2, gridSize/2);

        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                if (createGrid[x][y]) {
                    Vektor v = new Vektor(x, y);
                    v.sub(midPoint);
                    vs.add(v);
                }
            }
        }

        FileHandler.saveGOLPreset(vs, nameField.getText());

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "cr-save":
                String name = nameField.getText();

                if (name.length() == 0) {
                    JOptionPane.showMessageDialog(this, "Name is empty","ERROR", JOptionPane.ERROR_MESSAGE);
                    break;
                }

                if (FileHandler.isGOLNameUsed(name)) {
                    int reply = JOptionPane.showConfirmDialog(this, "Name already used. Overwrite?", "", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        saveCreation();
                    }
                    break;
                }

                saveCreation();
                break;
            case "cr-load":
                if (!FileHandler.isGOLNameUsed(nameField.getText())) {
                    JOptionPane.showMessageDialog(this, "Preset not found","ERROR", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                currentCreationGrid = FileHandler.readGOLPreset(true, nameField.getText() + ".txt");
                changeGrid = true;
                break;
            case "cr-reset":
                for (int y = 0; y < gridSize; y++) {
                    for (int x = 0; x < gridSize; x++) {
                        createGrid[x][y] = false;
                    }
                }
                break;
            case "reset":
                changeGrid = true;
                break;
            case "up":
                if (midPos.yAsInt() > 0) {
                    midPos.add(new Vektor(0, -1));
                    changeGrid = true;
                }
                break;
            case "down":
                if (midPos.yAsInt() < gridSize-1) {
                    midPos.add(new Vektor(0, 1));
                    changeGrid = true;
                }
                break;
            case "left":
                if (midPos.xAsInt() > 0) {
                    midPos.add(new Vektor(-1, 0));
                    changeGrid = true;
                }
                break;
            case "right":
                if (midPos.yAsInt() < gridSize-1) {
                    midPos.add(new Vektor(1, 0));
                    changeGrid = true;
                }
                break;
        }
    }

    @Override
    public void rangeChanged(String com, int am) {
        switch (com) {
            case "speed":
                speed = am;
                break;
            case "size":
                nextAm = am;
                sizeChange = true;
                changeGrid = true;
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                leftClicking = true;
                break;
            case MouseEvent.BUTTON3:
                rightClicking = true;
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                leftClicking = false;
                break;
            case MouseEvent.BUTTON3:
                rightClicking = false;
                break;
        }    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
