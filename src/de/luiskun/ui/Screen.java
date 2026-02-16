package de.luiskun.ui;

import de.luiskun.apps.Application;
import de.luiskun.apps.ApplicationInfo;
import de.luiskun.apps.games.centipede.Centipede;
import de.luiskun.apps.games.snake.Snake;
import de.luiskun.apps.sorting.*;
import de.luiskun.tools.FileHandler;
import de.luiskun.apps.sims.WeightedVoronoi;
import de.luiskun.apps.sims.*;
import de.luiskun.apps.games.spaceblaster.SpaceBlaster;

import javax.swing.*;
import java.awt.*;

import java.util.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;

public class Screen extends JPanel implements Runnable {

    public static final int WIDTH = 900, HEIGHT = 700;

    public static final int SIM = 0;
    public static final int GAME = 1;
    public static final int SORT = 2;


    private Thread thread;
    private int fps = 60;

    Application selected;
    private ButtonHandler bh = new ButtonHandler();

    private JButton mandelbrotB, elemCelAutoB, fallingSandB, timesTablesCarB;

    private BufferedImage background;

    List<JButton> buttons;

    int currentPage = 0;

    int mode = 0;

    Map<String, ApplicationInfo> sims;
    Map<String, ApplicationInfo> games;
    Map<String, ApplicationInfo> sort;

    public Screen() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDoubleBuffered(true);
        this.setLayout(null);

        games = new HashMap<>();
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

        sims = new HashMap<>();
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
                return new FaceAverager();
            }

            @Override
            public int getPosition() {
                return 13;
            }
        });

        // PROCEDUAL GENERATION, RASTERIZER

        initSort();

        background = FileHandler.SetupImage("res/previews/background.png", WIDTH, HEIGHT);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (Mandelbrot.class) {
                    switch (ke.getID()) {
                        case KeyEvent.KEY_PRESSED:
                            switch (ke.getKeyCode()) {
                                case KeyEvent.VK_Z:
                                    if (selected != null) {
                                        remove(selected);
                                        selected = null;
                                    }
                                    showUI();
                                    repaint();
                                    break;
                                case KeyEvent.VK_LEFT:
                                    if (selected == null) {
                                        currentPage--;
                                        resetButtons(currentPage);
                                    }
                                    break;
                                case KeyEvent.VK_RIGHT:
                                    if (selected == null) {
                                        currentPage++;
                                        resetButtons(currentPage);
                                    }
                                    break;
                            }
                            break;
                        case KeyEvent.KEY_RELEASED:

                            break;
                    }
                    return false;
                }
            }
        });

        buttons = new ArrayList<>();

        resetButtons(currentPage);
    }

    private void initSort() {
        sort = new HashMap<>();
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

    private void resetButtons(int page) {

        if (buttons.size() > 0) {
            for(JButton b : buttons) {
                b.removeActionListener(bh);
                this.remove(b);
            }
            buttons.clear();
        }

        page = getAcualPage(page);
        this.currentPage = page;

        int amountButtons = getAmountButtons(page);

        int start = page * 9;

        int size;
        String[] names = null;

        if (mode == SIM) {
            size = sims.size();
            names = sortNames(sims.values().toArray(new ApplicationInfo[0]), sims.keySet().toArray(new String[0]));
        } else if (mode == GAME) {
            size = games.size();
            names = sortNames(games.values().toArray(new ApplicationInfo[0]), games.keySet().toArray(new String[0]));
        } else {
            size = sort.size();
            names = sortNames(sort.values().toArray(new ApplicationInfo[0]), sort.keySet().toArray(new String[0]));
        }

        if (names != null) {
            for (int i = 0; i < 9; i++) {
                if (i+start >= size) {
                    break;
                }
                int x = 25 + (i%3) * 300;
                int y = 25 + (i/3) * 217;
                JButton button = new JButton(new ImageIcon("res/previews/" + names[start+i] + ".png"));
                button.setBounds(x, y, 250, 167);
                button.setActionCommand(names[start+i]);
                button.addActionListener(bh);
                buttons.add(button);
                this.add(button);

            }
        }

        JButton gameSwitch = new JButton(new ImageIcon(FileHandler.SetupImage("res/icons/controller.png", 50, 50)));
        gameSwitch.setBounds(WIDTH-55, HEIGHT-55, 50, 50);
        gameSwitch.setOpaque(false);
        gameSwitch.setContentAreaFilled(false);
        gameSwitch.setBorderPainted(false);
        gameSwitch.setActionCommand("gameSwitch");
        gameSwitch.addActionListener(bh);
        buttons.add(gameSwitch);
        this.add(gameSwitch);

        JButton colorSwitch = new JButton(new ImageIcon(FileHandler.SetupImage("res/icons/sorting.png", 50, 50)));
        colorSwitch.setBounds(WIDTH-110, HEIGHT-55, 50, 50);
        colorSwitch.setOpaque(false);
        colorSwitch.setContentAreaFilled(false);
        colorSwitch.setBorderPainted(false);
        colorSwitch.setActionCommand("colorSwitch");
        colorSwitch.addActionListener(bh);
        buttons.add(colorSwitch);
        this.add(colorSwitch);

        JButton simSwitch = new JButton(new ImageIcon(FileHandler.SetupImage("res/icons/laptop.png", 50, 50)));
        simSwitch.setBounds(WIDTH-165, HEIGHT-55, 50, 50);
        simSwitch.setOpaque(false);
        simSwitch.setContentAreaFilled(false);
        simSwitch.setBorderPainted(false);
        simSwitch.setActionCommand("simSwitch");
        simSwitch.addActionListener(bh);
        buttons.add(simSwitch);
        this.add(simSwitch);

        JButton goLeft = new JButton(new ImageIcon(FileHandler.SetupImage("res/icons/leftarrow.png", 50, 50)));
        goLeft.setBounds(WIDTH/2 - 150, HEIGHT-55, 50, 50);
        goLeft.setOpaque(false);
        goLeft.setContentAreaFilled(false);
        goLeft.setBorderPainted(false);
        goLeft.addActionListener(listener -> {
            currentPage--;
            resetButtons(currentPage);
        });
        buttons.add(goLeft);
        this.add(goLeft);

        JButton goRight = new JButton(new ImageIcon(FileHandler.SetupImage("res/icons/rightarrow.png", 50, 50)));
        goRight.setBounds(WIDTH/2 + 100, HEIGHT-55, 50, 50);
        goRight.setOpaque(false);
        goRight.setContentAreaFilled(false);
        goRight.setBorderPainted(false);
        goRight.addActionListener(listener -> {
            currentPage++;
            resetButtons(currentPage);
        });
        buttons.add(goRight);
        this.add(goRight);


    }

    private int getAcualPage(int page) {
        final int buttonsPerSite = 9;
        int amApps;

        if (mode == SIM) {
            amApps = sims.size();
        } else if (mode == GAME) {
            amApps = games.size();
        } else {
            amApps = sort.size();
        }

        if (page < 0) {
            page = 0;
        }

        int startIndex = page * buttonsPerSite;
        while (startIndex > amApps) {
            page--;
            startIndex = page * buttonsPerSite;
        }
        return page;
    }

    private int getAmountButtons(int page) {
        final int start = page * 9;
        int size;

        if (mode == SIM) {
            size = sims.size();
        } else if (mode == GAME) {
            size = games.size();
        }else {
            size = sort.size();
        }

        int count = 0;


        for (int i = start; i < start + 9; i++) {
            if (i < size) {
                count++;
            } else {
                break;
            }
        }

        return count;
    }

    public void hideUI() {
        resetButtons(currentPage);
        for (JButton b : buttons) {
            b.setVisible(false);
        }
    }

    public void showUI() {
        for (JButton b : buttons) {
            b.setVisible(true);
        }
    }

    public void startThread() {
        thread = new Thread(this);
        thread.start();
    }

    public void update() {
        if (selected == null) {

        } else {
            selected.update();
        }
    }

    private int getAmountPages() {
        int am;

        if (mode == SIM) {
            am = sims.size()/9;
            if (sims.size()%9>0)am++;
        } else if (mode == GAME) {
            am = games.size()/9;
            if (games.size()%9>0)am++;
        } else {
            am = sort.size()/9;
            if (sort.size()%9>0)am++;
        }

        return am;
    }

    private String[] sortNames(ApplicationInfo[] in, String[] names) {
        String[] out = new String[in.length];
        for (int i = 0; i < in.length; i++) {
            if (in[i].getPosition() < 0){
                out[in.length-1] = names[i];
                continue;
            }
            out[in[i].getPosition()] = names[i];
        }
        return out;
    }

    @Override
    public void paintComponent(Graphics g) {

        if (selected == null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, 0, WIDTH, HEIGHT);

            g2.drawImage(background, 0, 0, null);

            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30));

            String text = "Page " + (currentPage+1) + " / " + getAmountPages();

            int xPos = WIDTH/2 - g2.getFontMetrics(g2.getFont()).stringWidth(text)/2;
            int yPos = HEIGHT - 20;
            g2.drawString(text, xPos, yPos);

        } else {
            selected.repaint();
        }
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / fps; // 0.0166s
        double delta = 0;
        double lastTime = System.nanoTime();
        double currentTime;
        while (thread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void switchMode(int sel) {
        mode = sel;

        resetButtons(0);
    }

    private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            selected = getApplication(e.getActionCommand());
            if (selected != null) {
                selected.setBounds(0,0,WIDTH, HEIGHT);
                add(selected);
                hideUI();
            }
        }
    }

    private Application getApplication(String name) {
        if (name.equals("gameSwitch")) {
            switchMode(GAME);
            return null;
        } else if (name.equals("colorSwitch")) {
            switchMode(SORT);
            return null;
        } else if (name.equals("simSwitch")) {
            switchMode(SIM);
            return null;
        }else {
            if (mode == SIM) {
                return sims.get(name).getApplication();
            } else if (mode == GAME) {
                return games.get(name).getApplication();
            } if (mode == SORT) {
                return sort.get(name).getApplication();
            }
        }
        return null;
    }
}
