package de.luiskun.menu;

import de.luiskun.FileHandler;
import de.luiskun.apps.WeightedVoronoi;
import de.luiskun.apps.*;
import de.luiskun.games.spaceblaster.SpaceBlaster;

import javax.swing.*;
import java.awt.*;

import java.io.File;
import java.util.List;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Screen extends JPanel implements Runnable {

    public static final int WIDTH = 900, HEIGHT = 700;

    private Thread thread;
    private int fps = 60;

    Application selected;
    private ButtonHandler bh = new ButtonHandler();

    private JButton mandelbrotB, elemCelAutoB, fallingSandB, timesTablesCarB;

    private BufferedImage background;

    List<String> allApplications;
    List<String> allGames;
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
        allApplications.add("emptyslot");
        allApplications.add("emptyslot");
        allApplications.add("emptyslot");

        allGames = new ArrayList<>();
        allGames.add("emptyslot");
        allGames.add("emptyslot");
        allGames.add("emptyslot");
        allGames.add("emptyslot");

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
                                    currentPage--;
                                    resetButtons(currentPage);
                                    break;
                                case KeyEvent.VK_RIGHT:
                                    currentPage++;
                                    resetButtons(currentPage);
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

        int size = mode==0?allApplications.size():allGames.size();

        for (int i = 0; i < amountButtons; i++) {
            // Zur sicherheit (Top-Kommentar)
            if (i+start >= size) {
                break;
            }

            String name = mode==0?allApplications.get(start + i):allGames.get(start + i);
            int x = 25 + (i%3) * 300;
            int y = 25 + (i/3) * 217;

            JButton button = new JButton(new ImageIcon("res/previews/" + name + ".png"));
            button.setBounds(x, y, 250, 167);
            button.setActionCommand(name);
            button.addActionListener(bh);
            buttons.add(button);
            this.add(button);

        }

        JButton modeSwitch = new JButton(new ImageIcon(FileHandler.SetupImage("res/icons/controller.png", 50, 50)));
        modeSwitch.setBounds(WIDTH-55, HEIGHT-55, 50, 50);
        modeSwitch.setOpaque(false);
        modeSwitch.setContentAreaFilled(false);
        modeSwitch.setBorderPainted(false);
        modeSwitch.setActionCommand("modeSwitch");
        modeSwitch.addActionListener(bh);
        buttons.add(modeSwitch);
        this.add(modeSwitch);

    }

    private int getAcualPage(int page) {
        final int buttonsPerSite = 9;
        final int amApps = mode==0?allApplications.size():allGames.size();

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
        final int size = mode==0?allApplications.size():allGames.size();

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

    @Override
    public void paintComponent(Graphics g) {

        if (selected == null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, 0, WIDTH, HEIGHT);

            g2.drawImage(background, 0, 0, null);
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

    private void switchMode() {
        mode = mode==0?1:0;

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
        if (name.equals("modeSwitch")) {
            switchMode();
            return null;
        } else {
            if (mode == 0) {
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
                    default:
                        return new BarnsleyFern();
                }
            } else {
                switch (name) {
                    default:
                        return new SpaceBlaster();
                }
            }
        }
    }
}
