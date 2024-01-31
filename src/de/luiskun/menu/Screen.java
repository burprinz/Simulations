package de.luiskun.menu;

import de.luiskun.apps.Application;
import de.luiskun.apps.ElementaryCellularAutomata;
import de.luiskun.apps.FallingSand;
import de.luiskun.apps.Mandelbrot;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Screen extends JPanel implements Runnable {

    public static final int WIDTH = 900, HEIGHT = 700;

    private Thread thread;
    private int fps = 60;

    Application selected;
    private ButtonHandler bh = new ButtonHandler();

    private JButton mandelbrotB, elemCelAutoB, fallingSandB;

    public Screen() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDoubleBuffered(true);
        this.setLayout(null);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (Mandelbrot.class) {
                    switch (ke.getID()) {
                        case KeyEvent.KEY_PRESSED:
                            if (ke.getKeyCode() == KeyEvent.VK_Z) {
                                if (selected != null) {
                                    remove(selected);
                                    selected = null;
                                }
                                showUI();
                                repaint();
                            }
                            break;
                        case KeyEvent.KEY_RELEASED:

                            break;
                    }
                    return false;
                }
            }
        });

        mandelbrotB = new JButton(new ImageIcon("res/previews/mandelbrot.png"));
        mandelbrotB.setBounds(25, 50, 250, 167);
        mandelbrotB.setActionCommand("mandelbrot");
        mandelbrotB.addActionListener(bh);
        this.add(mandelbrotB);

        elemCelAutoB = new JButton(new ImageIcon("res/previews/elemcelauto.png"));
        elemCelAutoB.setBounds(325, 50, 250, 167);
        elemCelAutoB.setActionCommand("elemCelAuto");
        elemCelAutoB.addActionListener(bh);
        this.add(elemCelAutoB);

        fallingSandB = new JButton(new ImageIcon("res/previews/sandsim.png"));
        fallingSandB.setBounds(625, 50, 250, 167);
        fallingSandB.setActionCommand("fallingSand");
        fallingSandB.addActionListener(bh);
        this.add(fallingSandB);

    }

    public void hideUI() {
        mandelbrotB.setVisible(false);
        elemCelAutoB.setVisible(false);
        fallingSandB.setVisible(false);
    }

    public void showUI() {
        mandelbrotB.setVisible(true);
        elemCelAutoB.setVisible(true);
        fallingSandB.setVisible(true);
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
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, WIDTH, HEIGHT);

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
            switch (e.getActionCommand()) {
                case "mandelbrot":
                    selected = new Mandelbrot();
                    break;
                case "elemCelAuto":
                    selected = new ElementaryCellularAutomata();
                    break;
                case "fallingSand":
                    selected = new FallingSand();
                    break;
            }
            selected.setBounds(0,0,WIDTH, HEIGHT);
            add(selected);
            hideUI();
        }
    }
}
