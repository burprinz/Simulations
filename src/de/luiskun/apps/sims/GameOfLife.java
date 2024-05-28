package de.luiskun.apps.sims;

import de.luiskun.apps.Application;
import de.luiskun.apps.sims.classes.GOL_Objects;
import de.luiskun.tools.Vektor;
import de.luiskun.ui.uiparts.RangePicker;
import de.luiskun.ui.uiparts.RangeSelection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameOfLife extends Application implements MouseListener, ActionListener, RangeSelection {

    int gridSize = 70;
    int createSize = 35;
    int cellSize;
    int createCellSize;

    int speed = 1;
    int curPos = 0;

    boolean[][] currentGen;
    boolean[][] createGrid;

    JCheckBox createMode;
    JButton saveCreation;
    RangePicker speedPicker;

    boolean creating;
    boolean leftClicking, rightClicking;

    public GameOfLife() {
        this.addMouseListener(this);

        resetGrid();

        int x = HEIGHT + 10;
        int y = 10;

        speedPicker = new RangePicker(this, 1,  30, 10, "speed");
        speedPicker.setBounds(x, y, 180, 30);
        this.add(speedPicker);

        y += 35;

        createMode = new JCheckBox("Create Mode");
        createMode.setBounds(x, y, 180, 30);
        createMode.addChangeListener(list -> {
            creating = createMode.isSelected();
        });
        this.add(createMode);

        y += 35;
        saveCreation = new JButton();
        saveCreation.setBounds(x, y, 180, 25);
        saveCreation.addActionListener(this);
        saveCreation.setActionCommand("cr-save");
        this.add(saveCreation);
    }

    private void resetGrid() {
        cellSize = HEIGHT / gridSize;
        createCellSize = HEIGHT / createSize;
        currentGen = new boolean[gridSize][gridSize];
        createGrid = new boolean[gridSize][gridSize];

        currentGen = GOL_Objects.addCribbageToGrid(currentGen, new Vektor(gridSize/2, gridSize/2));
    }

    @Override
    public void update() {
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
                    v.divide(createCellSize);
                    if (v.xAsInt() < createSize && v.yAsInt() < createSize) {
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
        if (currentGen[(x+gridSize)%gridSize][(y+gridSize)%gridSize]) am++;

        x = pos.xAsInt();
        y = pos.yAsInt()-1;
        if (currentGen[(x+gridSize)%gridSize][(y+gridSize)%gridSize]) am++;

        x = pos.xAsInt()+1;
        y = pos.yAsInt()-1;
        if (currentGen[(x+gridSize)%gridSize][(y+gridSize)%gridSize]) am++;

        x = pos.xAsInt()-1;
        y = pos.yAsInt();
        if (currentGen[(x+gridSize)%gridSize][(y+gridSize)%gridSize]) am++;

        x = pos.xAsInt()+1;
        y = pos.yAsInt();
        if (currentGen[(x+gridSize)%gridSize][(y+gridSize)%gridSize]) am++;

        x = pos.xAsInt()-1;
        y = pos.yAsInt()+1;
        if (currentGen[(x+gridSize)%gridSize][(y+gridSize)%gridSize]) am++;

        x = pos.xAsInt();
        y = pos.yAsInt()+1;
        if (currentGen[(x+gridSize)%gridSize][(y+gridSize)%gridSize]) am++;

        x = pos.xAsInt()+1;
        y = pos.yAsInt()+1;
        if (currentGen[(x+gridSize)%gridSize][(y+gridSize)%gridSize]) am++;

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
                    if (currentGen[x][y]) {
                        g2.setColor(new Color(200, 30, 30));
                        g2.fillRect(x*cellSize, y*cellSize, cellSize, cellSize);
                    }
                    g2.setColor(Color.DARK_GRAY);
                    g2.drawRect(x*cellSize, y*cellSize, cellSize, cellSize);
                }
            }
        } else {
            for (int y = 0; y < createSize; y++) {
                for (int x = 0; x < createSize; x++) {
                    if (createGrid[x][y]) {
                        g2.setColor(new Color(200, 30, 30));
                        g2.fillRect(x*createCellSize, y*createCellSize, createCellSize, createCellSize);
                    }
                    g2.setColor(Color.DARK_GRAY);
                    g2.drawRect(x*createCellSize, y*createCellSize, createCellSize, createCellSize);
                }
            }
            g2.setColor(Color.GREEN);
            g2.drawRect(createSize/2*createCellSize, createSize/2*createCellSize, createCellSize, createCellSize);

        }
    }

    private void printCreation() {
        Vektor midPoint = new Vektor(createSize/2, createSize/2);

        for (int y = 0; y < createSize; y++) {
            for (int x = 0; x < createSize; x++) {
                if (createGrid[x][y]) {
                    Vektor v = new Vektor(x, y);
                    v.sub(midPoint);
                    System.out.println("new Vektor(" + v.xAsInt() + ", " + v.yAsInt() + "),");
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "cr-save":
                printCreation();
                break;
        }
    }

    @Override
    public void rangeChanged(String com, int am) {
        switch (com) {
            case "speed":
                speed = am;
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
