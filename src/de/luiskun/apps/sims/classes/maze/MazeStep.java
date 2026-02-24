package de.luiskun.apps.sims.classes.maze;

import de.luiskun.apps.sims.classes.MazeTile;
import de.luiskun.tools.Vektor;

import java.util.List;

public interface MazeStep {

    public MazeTile[][] prepare(Vektor start, MazeTile[][] maze);

    public MazeTile[][] step(MazeTile[][] maze);

}
