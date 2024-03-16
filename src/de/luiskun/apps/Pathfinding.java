package de.luiskun.apps;

import de.luiskun.FileHandler;
import de.luiskun.Vektor;
import de.luiskun.apps.classes.MazeTile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Pathfinding extends Application {

    int[] sizes = new int[] {5, 7, 10, 14, 20, 25, 28, 35, 50, 70, 100, 140};
    int size = sizes[5];
    MazeTile[][] maze;

    ButtonHandler bh = new ButtonHandler();

    boolean isCreating, isSearching;

    List<MazeTile> buffer;
    List<Vektor> solution;

    int amountSeen;
    int speed = 10;
    boolean mazeFinished = false;

    JSlider mazeSizeSlider, speedSlider;
    JComboBox search;
    JButton startGenerating, searchB, oneStep;

    public Pathfinding() {
        super();

        buffer = new ArrayList<>();
        solution = new ArrayList<>();

        int x = HEIGHT + 5;
        int y = 5;

        JLabel mazeInfo = new JLabel("Size: " + size);
        mazeInfo.setBounds(x,y,190,20);
        mazeInfo.setForeground(Color.WHITE);
        this.add(mazeInfo);

        y += 25;

        mazeSizeSlider = new JSlider();
        mazeSizeSlider.setBounds(x, y, 190, 25);
        mazeSizeSlider.setMinimum(0);
        mazeSizeSlider.setMaximum(11);
        mazeSizeSlider.setValue(5);
        mazeSizeSlider.setBackground(Color.BLACK);
        mazeSizeSlider.addChangeListener(listener -> {
            size = sizes[mazeSizeSlider.getValue()];
            mazeInfo.setText("Size: " + size);
            generateEmptyMaze();
        });
        this.add(mazeSizeSlider);

        y += 30;

        startGenerating = new JButton("Start Generating");
        startGenerating.setBounds(x, y, 190, 25);
        startGenerating.setActionCommand("gen");
        startGenerating.addActionListener(bh);
        this.add(startGenerating);

        y += 30;

        JLabel speedInfo = new JLabel("Speed: " + speed);
        speedInfo.setBounds(x,y,190,20);
        speedInfo.setForeground(Color.WHITE);
        this.add(speedInfo);

        y += 25;

        speedSlider = new JSlider();
        speedSlider.setBounds(x, y, 190, 25);
        speedSlider.setMinimum(1);
        speedSlider.setMaximum(100);
        speedSlider.setValue(speed);
        speedSlider.setBackground(Color.BLACK);
        speedSlider.addChangeListener(listener -> {
            speed = speedSlider.getValue();
            speedInfo.setText("Speed: " + speed);
        });
        this.add(speedSlider);

        y += 30;

        String[] palletes = new String[] {"Greedy", "A*"};
        search = new JComboBox(palletes);
        search.setBounds(x, y, 180, 30);
        this.add(search);

        y += 35;

        searchB = new JButton("Start Searching");
        searchB.setBounds(x, y, 190, 25);
        searchB.setActionCommand("sea");
        searchB.addActionListener(bh);
        this.add(searchB);

        y += 30;

        oneStep = new JButton("Step");
        oneStep.setBounds(x, y, 190, 25);
        oneStep.setActionCommand("ste");
        oneStep.addActionListener(bh);
        this.add(oneStep);

    }


    @Override
    public void update() {
        if (isCreating) {
            for (int i = 0; i < speed; i++) {
                if (amountSeen < size*size) {
                    boolean found = searchForNextPos();
                    if (!found) {
                        buffer.remove(buffer.size()-1);
                    } else {
                        amountSeen++;
                    }
                } else {
                    isCreating = false;
                    mazeFinished = true;
                    startStepSearching();
                    break;
                }
            }
        } else if (isSearching) {
            searchStep();
        }
    }

    private void greedyStep() {

        Vektor curPos = solution.get(solution.size()-1);
        MazeTile cur = maze[curPos.xAsInt()][curPos.yAsInt()];

        maze[cur.x][cur.y].isVisited = true;

        if (cur.rightOpen && !maze[cur.x+1][cur.y].isVisited) {
            solution.add(new Vektor(cur.x+1, cur.y));
        } else if (cur.upOpen && !maze[cur.x][cur.y-1].isVisited) {
            solution.add(new Vektor(cur.x, cur.y-1));
        } else if (cur.leftOpen && !maze[cur.x-1][cur.y].isVisited) {
            solution.add(new Vektor(cur.x-1, cur.y));
        } else if (cur.downOpen && !maze[cur.x][cur.y+1].isVisited) {
            solution.add(new Vektor(cur.x, cur.y+1));
        } else {
            solution.remove(solution.size() - 1);
        }
    }

    private void aStarStep() {

        Vektor pos = solution.get(solution.size()-1);
        MazeTile cur = maze[pos.xAsInt()][pos.yAsInt()];
        maze[pos.xAsInt()][pos.yAsInt()].isVisited = true;

        double[] hVals = new double[4];
        int count = 0;

        if (cur.rightOpen && !maze[cur.x+1][cur.y].isVisited) {
            hVals[0] = hFunction(new Vektor(cur.x+1, cur.y));
            count++;
        } else {
            hVals[0] = -1;
        }
        if (cur.upOpen && !maze[cur.x][cur.y-1].isVisited) {
            hVals[1] = hFunction(new Vektor(cur.x, cur.y-1));
            count++;
        }else {
            hVals[1] = -1;
        }
        if (cur.leftOpen && !maze[cur.x-1][cur.y].isVisited) {
            hVals[2] = hFunction(new Vektor(cur.x-1, cur.y));
            count++;
        } else {
            hVals[2] = -1;
        }
        if (cur.downOpen && !maze[cur.x][cur.y+1].isVisited) {
            hVals[3] = hFunction(new Vektor(cur.x, cur.y+1));
            count++;
        } else {
            hVals[3] = -1;
        }

        System.out.println(hVals[0] + " " + hVals[1] + " " + hVals[2] + " " + hVals[3]);

        if (count > 0) {
            int bestH = getBestH(hVals);
            switch (bestH) {
                case 0:
                    solution.add(new Vektor(cur.x+1, cur.y));
                    System.out.println("Rechts");
                    break;
                case 1:
                    solution.add(new Vektor(cur.x, cur.y-1));
                    System.out.println("Oben");
                    break;
                case 2:
                    solution.add(new Vektor(cur.x-1, cur.y));
                    System.out.println("Links");
                    break;
                case 3:
                    solution.add(new Vektor(cur.x, cur.y+1));
                    System.out.println("Unten");
                    break;
            }
        } else {
            solution.remove(solution.size() - 1);
        }

    }

    private double hFunction(Vektor pos) {
        final Vektor finish = new Vektor(size-1, size-1);
        return Vektor.dist(pos, finish);
    }

    private int getBestH(double[] vals) {

        int bestH = -1;

        for(int i = 0; i < vals.length; i++) {
            if (bestH == -1) {
                if (vals[i] != -1) {
                    bestH = i;
                }
            } else {
                if (vals[i] != -1 && vals[i] < vals[bestH]) {
                    bestH = i;
                }
            }
        }

        return bestH;
    }

    private boolean searchForNextPos() {
        MazeTile cur = buffer.get(buffer.size()-1);
        cur.isSeen = true;

        int cx = cur.x;
        int cy = cur.y;

        List<Vektor> possible = new ArrayList<>();

        // Links
        if (cx - 1 >= 0 && !maze[cx - 1][cy].isSeen) {
            possible.add(new Vektor(cx-1, cy));
        }
        // Rechts
        if (cx + 1 < size && !maze[cx + 1][cy].isSeen) {
            possible.add(new Vektor(cx+1, cy));
        }
        // Oben
        if (cy - 1 >= 0 && !maze[cx][cy - 1].isSeen) {
            possible.add(new Vektor(cx, cy-1));
        }
        // Unten
        if (cy + 1 < size && !maze[cx ][cy + 1].isSeen) {
            possible.add(new Vektor(cx, cy+1));
        }

        if (possible.size() == 0) {
            // Zurueck bis Luecke und dann nochmal testen
            //rollback();
            //searchForNextPos();
            return false;
        }

        Random r = new Random();
        Vektor next = possible.get(r.nextInt(possible.size()));

        maze[next.xAsInt()][next.yAsInt()].isSeen = true;
        maze[next.xAsInt()][next.yAsInt()].x = next.xAsInt();
        maze[next.xAsInt()][next.yAsInt()].y = next.yAsInt();
        switch (getDir(new Vektor(cur.x, cur.y), next)) {
            case 0:
                maze[next.xAsInt()][next.yAsInt()].rightOpen = true;
                cur.leftOpen = true;
                break;
            case 1:
                maze[next.xAsInt()][next.yAsInt()].leftOpen = true;
                cur.rightOpen = true;
                break;
            case 2:
                maze[next.xAsInt()][next.yAsInt()].downOpen = true;
                cur.upOpen = true;
                break;
            case 3:
                maze[next.xAsInt()][next.yAsInt()].upOpen = true;
                cur.downOpen = true;
                break;
        }

        buffer.add(maze[next.xAsInt()][next.yAsInt()]);

        return true;
    }

    private int getDir(Vektor ol, Vektor ne) {
        if (ne.x < ol.x) {
            // Links
            return 0;
        }
        if (ol.x < ne.x) {
            // Rechts
            return 1;
        }
        if (ne.y < ol.y) {
            // Links
            return 2;
        }
        if (ol.y < ne.y) {
            // Rechts
            return 3;
        }
        return 4;
    }

    @Override
    public void paintComponent(Graphics g) {
        final int cellSize = HEIGHT / size;

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        if (maze != null) {
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    MazeTile t = maze[x][y];

                    if (buffer.size() > 0) {

                        if (buffer.get(buffer.size()-1) != null && x == buffer.get(buffer.size()-1).x && y == buffer.get(buffer.size()-1).y) {
                            g2.setColor(new Color(167, 199, 231));
                            g2.fillRect(x*cellSize, y*cellSize, cellSize, cellSize);
                        }
                    }
                    if (t.start) {
                        g2.setColor(new Color(184,216,190));
                        g2.fillRect(x*cellSize, y*cellSize, cellSize, cellSize);
                    }
                    if (t.finish) {
                        g2.setColor(new Color(255, 87, 51));
                        g2.fillRect(x*cellSize, y*cellSize, cellSize, cellSize);
                    }

                    g2.setColor(Color.LIGHT_GRAY);

                    int xPos = x*cellSize;
                    int yPos = y*cellSize;

                    if (!t.upOpen) {
                        g2.drawLine(xPos, yPos, xPos + cellSize, yPos);
                    }
                    if (!t.downOpen) {
                        g2.drawLine(xPos, yPos + cellSize, xPos + cellSize, yPos + cellSize);
                    }
                    if (!t.leftOpen) {
                        g2.drawLine(xPos, yPos , xPos, yPos + cellSize);
                    }
                    if (!t.rightOpen) {
                        g2.drawLine(xPos + cellSize, yPos , xPos + cellSize, yPos + cellSize);
                    }
                }
            }

            if (!solution.isEmpty()) {
                g2.setColor(Color.RED);
                for (int i = 1; i < solution.size(); i++) {
                    Vektor cur = solution.get(i);
                    Vektor pre = solution.get(i-1);

                    if (cur.x < pre.x) {
                        // Links
                        g2.drawLine(cur.xAsInt() * cellSize + (cellSize/2), cur.yAsInt()*cellSize + (cellSize/2), (cur.xAsInt()+1) * cellSize + (cellSize/2), cur.yAsInt()*cellSize + (cellSize/2));
                    }
                    if (pre.x < cur.x) {
                        // Rechts
                        g2.drawLine(cur.xAsInt() * cellSize - (cellSize/2), cur.yAsInt()*cellSize + (cellSize/2), cur.xAsInt() * cellSize + (cellSize/2), cur.yAsInt()*cellSize + (cellSize/2));
                    }
                    if (cur.y < pre.y) {
                        // Oben
                        g2.drawLine(cur.xAsInt()*cellSize + (cellSize/2), cur.yAsInt() * cellSize + (cellSize/2), cur.xAsInt()*cellSize + (cellSize/2), (cur.yAsInt()+1) * cellSize + (cellSize/2));
                    }
                    if (pre.y < cur.y) {
                        g2.drawLine(cur.xAsInt()*cellSize + (cellSize/2), cur.yAsInt() * cellSize - (cellSize/2), cur.xAsInt()*cellSize + (cellSize/2), cur.yAsInt() * cellSize + (cellSize/2));
                    }

                }
            }
        }

    }

    private void generateEmptyMaze() {
        buffer.clear();
        solution.clear();
        mazeFinished = false;
        isCreating = false;
        maze = new MazeTile[size][size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                maze[x][y] = new MazeTile();
            }
        }
    }

    private void startGenerating() {
        isCreating = true;
        amountSeen = 1;
        buffer.clear();
        maze[0][0].start = true;
        maze[0][0].x = 0;
        maze[0][0].y = 0;
        maze[0][0].isSeen = true;
        maze[size-1][size-1].finish = true;
        buffer.add( maze[0][0]);
    }

    private void startSearching() {
        isSearching = true;
        buffer.clear();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                maze[x][y].isVisited = false;
            }
        }
        maze[0][0].isVisited = true;
        solution.add(new Vektor(0,0));
    }

    private void startStepSearching() {
        buffer.clear();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                maze[x][y].isVisited = false;
            }
        }
        maze[0][0].isVisited = true;
        solution.add(new Vektor(0,0));
    }

    private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "gen":
                    generateEmptyMaze();
                    startGenerating();
                    break;
                case "sea":
                    if (mazeFinished) {
                        startSearching();
                    }
                    break;
                case "ste":
                    if (mazeFinished && !isSearching) {
                        searchStep();
                    }
                    break;
            }
        }
    }

    private void searchStep() {
        switch (search.getSelectedItem().toString()) {
            case "Greedy":
                for (int i = 0; i < speed; i++) {
                    greedyStep();
                    if ( solution.get(solution.size()-1).x == size-1 &&  solution.get(solution.size()-1).y == size-1) {
                        isSearching = false;
                        break;
                    }
                }
                break;
            case "A*":
                for (int i = 0; i < speed; i++) {
                    aStarStep();
                    if (solution.get(solution.size()-1).x == size-1 &&  solution.get(solution.size()-1).y == size-1) {
                        isSearching = false;
                        break;
                    }
                }
                break;
        }
    }

}
