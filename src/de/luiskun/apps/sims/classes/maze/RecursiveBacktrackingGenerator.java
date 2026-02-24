package de.luiskun.apps.sims.classes.maze;

import de.luiskun.apps.sims.classes.MazeTile;
import de.luiskun.tools.Vektor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecursiveBacktrackingGenerator implements MazeStep {

    private List<Vektor> buffer;
    private Random r;

    @Override
    public MazeTile[][] prepare(Vektor start, MazeTile[][] maze) {
        buffer = new ArrayList<>();
        buffer.add(start);
        r = new Random();
        maze[start.yAsInt()][start.xAsInt()].isVisited = true;
        return maze;
    }

    @Override
    public MazeTile[][] step(MazeTile[][] maze) {
        boolean stepMade = false;
        while (!stepMade) {
            Vektor front = buffer.get(buffer.size()-1);
            List<Vektor> neighbors = getPossibleNeighbors(front, maze);
            if (neighbors.size() > 0) {
                Vektor nextFront = neighbors.get(r.nextInt(neighbors.size()));
                maze[nextFront.yAsInt()][nextFront.xAsInt()].isVisited = true;
                maze = openPath(front, nextFront, maze);
                buffer.add(nextFront);
                stepMade = true;
            } else {
                neighbors.remove(neighbors.size()-1);
            }
        }
        return maze;
    }

    private List<Vektor> getPossibleNeighbors(Vektor pos, MazeTile[][] maze) {
        List<Vektor> neighbors = new ArrayList<>();
        for (int x = -1; x <= 1; x+=2) {
            int xPos = pos.xAsInt() + x;
            int yPos = pos.yAsInt();
            if (xPos >= 0 && xPos < maze.length && !maze[yPos][xPos].isVisited) {
                neighbors.add(new Vektor(xPos, yPos));
            }
        }
        for (int y = -1; y <= 1; y+=2) {
            int xPos = pos.xAsInt();
            int yPos = pos.yAsInt() + y;
            if (yPos >= 0 && yPos < maze.length && !maze[yPos][xPos].isVisited) {
                neighbors.add(new Vektor(xPos, yPos));
            }
        }
        return neighbors;
    }

    private MazeTile[][] openPath(Vektor start, Vektor end, MazeTile[][] maze) {
        if (start.xAsInt() == end.xAsInt()) {
            if (start.yAsInt() < end.yAsInt()) {
                maze[start.yAsInt()][start.xAsInt()].downOpen = true;
                maze[end.yAsInt()][end.xAsInt()].upOpen = true;
            } else {
                maze[start.yAsInt()][start.xAsInt()].upOpen = true;
                maze[end.yAsInt()][end.xAsInt()].downOpen = true;
            }
        } else {
            if (start.xAsInt() > end.xAsInt()) {
                maze[start.yAsInt()][start.xAsInt()].leftOpen = true;
                maze[end.yAsInt()][end.xAsInt()].rightOpen = true;
            } else {
                maze[start.yAsInt()][start.xAsInt()].rightOpen = true;
                maze[end.yAsInt()][end.xAsInt()].leftOpen = true;
            }
        }
        return maze;
    }
}
