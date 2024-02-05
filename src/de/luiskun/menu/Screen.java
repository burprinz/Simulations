package de.luiskun.menu;

import de.luiskun.FileHandler;
import de.luiskun.apps.*;
import de.luiskun.json.Farbpalette;

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

    private Thread thread;
    private int fps = 60;

    Application selected;
    private ButtonHandler bh = new ButtonHandler();

    private JButton mandelbrotB, elemCelAutoB, fallingSandB, timesTablesCarB;

    private BufferedImage background;

    List<String> allApplications;
    List<JButton> buttons;

    int currentPage = 0;

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
        allApplications.add("emptyslot");
        allApplications.add("emptyslot");
        allApplications.add("emptyslot");
        allApplications.add("emptyslot");
        allApplications.add("emptyslot");
        allApplications.add("emptyslot");

        background = FileHandler.SetupImage("res/previews/background", WIDTH, HEIGHT);

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

        for (int i = 0; i < amountButtons; i++) {
            // Zur sicherheit (Top-Kommentar)
            if (i+start >= allApplications.size()) {
                break;
            }

            String name = allApplications.get(start + i);
            int x = 25 + (i%3) * 300;
            int y = 25 + (i/3) * 217;

            JButton button = new JButton(new ImageIcon("res/previews/" + name + ".png"));
            button.setBounds(x, y, 250, 167);
            button.setActionCommand(name);
            button.addActionListener(bh);
            buttons.add(button);
            this.add(button);
        }

    }

    private int getAcualPage(int page) {
        final int buttonsPerSite = 9;
        final int amApps = allApplications.size();

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

        int count = 0;

        for (int i = start; i < start + 9; i++) {
            if (i < allApplications.size()) {
                count++;
            } else {
                break;
            }
        }

        return count;
    }

    public void hideUI() {
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
            default:
                return null;
        }
    }
}
