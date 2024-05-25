package de.luiskun.ui;

import de.luiskun.apps.Application;
import de.luiskun.apps.games.centipede.Centipede;
import de.luiskun.apps.games.snake.Snake;
import de.luiskun.apps.sorting.*;
import de.luiskun.tools.FileHandler;
import de.luiskun.apps.sims.WeightedVoronoi;
import de.luiskun.apps.sims.*;
import de.luiskun.apps.games.spaceblaster.SpaceBlaster;

import javax.swing.*;
import java.awt.*;

import java.util.List;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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

    List<String> allApplications;
    List<String> allGames;
    List<String> allSortings;
    List<JButton> buttons;

    int currentPage = 0;

    int mode = 0;

    public Screen() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDoubleBuffered(true);
        this.setLayout(null);

        allApplications = new ArrayList<>();
        allApplications.add("mandelbrot");
        allApplications.add("elemcelauto");
        allApplications.add("sandsim");
        allApplications.add("timestablescar");
        allApplications.add("phyllotaxis");
        allApplications.add("reactiondiffusion");
        allApplications.add("firework");
        allApplications.add("searching");
        allApplications.add("sandpiles");
        allApplications.add("boids");
        allApplications.add("barnsleyfern");
        allApplications.add("emptyslot");
        allApplications.add("emptyslot");

        allGames = new ArrayList<>();
        allGames.add("snake");
        allGames.add("emptyslot");
        allGames.add("emptyslot");
        allGames.add("emptyslot");

        allSortings = new ArrayList<>();
        allSortings.add("selectionsort");
        allSortings.add("bubblesort");
        allSortings.add("mergesort");
        allSortings.add("quicksort");
        allSortings.add("heapsort");
        allSortings.add("combsort");
        allSortings.add("cyclesort");
        allSortings.add("cocktailsort");
        allSortings.add("pancakesort");
        allSortings.add("gnomesort");
        allSortings.add("stoogesort");
        allSortings.add("oddevensort");

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

        if (mode == SIM) {
            size = allApplications.size();
        } else if (mode == GAME) {
            size = allGames.size();
        } else {
            size = allSortings.size();
        }

        for (int i = 0; i < amountButtons; i++) {
            // Zur sicherheit (Top-Kommentar)
            if (i+start >= size) {
                break;
            }

            String name = "";

            if (mode == SIM) {
                name = allApplications.get(start + i);
            } else if (mode == GAME) {
                name = allGames.get(start + i);
            } else {
                name = allSortings.get(start + i);
            }

            int x = 25 + (i%3) * 300;
            int y = 25 + (i/3) * 217;

            JButton button = new JButton(new ImageIcon("res/previews/" + name + ".png"));
            button.setBounds(x, y, 250, 167);
            button.setActionCommand(name);
            button.addActionListener(bh);
            buttons.add(button);
            this.add(button);

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
            amApps = allApplications.size();
        } else if (mode == GAME) {
            amApps = allGames.size();
        } else {
            amApps = allSortings.size();
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
            size = allApplications.size();
        } else if (mode == GAME) {
            size = allGames.size();
        }else {
            size = allSortings.size();
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
            am = allApplications.size()/9;
            if (allApplications.size()%9>0)am++;
        } else if (mode == GAME) {
            am = allGames.size()/9;
            if (allGames.size()%9>0)am++;
        } else {
            am = allSortings.size()/9;
            if (allSortings.size()%9>0)am++;
        }

        return am;
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
                switch (name) {
                    case "mandelbrot":
                        return new Mandelbrot();
                    case "elemcelauto":
                        return new ElementaryCellularAutomata();
                    case "sandsim":
                        return new FallingSand();
                    case "timestablescar":
                        return new TimesTablesCardioid();
                    case "phyllotaxis":
                        return new Phyllotaxis();
                    case "reactiondiffusion":
                        return new ReactionDiffusion();
                    case "firework":
                        return new Fireworks();
                    case "searching":
                        return new Pathfinding();
                    case "sandpiles":
                        return new Sandpiles();
                    case "boids":
                        return new Boids();
                    case "barnsleyfern":
                        return new BarnsleyFern();
                    default:
                        return new WeightedVoronoi();
                }
            } else if (mode == GAME) {
                switch (name) {
                    case "snake":
                        return new Snake();
                    default:
                        return new Centipede();
                }
            } if (mode == SORT) {
                switch (name) {
                    case "selectionsort":
                        return new SelectionSort();
                    case "bubblesort":
                        return new Bubblesort();
                    case "mergesort":
                        return new MergeSort();
                    case "quicksort":
                        return new Quicksort();
                    case "heapsort":
                        return new Heapsort();
                    case "combsort":
                        return new Combsort();
                    case "cyclesort":
                        return new Cyclesort();
                    case "cocktailsort":
                        return new Cocktailsort();
                    case "pancakesort":
                        return new Pancakesort();
                    case "gnomesort":
                        return new Gnomesort();
                    case "stoogesort":
                        return new Stoogesort();
                    case "oddevensort":
                        return new OddEvenSort();
                    default:
                        return null;
                }
            }
        }
        return null;
    }
}
