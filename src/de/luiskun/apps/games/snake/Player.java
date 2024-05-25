package de.luiskun.apps.games.snake;

import de.luiskun.apps.Application;
import de.luiskun.apps.games.Game;
import de.luiskun.tools.FileHandler;
import de.luiskun.tools.Geometry;
import de.luiskun.tools.Vektor;
import de.luiskun.ui.Screen;
import de.luiskun.ui.uiparts.Updatable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Player implements Updatable {

    final Color bodyC = new Color(76, 159, 0);
    final int maxSpeed = (int)(Snake.cellSize*0.057142857142857f);
    final int startSpeed = 35;
    int pos;

    public List<Vektor> body;
    public Vektor dir;
    boolean ate = false;
    public boolean dirChanged = false;
    Vektor headDir;
    Snake s;
    BufferedImage orHead;
    BufferedImage head;

    public int score = 0;

    public Player(Snake s) {
        this.s = s;
        dir = new Vektor(0,0);

        head = FileHandler.SetupImage("res/games/snake/head.png", Snake.cellSize, Snake.cellSize);
        orHead = head;

        body = new ArrayList<>();
        body.add(new Vektor(1, 0));
        body.add(new Vektor(0, 0));

        head = Geometry.rotate(orHead, 90);
        headDir = new Vektor(1,0);

    }

    public void grow() {
        body.add(new Vektor(body.get(body.size()-1)));
        ate = true;
        score++;
    }

    public boolean isFree(Vektor pos) {
        for (Vektor v : body) {
            if (Vektor.isSameInt(pos, v)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void update() {
        pos++;
        int currentSpeed = (int)(60-startSpeed - body.size()*0.3f);
        currentSpeed = currentSpeed<maxSpeed?maxSpeed:currentSpeed;

        if (pos > currentSpeed) {
            pos = 0;

            if (!Vektor.isSameInt(dir, new Vektor(0,0))) {
                Vektor lastPos = new Vektor(body.get(0));
                body.get(0).add(dir);

                if(body.get(0).xAsInt() < 0) {
                    body.get(0).x = Snake.gridS-1;
                }
                if(body.get(0).yAsInt() < 0) {
                    body.get(0).y = Snake.gridS-1;

                }
                if(body.get(0).xAsInt() >= Snake.gridS) {
                    body.get(0).x = 0;
                }
                if(body.get(0).yAsInt() >= Snake.gridS) {
                    body.get(0).y = 0;
                }

                for (int i = 1; i < body.size(); i++) {
                    Vektor v = body.get(i);
                    if (Vektor.isSameInt(body.get(0), v)) {
                        s.gameOver(score);
                    }
                }

                for (int i = 1; i < body.size()-1; i++) {
                    Vektor tmp = new Vektor(body.get(i));
                    body.set(i, new Vektor(lastPos));
                    lastPos = tmp;
                }

                if (!ate) {
                    body.set(body.size()-1, new Vektor(lastPos));
                } else {
                    ate = false;
                }
            }
            updateImageRotation();
        }
    }

    private void updateImageRotation() {
        if (!Vektor.isSameInt(headDir, dir)) {
            headDir = dir;
            if (Vektor.isSameInt(headDir, new Vektor(0, 1))) {
                head = Geometry.rotate(orHead, 180);
            } else if (Vektor.isSameInt(headDir, new Vektor(0, -1))) {
                head = orHead;
            } else if (Vektor.isSameInt(headDir, new Vektor(1, 0))) {
                head = Geometry.rotate(orHead, 90);
            } else if (Vektor.isSameInt(headDir, new Vektor(-1, 0))) {
                head = Geometry.rotate(orHead, 270);
            }
        }
    }


    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(bodyC);
        for (int i = 1; i < body.size(); i++) {
            Vektor v = body.get(i);
            g2.fillRect(v.xAsInt()*Snake.cellSize, v.yAsInt()*Snake.cellSize, Snake.cellSize, Snake.cellSize);
        }
        g2.drawImage(head, body.get(0).xAsInt()*Snake.cellSize, body.get(0).yAsInt()*Snake.cellSize, null);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30));
        g2.setColor(Color.WHITE);
        int x = Game.HEIGHT + 10;
        int y = 50;
        String text = "Score: " + score;
        g2.drawString(text, x, y);
    }
}
