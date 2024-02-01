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

    private int colormode;

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
                                case KeyEvent.VK_0:
                                    spawnRadius = 16;
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
                                case KeyEvent.VK_H:
                                    colormode = 0;
                                    break;
                                case KeyEvent.VK_I:
                                    colormode = 1;
                                    break;
                            }
                            break;
                    }

                    return false;
                }
            }
        });

        initSettings();
    }

    private JSlider fieldSize, spawnRadiusS;

    private void initSettings() {
        int x = 715;
        int y = 15;
        JLabel fSizeInfo = new JLabel("Field Size: 8");
        fSizeInfo.setBounds(x, y, 150, 10);
        fSizeInfo.setForeground(Color.WHITE);
        this.add(fSizeInfo);

        y += 15;

        fieldSize = new JSlider();
        fieldSize.setBounds(x, y, 170, 25);
        fieldSize.setMinimum(0);
        fieldSize.setMaximum(3);
        fieldSize.setValue(2);
        fieldSize.setBackground(Color.BLACK);
        fieldSize.addChangeListener(listener -> {
            int val = 0;

            switch (fieldSize.getValue()) {
                case 0:
                    val = 2;
                    break;
                case 1:
                    val = 4;
                    break;
                case 2:
                    val = 8;
                    break;
                case 3:
                default:
                    val = 20;
                    break;
            }

            changeSandSize(val);
            fSizeInfo.setText("Field Size: " + val);
        });
        this.add(fieldSize);

        y += 30;

        JLabel spawnRadiusInfo = new JLabel("Spawn Radius: 1");
        spawnRadiusInfo.setBounds(x, y, 150, 10);
        spawnRadiusInfo.setForeground(Color.WHITE);
        this.add(spawnRadiusInfo);

        y += 15;

        spawnRadiusS = new JSlider();
        spawnRadiusS.setBounds(x, y, 170, 25);
        spawnRadiusS.setMinimum(0);
        spawnRadiusS.setMaximum(4);
        spawnRadiusS.setValue(0);
        spawnRadiusS.setBackground(Color.BLACK);
        spawnRadiusS.addChangeListener(listener -> {
            int val;
            switch (spawnRadiusS.getValue()) {
                case 0:
                    val = 1;
                    break;
                case 1:
                    val = 2;
                    break;
                case 2:
                    val = 4;
                    break;
                case 3:
                    val = 8;
                    break;
                case 4:
                default:
                    val = 16;
                    break;
            }
            spawnRadius = val;
            spawnRadiusInfo.setText("Spawn Radius: " + val);
        });
        this.add(spawnRadiusS);


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
                    g2.setColor(valueToColor(grid[x][y]));
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
        /**
        g2.drawString("1-4=Field Size", x, y);
        y += 30;
        g2.drawString("6-0=Spawn Radius", x, y);
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
        y += 25;
        g2.drawString("Z = Back", x, y);

         */

        y += 325;
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

            if (b != null) {
                int mCol = (int) Math.floor(b.getX() / w);
                int mRow = (int) Math.floor(b.getY() / w);

                if(mCol >= 0 && mCol < cols && mRow >= 0 && mRow < rows) {
                    int val = getCurrentColor();
                    for (int y = mRow - spawnRadius; y <= mRow + spawnRadius; y++) {
                        for (int x = mCol - spawnRadius; x <= mCol + spawnRadius; x++) {
                            boolean xInBorder = x >= 0 && x < cols;
                            boolean yInBorder = y >= 0 && y < rows;
                            if(xInBorder && yInBorder && grid[x][y] == 0 && nextGrid[x][y] == 0) {
                                nextGrid[x][y] = val;
                            }
                        }
                    }
                }
            }
        }

        /*
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
        */

        grid = nextGrid;

    }


    int colorUpdateCount;
    int val = 1;
    int maxRainbow = 100;

    private int getCurrentColor() {
        int maxVal;
        int updateBorder;

        // Borderbedingungen berechnen
        if (colormode == 0) {
            maxVal = colors.length;
            updateBorder = 100;
        } else {
            // RGB
            maxVal = maxRainbow;
            updateBorder = 10;
        }

        // Farbenposition updaten, wenn noetig
        colorUpdateCount++;
        if (colorUpdateCount > updateBorder) {
            colorUpdateCount = 0;
            val++;
            if (val > maxVal) {
                val = 1;
            }
        }
        return val;
    }

    private Color valueToColor(int val) {

        int max = colormode==0? colors.length:maxRainbow;

        if (val > max) {
            val = max;
        }
        if (val < 1) {
            val = 1;
        }

        // Index startet bei 0
        val--;

        if (colormode == 0) {
            return colors[val];
        } else {
            return new Color(Color.HSBtoRGB(1f / (float) maxRainbow * val, 1f, 1f));
        }
    }

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
