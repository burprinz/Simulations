package de.luiskun.apps;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class FallingSand extends Application{

    public static final int WIDTH = 900;
    public static final int HEIGHT = 700;

    private static final Color[] colors =  {
            new Color(10, 17, 36),
            new Color(18,34,59),
            new Color(14,59,80),
            new Color(182,81,13),
            new Color(205,110,16),
            new Color(233,134,7)
    };

    private int w = 8;
    public int cols, rows;
    public int[][] grid;

    int spawnRadius = 1;
    int expRadius = 1;

    private MouseHandler mh = new MouseHandler();
    public FallingSand() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDoubleBuffered(true);
        this.setLayout(null);
        this.addMouseListener(mh);
        changeSandSize(w);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (FallingSand.class) {

                    switch (ke.getID()) {
                        case KeyEvent.KEY_PRESSED:
                            switch (ke.getKeyCode()) {
                                case KeyEvent.VK_1:
                                    changeSandSize(2);
                                    break;
                                case KeyEvent.VK_2:
                                    changeSandSize(4);
                                    break;
                                case KeyEvent.VK_3:
                                    changeSandSize(8);
                                    break;
                                case KeyEvent.VK_4:
                                    changeSandSize(20);
                                    break;
                                case KeyEvent.VK_6:
                                    spawnRadius = 1;
                                    break;
                                case KeyEvent.VK_7:
                                    spawnRadius = 2;
                                    break;
                                case KeyEvent.VK_8:
                                    spawnRadius = 4;
                                    break;
                                case KeyEvent.VK_9:
                                    spawnRadius = 8;
                                    break;
                                case KeyEvent.VK_A:
                                    expRadius = 1;
                                    break;
                                case KeyEvent.VK_B:
                                    expRadius = 3;
                                    break;
                                case KeyEvent.VK_C:
                                    expRadius = 9;
                                    break;
                                case KeyEvent.VK_D:
                                    expRadius = 16;
                                    break;
                                case KeyEvent.VK_E:
                                    expRadius = 25;
                                    break;
                                case KeyEvent.VK_F:
                                    expRadius = 40;
                                    break;
                            }
                            break;
                    }

                    return false;
                }
            }
        });
    }

    private void changeSandSize(int num) {
        w = num;
        resetGrid();
    }

    private void resetGrid() {
        cols = HEIGHT / w;
        rows = HEIGHT / w;
        grid = new int[cols][rows];
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);


        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                if (grid[x][y] > 0) {
                    g2.setColor(colors[grid[x][y]-1]);
                    g2.fillRect(x * w, y * w, w, w);
                }
                g2.setColor(Color.WHITE);
               // g2.drawRect(x * w, y * w, w, w);
            }
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 17));
        g2.setColor(Color.WHITE);
        int x = 710;
        int y = 25;
        g2.drawString("1-4=Field Size", x, y);
        y += 30;
        g2.drawString("6-9=Spawn Radius", x, y);
        y += 30;
        g2.drawString("a-f=Explosion Radius", x, y);
        y += 30;
        g2.drawString("Left Mouse = Spawn", x, y);
        y += 30;
        g2.drawString("Right Mouse =", x, y);
        y += 25;
        g2.drawString("                     Explosion", x, y);
        y += 30;
        g2.drawString("Backspace = Back to", x, y);
        y += 25;
        g2.drawString("                          Menu", x, y);

        y += 100;
        g2.drawString("Sandsize = " + w, x, y);
        y += 30;
        g2.drawString("Spawn Radius = "  +spawnRadius, x, y);
        y += 30;
        g2.drawString("Explosion Radius = " + expRadius, x, y);
    }

    public void update() {


        int[][] nextGrid = new int[cols][rows];

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                int state = grid[x][y];
                if (state > 0) {

                    int rndDir = 1;
                    Random r = new Random();
                    if (r.nextBoolean()) {
                        rndDir *= -1;
                    }

                    boolean set = false;
                    if (y == rows - 1) {
                        // Sitzt schon auf Boden
                        nextGrid[x][y] = state;
                        set = true;
                    }

                    if (!set) {
                        if (grid[x][y + 1] == 0 && nextGrid[x][y+1] == 0) {
                            // Unten Frei und nicht belegt
                            nextGrid[x][y + 1] = state;
                            set = true;
                        }

                        if(!set) {
                            if (x + rndDir >= 0 && x + rndDir < cols && grid[x+rndDir][y+1] == 0 && nextGrid[x+rndDir][y+1] == 0) {
                                nextGrid[x+rndDir][y+1] = state;
                                set = true;
                            }

                            if(!set) {
                                if (x - rndDir >= 0 && x - rndDir < cols && grid[x-rndDir][y+1] == 0 && nextGrid[x-rndDir][y+1] == 0) {
                                    nextGrid[x-rndDir][y+1] = state;
                                    set = true;
                                }

                                if(!set) {
                                    nextGrid[x][y] = state;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (mh.pressed) {
            Point b = this.getMousePosition();

            colorUpdateCount++;
            if(colorUpdateCount > 100) {
                colorUpdateCount = 0;
                val++;
                if(val > colors.length) {
                    val = 1;
                }
            }


            if (b != null) {
                int mCol = (int) Math.floor(b.getX() / w);
                int mRow = (int) Math.floor(b.getY() / w);

                if(mCol >= 0 && mCol < cols && mRow >= 0 && mRow < rows) {
                        for (int y = mRow - spawnRadius; y <= mRow + spawnRadius; y++) {
                            for (int x = mCol - spawnRadius; x <= mCol + spawnRadius; x++) {
                                if(x >= 0 && x < cols && y >= 0 && y < rows && grid[x][y] == 0 && nextGrid[x][y] == 0) {
                                    nextGrid[x][y] = val;
                                }
                            }
                        }
                }
            }
        }

        if (mh.right) {
            Point b = this.getMousePosition();
            if (b != null) {
                int mCol = (int) Math.floor(b.getX() / w);
                int mRow = (int) Math.floor(b.getY() / w);

                if(mCol >= 0 && mCol < cols && mRow >= 0 && mRow < rows) {
                    for (int y = mRow - expRadius; y <= mRow + expRadius; y++) {
                        for (int x = mCol - expRadius; x <= mCol + expRadius; x++) {
                            if(x >= 0 && x < cols && y >= 0 && y < rows && grid[x][y] == 0 && nextGrid[x][y] == 0) {
                                nextGrid[x][y] = 0;
                            }
                        }
                    }
                }
            }
        }

        grid = nextGrid;

    }


    int colorUpdateCount;
    int val = 1;

    public class MouseHandler implements MouseListener {

        public boolean pressed = false;
        public boolean right = false;

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            switch (e.getButton()) {
                case MouseEvent.BUTTON1:
                    pressed = true;
                    break;
                case MouseEvent.BUTTON3:
                    right = true;
                    break;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            switch (e.getButton()) {
                case MouseEvent.BUTTON1:
                    pressed = false;
                    break;
                case MouseEvent.BUTTON3:
                    right = false;
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

}
