package de.luiskun.apps.sims;

import de.luiskun.apps.Application;
import de.luiskun.apps.sims.classes.MazeTile;
import de.luiskun.apps.sims.classes.maze.MazeStep;
import de.luiskun.apps.sims.classes.maze.RecursiveBacktrackingGenerator;
import de.luiskun.tools.Vektor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Maze extends Application {

    private static final int MAZE_PIXEL_SIZE = HEIGHT;

    boolean generated = false;

    int[] sizes;
    int cellSize;
    int gridSize;

    MazeStep generator;

    MazeTile[][] maze;

    public Maze() {
        super();
        createSizeArray();
        cellSize = sizes[7];
        resetGrid();
        generator = new RecursiveBacktrackingGenerator();
        maze = generator.prepare(new Vektor(0,0), maze);
    }

    private void createSizeArray() {
        List<Integer> sizes = new ArrayList<>();
        for (int i = 3; i < MAZE_PIXEL_SIZE; i++) {
            if (MAZE_PIXEL_SIZE % i == 0) sizes.add(i);
        }
        this.sizes = new int[sizes.size()];
        for (int i  = 0; i < sizes.size(); i++) {
            this.sizes[i] = sizes.get(i);
        }
    }

    private void resetGrid() {
        generated = false;
        gridSize = MAZE_PIXEL_SIZE / cellSize;
        this.maze = new MazeTile[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                maze[i][j] = new MazeTile();
            }
        }
    }

    @Override
    public void update() {
        maze = generator.step(maze);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);


        g2.setColor(Color.WHITE);
        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                MazeTile t = maze[y][x];
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

    }
}
