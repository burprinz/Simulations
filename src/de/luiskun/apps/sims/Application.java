package de.luiskun.apps.sims;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class Application extends JPanel {

    public static final int WIDTH = 900;
    public static final int HEIGHT = 700;

    // Key Input
    public boolean leftPressed, rightPressed, spacePressed;

    public Application() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDoubleBuffered(true);
        this.setLayout(null);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (Mandelbrot.class) {
                    switch (ke.getID()) {
                        case KeyEvent.KEY_PRESSED:
                            changePressed(ke.getKeyCode(), true);
                            break;
                        case KeyEvent.KEY_RELEASED:
                            changePressed(ke.getKeyCode(), false);
                            break;
                    }
                    return false;
                }
            }
        });
    }

    private void changePressed(int keycode, boolean b) {
        switch (keycode) {
            case KeyEvent.VK_LEFT:
                leftPressed = b;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = b;
                break;
            case KeyEvent.VK_SPACE:
                spacePressed = b;
                break;
        }
    }


    public abstract void update();

    @Override
    public abstract void paintComponent(Graphics g);

}
