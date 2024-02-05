package de.luiskun.apps;

import de.luiskun.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class FallingSand extends Application{

    public static final int WIDTH = 900;
    public static final int HEIGHT = 700;

    private int w = 8;
    public int cols, rows;
    public float[][] grid;

    private float[] currentColors;

    int spawnRadius = 1;
    int expRadius = 1;

    private int colormode;

    private JSlider fieldSize, spawnRadiusS;
    private JComboBox colorSelector;
    private JButton resetButton;

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

    private void initSettings() {
        int x = 715;
        int y = 15;
        JLabel fSizeInfo = new JLabel("Sand Size: 8");
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
            fSizeInfo.setText("Sand Size: " + val);
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

        y += 30;

        String[] palletes = FileHandler.getPaletteNames();

        String[] goodPallettes = new String[palletes.length - 2];
        int c = 0;
        for(int i = 0; i < palletes.length; i++) {
            if (!palletes[i].equals("rainbow") && !palletes[i].equals("darkyellow")) {
                goodPallettes[c] = palletes[i];
                c++;
            }
        }

        colorSelector = new JComboBox(goodPallettes);
        colorSelector.setBounds(x, y, 180, 30);
        colorSelector.addActionListener(listener -> {
            currentColors = FileHandler.loadHuePalette(colorSelector.getSelectedItem().toString());
        });
        currentColors = FileHandler.loadHuePalette(colorSelector.getSelectedItem().toString());
        this.add(colorSelector);

        y += 35;

        resetButton = new JButton("Reset Canvas");
        resetButton.setBounds(x, y, 180, 30);
        resetButton.addActionListener(listener -> {
            resetGrid();
        });
        this.add(resetButton);

    }

    private void changeSandSize(int num) {
        w = num;
        resetGrid();
    }

    private void resetGrid() {
        cols = HEIGHT / w;
        rows = HEIGHT / w;
        grid = new float[cols][rows];
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        if (currentColors != null) {
            for (int x = 0; x < cols; x++) {
                for (int y = 0; y < rows; y++) {
                    if (grid[x][y] > 0) {
                        g2.setColor(valueToColor(grid[x][y]));
                        g2.fillRect(x * w, y * w, w, w);
                    }
                }
            }
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 17));
        g2.setColor(Color.WHITE);
        int x = 710;
        int y = 25;
        /*
        g2.drawString("Left Mouse = Spawn", x, y);
        y += 30;
        g2.drawString("Backspace = Back to", x, y);
        y += 25;
        g2.drawString("Z = Back", x, y);
         */
    }

    public void update() {


        float[][] nextGrid = new float[cols][rows];

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                float state = grid[x][y];
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

        if (mh.pressed && currentColors != null) {
            Point b = this.getMousePosition();

            if (b != null) {
                int mCol = (int) Math.floor(b.getX() / w);
                int mRow = (int) Math.floor(b.getY() / w);

                if(mCol >= 0 && mCol < cols && mRow >= 0 && mRow < rows) {
                    float val = getCurrentColor();
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

        grid = nextGrid;

    }


    int colorUpdateCount;
    int val = 0;
    int maxRainbow = 100;

    private float getCurrentColor() {
        int maxVal;
        int updateBorder;
        maxVal = currentColors.length;

        // Borderbedingungen berechnen
        if (currentColors.length < 15) {
            updateBorder = 70;
        } else if (currentColors.length < 100) {
            updateBorder = 30;
        } else {
            updateBorder = 1;
        }

        // Farbenposition updaten, wenn noetig
        colorUpdateCount++;
        if (colorUpdateCount >= updateBorder) {
            colorUpdateCount = 0;
            val++;
            if (val >= maxVal) {
                val = 0;
            }
        }
        return valueToColor(val);
    }

    private float valueToColor(int val) {

        int max = colormode==0? currentColors.length:maxRainbow;

        if (val > max) {
            val = max;
        }
        if (val < 1) {
            val = 1;
        }

        // Index startet bei 0
        val--;

        return currentColors[val];
    }

    private Color valueToColor(float val) {
        return new Color(Color.HSBtoRGB(val, 1f, 1f));
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
