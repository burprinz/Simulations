package de.luiskun.apps.sims;

import de.luiskun.apps.Application;
import de.luiskun.apps.sims.classes.StreetViewer.MapViewer;
import de.luiskun.tools.Vektor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.KeyEventDispatcher;
import java.awt.geom.RoundRectangle2D;

public class StreetViewer extends Application {

    private JLayeredPane layeredPane;
    private MapViewer mapViewer;
    private JPanel hudPanel;

    private volatile boolean wPressed;
    private volatile boolean aPressed;
    private volatile boolean sPressed;
    private volatile boolean dPressed;
    private volatile boolean handbrakePressed;

    private KeyEventDispatcher keyDispatcher;

    private double speed = 0.0;
    private double acceleration = 2.2;
    private double braking = 4.0;
    private double friction = 0.75;
    private double maxSpeed = 5.0;
    private double maxReverseSpeed = 2.0;

    private double steeringSpeed = 85.0;
    private double steeringInput = 0.0;
    private double steeringAngle = 0.0;

    private int currentGear = 1;

    private long lastUpdateTime;
    private long startTime;

    private final Color cyan = new Color(0, 235, 255);
    private final Color orange = new Color(255, 150, 45);
    private final Color panelBackground = new Color(8, 14, 24, 225);

    public StreetViewer() {
        super();

        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        layeredPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        layeredPane.setBounds(0, 0, WIDTH, HEIGHT);

        mapViewer = new MapViewer();
        mapViewer.setBounds(0, 0, WIDTH, HEIGHT);
        layeredPane.add(mapViewer, JLayeredPane.DEFAULT_LAYER);

        hudPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setRenderingHint(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                );

                int width = getWidth();
                int height = getHeight();

                drawCenterReticle(g2, width, height);
                drawSpeedometer(g2, width, height);
                drawTopBar(g2, width);
                drawSteeringIndicator(g2, width, height);
                drawStatus(g2, width, height);

                g2.dispose();
            }
        };

        hudPanel.setOpaque(false);
        hudPanel.setBounds(0, 0, WIDTH, HEIGHT);
        layeredPane.add(hudPanel, JLayeredPane.PALETTE_LAYER);

        add(layeredPane);

        installKeyboardControls();

        lastUpdateTime = System.nanoTime();
        startTime = lastUpdateTime;
    }

    private void installKeyboardControls() {
        keyDispatcher = new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                int code = e.getKeyCode();
                int id = e.getID();

                if (id != KeyEvent.KEY_PRESSED
                        && id != KeyEvent.KEY_RELEASED) {
                    return false;
                }

                boolean pressed = id == KeyEvent.KEY_PRESSED;

                switch (code) {
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        wPressed = pressed;
                        return true;

                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        sPressed = pressed;
                        return true;

                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        aPressed = pressed;
                        return true;

                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        dPressed = pressed;
                        return true;

                    case KeyEvent.VK_SPACE:
                        handbrakePressed = pressed;
                        return true;

                    default:
                        return false;
                }
            }
        };

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(keyDispatcher);
    }

    @Override
    public void update() {
        long now = System.nanoTime();

        double dt = (now - lastUpdateTime) / 1_000_000_000.0;
        lastUpdateTime = now;

        dt = Math.max(0.0, Math.min(dt, 0.04));

        updateVehicle(dt);

        mapViewer.update();
        hudPanel.repaint();
    }

    private void updateVehicle(double dt) {
        // Beschleunigung und Bremsen
        if (wPressed && !sPressed) {
            speed += acceleration * dt;
        } else if (sPressed && !wPressed) {
            if (speed > 0.05) {
                speed -= braking * dt;
            } else {
                speed -= acceleration * 0.7 * dt;
            }
        } else {
            if (speed > 0) {
                speed = Math.max(0, speed - friction * dt);
            } else if (speed < 0) {
                speed = Math.min(0, speed + friction * dt);
            }
        }

        speed = Math.max(
                -maxReverseSpeed,
                Math.min(maxSpeed, speed)
        );

        if (handbrakePressed) {
            speed *= Math.max(0.0, 1.0 - 2.5 * dt);
        }

        // Korrigierte Lenkung: A links, D rechts
        double targetSteering = 0.0;

        if (aPressed && !dPressed) {
            targetSteering = 1.0;
        } else if (dPressed && !aPressed) {
            targetSteering = -1.0;
        }

        steeringInput += (targetSteering - steeringInput)
                * Math.min(1.0, dt * 7.0);

        double speedFactor = Math.min(1.0, Math.abs(speed) / 1.2);
        steeringAngle = steeringInput * speedFactor;

        // Karte entsprechend der Lenkung drehen.
        double direction = speed >= 0.0 ? 1.0 : -1.0;

        double rotation = steeringAngle
                * steeringSpeed
                * direction
                * dt;

        mapViewer.rotate(rotation);

        // Vorwärts- und Rückwärtsbewegung
        if (Math.abs(speed) > 0.005) {
            Vektor movement = new Vektor();

            movement.x = 0.0;
            movement.y = -speed * dt * 60.0;

            mapViewer.moveMap(movement);
        }

        double absoluteSpeed = Math.abs(speed);

        currentGear = absoluteSpeed < 0.2
                ? 1
                : Math.min(
                6,
                1 + (int) (absoluteSpeed / (maxSpeed / 6.0))
        );
    }

    private void drawCenterReticle(Graphics2D g, int width, int height) {
        int cx = width / 2;
        int cy = height / 2;

        g.setColor(new Color(0, 0, 0, 80));
        g.fillOval(cx - 14, cy - 14, 28, 28);

        g.setColor(Color.WHITE);
        g.fillOval(cx - 5, cy - 5, 10, 10);

        g.setColor(cyan);
        g.fillOval(cx - 2, cy - 2, 4, 4);

        g.setStroke(new BasicStroke(2f));
        g.drawLine(cx - 20, cy, cx - 10, cy);
        g.drawLine(cx + 10, cy, cx + 20, cy);
        g.drawLine(cx, cy - 20, cx, cy - 10);
    }

    private void drawSpeedometer(Graphics2D g, int width, int height) {
        int panelWidth = 220;
        int panelHeight = 142;
        int x = 22;
        int y = height - panelHeight - 22;

        g.setColor(panelBackground);
        g.fill(new RoundRectangle2D.Double(
                x, y, panelWidth, panelHeight, 22, 22
        ));

        g.setColor(new Color(0, 235, 255, 100));
        g.setStroke(new BasicStroke(1.4f));
        g.draw(new RoundRectangle2D.Double(
                x, y, panelWidth, panelHeight, 22, 22
        ));

        g.setFont(new Font("Monospaced", Font.BOLD, 11));
        g.setColor(cyan);
        g.drawString("STREET // DRIVE SYSTEM", x + 14, y + 20);

        int speedValue = (int) Math.round(
                Math.abs(speed) / maxSpeed * 240.0
        );

        g.setFont(new Font("Monospaced", Font.BOLD, 44));
        g.setColor(Color.WHITE);
        g.drawString(String.format("%03d", speedValue), x + 14, y + 72);

        g.setFont(new Font("Monospaced", Font.BOLD, 12));
        g.setColor(orange);
        g.drawString("KM/H", x + 145, y + 70);

        int barX = x + 14;
        int barY = y + 88;
        int barWidth = panelWidth - 28;

        g.setColor(new Color(255, 255, 255, 25));
        g.fillRoundRect(barX, barY, barWidth, 7, 7, 7);

        int filled = (int) (
                barWidth * Math.min(1.0, Math.abs(speed) / maxSpeed)
        );

        g.setColor(speed < 0 ? orange : cyan);
        g.fillRoundRect(barX, barY, filled, 7, 7, 7);

        g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g.setColor(new Color(200, 220, 235));

        g.drawString(
                speed < -0.05 ? "REVERSE" : "GEAR " + currentGear,
                x + 14, y + 119
        );

        g.drawString(
                handbrakePressed ? "HANDBRAKE" : "TRACTION ON",
                x + 104, y + 119
        );
    }

    private void drawTopBar(Graphics2D g, int width) {
        int x = 22;
        int y = 20;
        int panelWidth = Math.min(width - 44, 410);
        int panelHeight = 66;

        g.setColor(panelBackground);
        g.fill(new RoundRectangle2D.Double(
                x, y, panelWidth, panelHeight, 18, 18
        ));

        g.setColor(new Color(0, 235, 255, 100));
        g.draw(new RoundRectangle2D.Double(
                x, y, panelWidth, panelHeight, 18, 18
        ));

        g.setFont(new Font("Monospaced", Font.BOLD, 17));
        g.setColor(Color.WHITE);
        g.drawString("STREET", x + 14, y + 25);

        g.setColor(cyan);
        g.drawString("RIDER", x + 87, y + 25);

        g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g.setColor(new Color(190, 210, 225));
        g.drawString("WASD / ARROWS  DRIVE", x + 14, y + 44);

        g.setColor(orange);
        g.drawString("SPACE  HANDBRAKE", x + 14, y + 56);

        int dotX = x + panelWidth - 25;
        int dotY = y + 20;

        g.setColor(new Color(60, 255, 150));
        g.fillOval(dotX, dotY, 7, 7);

        g.setFont(new Font("Monospaced", Font.PLAIN, 9));
        g.drawString("LIVE", dotX - 35, dotY + 7);
    }

    private void drawSteeringIndicator(
            Graphics2D g, int width, int height) {

        int x = width - 42;
        int y = height / 2;

        g.setColor(new Color(0, 0, 0, 110));
        g.fillRoundRect(x - 9, y - 65, 18, 130, 9, 9);

        g.setColor(new Color(255, 255, 255, 35));
        g.drawRoundRect(x - 9, y - 65, 18, 130, 9, 9);

        int markerY = y + (int) (steeringInput * 48);

        g.setColor(cyan);
        g.fillRoundRect(x - 5, markerY - 7, 10, 14, 5, 5);

        g.setColor(new Color(255, 255, 255, 130));
        g.drawLine(x - 3, y, x + 3, y);
    }

    private void drawStatus(Graphics2D g, int width, int height) {
        long elapsedSeconds =
                (System.nanoTime() - startTime) / 1_000_000_000L;

        String time = String.format(
                "SESSION %02d:%02d",
                elapsedSeconds / 60,
                elapsedSeconds % 60
        );

        g.setFont(new Font("Monospaced", Font.BOLD, 11));
        g.setColor(new Color(220, 235, 245, 210));

        FontMetrics fm = g.getFontMetrics();
        g.drawString(time, width - fm.stringWidth(time) - 22, 32);

        g.setColor(new Color(0, 0, 0, 100));
        g.fillRoundRect(
                width / 2 - 105,
                height - 31,
                210, 22, 11, 11
        );

        g.setColor(cyan);

        String status = handbrakePressed
                ? "DRIFT MODE"
                : Math.abs(speed) > 0.1
                ? "CRUISING"
                : "READY TO DRIVE";

        FontMetrics statusMetrics = g.getFontMetrics();

        g.drawString(
                status,
                width / 2 - statusMetrics.stringWidth(status) / 2,
                height - 16
        );
    }

    @Override
    public void paintComponent(Graphics g) {
    }
}