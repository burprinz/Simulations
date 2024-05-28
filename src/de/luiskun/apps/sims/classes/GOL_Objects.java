package de.luiskun.apps.sims.classes;

import de.luiskun.tools.Vektor;

public class GOL_Objects {

    private static final Vektor[] glider = new Vektor[] {
            new Vektor(1,-1),
            new Vektor(1,0),
            new Vektor(1,1),
            new Vektor(0, 1),
            new Vektor(-1, 0)
    };

    private static final Vektor[] pulsar = new Vektor[] {
            new Vektor(-4, -6),
            new Vektor(-3, -6),
            new Vektor(-2, -6),
            new Vektor(2, -6),
            new Vektor(3, -6),
            new Vektor(4, -6),
            new Vektor(-6, -4),
            new Vektor(-1, -4),
            new Vektor(1, -4),
            new Vektor(6, -4),
            new Vektor(-6, -3),
            new Vektor(-1, -3),
            new Vektor(1, -3),
            new Vektor(6, -3),
            new Vektor(-6, -2),
            new Vektor(-1, -2),
            new Vektor(1, -2),
            new Vektor(6, -2),
            new Vektor(-4, -1),
            new Vektor(-3, -1),
            new Vektor(-2, -1),
            new Vektor(2, -1),
            new Vektor(3, -1),
            new Vektor(4, -1),
            new Vektor(-4, 1),
            new Vektor(-3, 1),
            new Vektor(-2, 1),
            new Vektor(2, 1),
            new Vektor(3, 1),
            new Vektor(4, 1),
            new Vektor(-6, 2),
            new Vektor(-1, 2),
            new Vektor(1, 2),
            new Vektor(6, 2),
            new Vektor(-6, 3),
            new Vektor(-1, 3),
            new Vektor(1, 3),
            new Vektor(6, 3),
            new Vektor(-6, 4),
            new Vektor(-1, 4),
            new Vektor(1, 4),
            new Vektor(6, 4),
            new Vektor(-4, 6),
            new Vektor(-3, 6),
            new Vektor(-2, 6),
            new Vektor(2, 6),
            new Vektor(3, 6),
            new Vektor(4, 6)
    };

    private static final Vektor[] cribbage = new Vektor[] {
            new Vektor(-11, -10),
            new Vektor(-10, -10),
            new Vektor(-11, -9),
            new Vektor(-14, -8),
            new Vektor(-13, -8),
            new Vektor(-11, -8),
            new Vektor(0, -8),
            new Vektor(-14, -7),
            new Vektor(-11, -7),
            new Vektor(-10, -7),
            new Vektor(0, -7),
            new Vektor(1, -7),
            new Vektor(2, -7),
            new Vektor(-12, -6),
            new Vektor(-9, -6),
            new Vektor(3, -6),
            new Vektor(-14, -5),
            new Vektor(-12, -5),
            new Vektor(-11, -5),
            new Vektor(-10, -5),
            new Vektor(-8, -5),
            new Vektor(2, -5),
            new Vektor(3, -5),
            new Vektor(-15, -4),
            new Vektor(-13, -4),
            new Vektor(-15, -3),
            new Vektor(-12, -3),
            new Vektor(-11, -3),
            new Vektor(-10, -3),
            new Vektor(-9, -3),
            new Vektor(5, -3),
            new Vektor(6, -3),
            new Vektor(-14, -2),
            new Vektor(-13, -2),
            new Vektor(-10, -2),
            new Vektor(5, -2),
            new Vektor(6, -2),
            new Vektor(14, -2),
            new Vektor(15, -2),
            new Vektor(-12, -1),
            new Vektor(-11, -1),
            new Vektor(-5, -1),
            new Vektor(-4, -1),
            new Vektor(-3, -1),
            new Vektor(5, -1),
            new Vektor(6, -1),
            new Vektor(13, -1),
            new Vektor(15, -1),
            new Vektor(-12, 0),
            new Vektor(13, 0),
            new Vektor(-14, 1),
            new Vektor(-12, 1),
            new Vektor(-5, 1),
            new Vektor(-4, 1),
            new Vektor(4, 1),
            new Vektor(5, 1),
            new Vektor(6, 1),
            new Vektor(12, 1),
            new Vektor(13, 1),
            new Vektor(-14, 2),
            new Vektor(-13, 2),
            new Vektor(-5, 2),
            new Vektor(-4, 2),
            new Vektor(11, 2),
            new Vektor(14, 2),
            new Vektor(15, 2),
            new Vektor(-5, 3),
            new Vektor(-4, 3),
            new Vektor(10, 3),
            new Vektor(11, 3),
            new Vektor(12, 3),
            new Vektor(13, 3),
            new Vektor(16, 3),
            new Vektor(14, 4),
            new Vektor(16, 4),
            new Vektor(-2, 5),
            new Vektor(-1, 5),
            new Vektor(9, 5),
            new Vektor(11, 5),
            new Vektor(12, 5),
            new Vektor(13, 5),
            new Vektor(15, 5),
            new Vektor(-2, 6),
            new Vektor(10, 6),
            new Vektor(13, 6),
            new Vektor(-1, 7),
            new Vektor(0, 7),
            new Vektor(1, 7),
            new Vektor(11, 7),
            new Vektor(12, 7),
            new Vektor(15, 7),
            new Vektor(1, 8),
            new Vektor(12, 8),
            new Vektor(14, 8),
            new Vektor(15, 8),
            new Vektor(12, 9),
            new Vektor(11, 10),
            new Vektor(12, 10)
    };

    public static boolean[][] addGliderToGrid(boolean[][] grid, Vektor midPos) {
        final int size = grid[0].length;

        for (Vektor v : glider) {
            grid[(midPos.xAsInt()+v.xAsInt())%size][(midPos.yAsInt()+v.yAsInt())%size] = true;
        }

        return grid;
    }

    public static boolean[][] addPulsarToGrid(boolean[][] grid, Vektor midPos) {
        final int size = grid[0].length;

        for (Vektor v : pulsar) {
            grid[(midPos.xAsInt()+v.xAsInt())%size][(midPos.yAsInt()+v.yAsInt())%size] = true;
        }

        return grid;
    }

    public static final boolean[][] addCribbageToGrid(boolean[][] grid, Vektor midPos) {
        final int size = grid[0].length;

        for (Vektor v : cribbage) {
            grid[(midPos.xAsInt() + v.xAsInt()) % size][(midPos.yAsInt() + v.yAsInt()) % size] = true;
        }

        return grid;
    }
}
