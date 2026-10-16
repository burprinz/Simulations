package de.luiskun.ui;

import de.luiskun.apps.Application;
import de.luiskun.apps.ApplicationInfo;
import de.luiskun.apps.games.centipede.Centipede;
import de.luiskun.apps.games.snake.Snake;
import de.luiskun.apps.sims.*;
import de.luiskun.apps.sorting.*;

import java.util.HashMap;
import java.util.Map;

public class Applications {

    public static final int SIM = 0;
    public static final int GAME = 1;
    public static final int SORT = 2;

    private final Map<String, ApplicationInfo> sims;
    private final Map<String, ApplicationInfo> games;
    private final Map<String, ApplicationInfo> sort;

    private Map<String, ApplicationInfo> currentCategory;

    public Applications() {
        this.sims = new HashMap<>();
        this.games = new HashMap<>();
        this.sort = new HashMap<>();

        initSims();
        initGames();
        initSort();

        switchCurrentCategory(SIM);
    }

    /**
     * Mode Initialization
     */
    private void initSims() {
        sims.put("mandelbrot", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new Mandelbrot();
            }

            @Override
            public int getPosition() {
                return 0;
            }
        });
        sims.put("elemcelauto", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new ElementaryCellularAutomata();
            }

            @Override
            public int getPosition() {
                return 5;
            }
        });
        sims.put("sandsim", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new FallingSand();
            }

            @Override
            public int getPosition() {
                return 6;
            }
        });
        sims.put("timestablescar", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new TimesTablesCardioid();
            }

            @Override
            public int getPosition() {
                return 7;
            }
        });
        sims.put("phyllotaxis", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new Phyllotaxis();
            }

            @Override
            public int getPosition() {
                return 8;
            }
        });
        sims.put("reactiondiffusion", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new ReactionDiffusion();
            }

            @Override
            public int getPosition() {
                return 9;
            }
        });
        sims.put("firework", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new Fireworks();
            }

            @Override
            public int getPosition() {
                return 1;
            }
        });
        sims.put("sandpiles", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new Sandpiles();
            }

            @Override
            public int getPosition() {
                return 2;
            }
        });
        sims.put("boids", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new Boids();
            }

            @Override
            public int getPosition() {
                return 3;
            }
        });
        sims.put("barnsleyfern", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new BarnsleyFern();
            }

            @Override
            public int getPosition() {
                return 10;
            }
        });
        sims.put("gameoflife", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new GameOfLife();
            }

            @Override
            public int getPosition() {
                return 11;
            }
        });
        sims.put("bouncingcircle", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new BouncingCircle();
            }

            @Override
            public int getPosition() {
                return 4;
            }
        });
        sims.put("searching", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new Pathfinding();
            }

            @Override
            public int getPosition() {
                return 12;
            }
        });
        sims.put("emptyslot", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new FourierDrawing();
            }

            @Override
            public int getPosition() {
                return 13;
            }
        });
    }

    private void initGames() {
        games.put("snake", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new Snake();
            }

            @Override
            public int getPosition() {
                return 0;
            }
        });
        games.put("centipede", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new Centipede();
            }

            @Override
            public int getPosition() {
                return 1;
            }
        });
    }

    private void initSort() {
        sort.put("selectionsort", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new SelectionSort();
            }

            @Override
            public int getPosition() {
                return 0;
            }
        });
        sort.put("bubblesort", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new Bubblesort();
            }

            @Override
            public int getPosition() {
                return 1;
            }
        });
        sort.put("mergesort", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new MergeSort();
            }

            @Override
            public int getPosition() {
                return 2;
            }
        });
        sort.put("quicksort", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new Quicksort();
            }

            @Override
            public int getPosition() {
                return 3;
            }
        });
        sort.put("heapsort", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new Heapsort();
            }

            @Override
            public int getPosition() {
                return 4;
            }
        });
        sort.put("combsort", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new Combsort();
            }

            @Override
            public int getPosition() {
                return 5;
            }
        });
        sort.put("cyclesort", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new Cyclesort();
            }

            @Override
            public int getPosition() {
                return 6;
            }
        });
        sort.put("cocktailsort", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new Cocktailsort();
            }

            @Override
            public int getPosition() {
                return 7;
            }
        });
        sort.put("pancakesort", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new Pancakesort();
            }

            @Override
            public int getPosition() {
                return 8;
            }
        });
        sort.put("gnomesort", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new Gnomesort();
            }

            @Override
            public int getPosition() {
                return 9;
            }
        });
        sort.put("stoogesort", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new Stoogesort();
            }

            @Override
            public int getPosition() {
                return 10;
            }
        });
        sort.put("oddevensort", new ApplicationInfo() {
            @Override
            public Application getApplication() {
                return new OddEvenSort();
            }

            @Override
            public int getPosition() {
                return 11;
            }
        });
    }


    /**
     * Mode Selection
     */
    public int getCurrentSize() {
        if (currentCategory != null) {
            return currentCategory.size();
        }
        return 0;
    }

    public Map<String, ApplicationInfo> getCurrentCategory() {
        return currentCategory;
    }

    public void switchCurrentCategory(int mode) {
        switch(mode) {
            case SIM:
                currentCategory = sims;
                break;
            case GAME:
                currentCategory = games;
                break;
            case SORT:
                currentCategory = sort;
                break;
        }
    }

    public Application getCurrentApp(String name) {
        return currentCategory.get(name).getApplication();
    }
}
