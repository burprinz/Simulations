package de.luiskun.apps;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mandelbrot extends Application {

    public static final int WIDTH = 700;
    public static final int HEIGHT = 700;
    public static final int CENTERX = WIDTH / 2;
    public static final int CENTERY = HEIGHT / 2;

    private static final Color[] colors = {
            new Color(255, 0, 0),
            new Color(255,121,1),
            new Color(255,255,11),
            new Color(34,219,19),
            new Color(36,48,255),
            new Color(102,0,146),
            new Color(100,0,249),
    };

    private static final Color[] colors2 =  {
            new Color(10, 17, 36),
            new Color(18,34,59),
            new Color(14,59,80),
            new Color(182,81,13),
            new Color(205,110,16),
            new Color(233,134,7)
    };

    private static final Color[] colors3 =  {
            new Color(217, 237, 146),
            new Color(181, 228, 140),
            new Color(153, 217, 140),
            new Color(118, 200, 147),
            new Color(82, 182, 154),
            new Color(52, 160, 164),
            new Color(22, 138, 173),
            new Color(26, 117, 159),
            new Color(30, 96, 145),
            new Color(24, 78, 119)
    };

    private static final Color[] colors4 =  {
            new Color(45, 0, 247),
            new Color(106, 0, 244),
            new Color(137, 0, 242),
            new Color(161,0,242),
            new Color(177,0,232),
            new Color(188,0,221),
            new Color(209,0,209),
            new Color(219, 0,182),
            new Color(229,0,164),
            new Color(242,0,137)
    };

    int currentPalette = 0;

    private double scale = 125d;
    private double scaleFactor = 1d/scale;

    private double originR = -0.5;//-0.128;
    private double originI = 0.3;//0.987;

    private static volatile boolean wPressed = false;
    private static volatile boolean sPressed = false;
    private static volatile boolean aPressed = false;
    private static volatile boolean dPressed = false;
    private static volatile boolean ePressed = false;
    private static volatile boolean qPressed = false;

    String performanceText = "Mode On";

    boolean performanceMode = true;

    MouseHandler mh = new MouseHandler();

    public Mandelbrot() {
        this.setPreferredSize(new Dimension(WIDTH + 200, HEIGHT));
        this.setDoubleBuffered(true);
        this.setLayout(null);
        this.addMouseListener(mh);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (Mandelbrot.class) {
                    switch (ke.getID()) {
                        case KeyEvent.KEY_PRESSED:
                            if (ke.getKeyCode() == KeyEvent.VK_W) {
                                wPressed = true;
                            } else if(ke.getKeyCode() == KeyEvent.VK_S) {
                                sPressed = true;
                            } else if(ke.getKeyCode() == KeyEvent.VK_A) {
                                aPressed = true;
                            } else if(ke.getKeyCode() == KeyEvent.VK_D) {
                                dPressed = true;
                            } else if(ke.getKeyCode() == KeyEvent.VK_E) {
                                ePressed = true;
                            } else if(ke.getKeyCode() == KeyEvent.VK_Q) {
                                qPressed = true;
                            }else if(ke.getKeyCode() == KeyEvent.VK_0) {
                                currentPalette = 0;
                                repaint();
                            } else if(ke.getKeyCode() == KeyEvent.VK_1) {
                                currentPalette = 1;
                                repaint();
                            } else if(ke.getKeyCode() == KeyEvent.VK_2) {
                                currentPalette = 2;
                                repaint();
                            }else if(ke.getKeyCode() == KeyEvent.VK_3) {
                                currentPalette = 3;
                                repaint();
                            } else if(ke.getKeyCode() == KeyEvent.VK_4) {
                                currentPalette = 4;
                                repaint();
                            }else if(ke.getKeyCode() == KeyEvent.VK_P) {
                                performanceMode = !performanceMode;
                                if (performanceMode) {
                                    performanceText = "Mode Off";
                                } else {
                                    performanceText = "Mode On";
                                }
                                repaint();
                            }
                            break;

                        case KeyEvent.KEY_RELEASED:
                            if (ke.getKeyCode() == KeyEvent.VK_W) {
                                wPressed = false;
                            } else if(ke.getKeyCode() == KeyEvent.VK_S) {
                                sPressed = false;
                            } else if(ke.getKeyCode() == KeyEvent.VK_A) {
                                aPressed = false;
                            } else if(ke.getKeyCode() == KeyEvent.VK_D) {
                                dPressed = false;
                            } else if(ke.getKeyCode() == KeyEvent.VK_E) {
                                ePressed = false;
                            } else if(ke.getKeyCode() == KeyEvent.VK_Q) {
                                qPressed = false;
                            }
                            break;
                    }
                    return false;
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0,0,WIDTH+200,HEIGHT);
        if(!performanceMode) {
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    updatePoint(x, y, g2);
                }
            }
        } else {
            for (int x = 150; x < WIDTH-150; x++) {
                for (int y = 150; y < HEIGHT-150; y++) {
                    updatePoint(x, y, g2);
                }
            }
        }


        g2.setColor(Color.BLACK);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20));
        g2.drawString("Zoom Faktor: " + scaleFactor, 5, 20);


        g2.setColor(Color.WHITE);
        int x = WIDTH + 10;
        int y = 20;
        g2.drawString("WASD = Move", x, y);
        y += 25;
        g2.drawString("E = Zoom in", x, y);
        y += 25;
        g2.drawString("Q = Zoom out", x, y);
        y += 25;
        g2.drawString("Left Click = Change", x, y);
        y += 25;
        g2.drawString("                   center", x, y);
        y += 25;
        g2.drawString("0-4 = Colors", x, y);
        y += 25;
        g2.drawString("p = Performance", x, y);
        y += 25;
        g2.drawString("      " + performanceText, x, y);
        y += 25;
        g2.drawString("Z = Quit", x, y);


    }

    public void update() {
        if (ePressed) {
            scaleFactor /= 1.1;
        } else if (qPressed) {
            scaleFactor *= 1.1;
        }
        double step = 10 * scaleFactor;
        if(wPressed) {
            originI += step;
        }
        if(aPressed) {
            originR -= step;
        }
        if(sPressed) {
            originI -= step;
        }
        if(dPressed) {
            originR += step;
        }

    }

    private void updatePoint(double x, double y, Graphics2D g2) {

        double[] pos = screenToWorld(x, y);
        double cr = pos[0];
        double ci = pos[1];

        double zr = 0;
        double zi = 0;

        for (int i = 0; i < 100; i++) {
            double _zr = zr * zr - zi * zi + cr;
            double _zi = 2 * zr * zi + ci;
            zr = _zr;
            zi = _zi;
            double dist = Math.sqrt(zr * zr + zi * zi);
            if (dist > 2) {
                Color c;
                switch(currentPalette) {
                    case 0:
                        c = colors[i % colors.length];
                        break;
                    case 1:
                        c = new Color(Color.HSBtoRGB((float)1 / (float)100 * i, 1f, 1f));
                        break;
                    case 2:
                        c = colors2[i % colors2.length];
                        break;
                    case 3:
                        c = colors3[i % colors3.length];
                        break;
                    case 4:
                        c = colors4[i % colors4.length];
                        break;
                    default:
                        // Standart
                        c = colors[i % colors.length];
                        break;
                }
                g2.setColor(c);
                g2.fillRect((int) x, (int) y, 1, 1);
                return;
            }
        }

    }

    private double[] screenToWorld(double x, double y) {
        double r = originR + (x - CENTERX) * scaleFactor;
        double i = originI - (y - CENTERY) * scaleFactor;
        return new double[] {r, i};
    }

    private void zoom() {
        Point b = getMousePosition();
        if (b != null) {
            double[] worldPos = screenToWorld(b.getX(), b.getY());
            originR = worldPos[0];
            originI = worldPos[1];
            this.repaint();
        }
    }

    private class MouseHandler implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            zoom();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }


}
