package de.luiskun.apps.games.centipede;

import de.luiskun.apps.games.Game;
import de.luiskun.apps.games.snake.Snake;
import de.luiskun.tools.Vektor;
import de.luiskun.ui.uiparts.Updatable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Enemy implements Updatable {

    List<Vektor> body;
    List<Vektor> directions;
    int speed = 3;
    CentipedeGame g;

    public Enemy(CentipedeGame g) {
        this.g = g;
        body = new ArrayList<>();
        directions = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            body.add(new Vektor(20 + i*Centipede.cellS, 0));
            directions.add(new Vektor(-speed, 0));
        }

    }

    public Enemy(List<Vektor> body, List<Vektor> directions, CentipedeGame g) {
        this.g = g;
        this.body = body;
        this.directions = directions;
    }

    @Override
    public void update() {
        for (int i = 0; i < body.size(); i++) {
            body.get(i).add(directions.get(i));

            if (body.get(i).xAsInt() < 0) {
                body.set(i, new Vektor(0, body.get(i).yAsInt() + 5));

                if (body.get(i).yAsInt() % Centipede.cellS == 0) {
                    // Halben schritt mitte
                    directions.get(i).mult(-1);
                }
            }
            if (body.get(i).xAsInt() + Centipede.cellS > Game.HEIGHT) {
                body.set(i, new Vektor(Game.HEIGHT - Centipede.cellS, body.get(i).yAsInt() + 5));
                if (body.get(i).yAsInt() % Centipede.cellS == 0) {
                    // Halben schritt mitte
                    directions.get(i).mult(-1);
                }
            }

            Vektor gridPos = new Vektor(body.get(i));
            gridPos.add(new Vektor(15, 0));
            gridPos.divide(Centipede.cellS);

            if (directions.get(i).xAsInt() < 0) {
                if (gridPos.xAsInt() - 1 >= 0) {
                    if (g.mushroomGrid[gridPos.xAsInt()-1][gridPos.yAsInt()] != null) {
                        // Pilz links
                        if (body.get(i).xAsInt() < gridPos.xAsInt()*Centipede.cellS) {
                            body.set(i, new Vektor(gridPos.xAsInt()*Centipede.cellS, body.get(i).yAsInt() + 5));
                            if (body.get(i).yAsInt() % Centipede.cellS == 0) {
                                // Halben schritt mitte
                                directions.get(i).mult(-1);
                            }
                        }
                    }
                }
            } else {
                if (gridPos.xAsInt() + 1 < Centipede.gridS) {
                    if (g.mushroomGrid[gridPos.xAsInt()+1][gridPos.yAsInt()] != null) {
                        // Pilz rechts
                        if (body.get(i).xAsInt() + Centipede.cellS > gridPos.xAsInt()*Centipede.cellS) {
                            body.set(i, new Vektor(gridPos.xAsInt()*Centipede.cellS, body.get(i).yAsInt() + 5));
                            if (body.get(i).yAsInt() % Centipede.cellS == 0) {
                                // Halben schritt mitte
                                directions.get(i).mult(-1);
                            }
                        }
                    }
                }
            }





        }
    }

    public Enemy split(int pos) {
        List<Vektor> oldb = new ArrayList<>();
        List<Vektor> newb = new ArrayList<>();
        List<Vektor> oldd = new ArrayList<>();
        List<Vektor> newd = new ArrayList<>();


        for (int i = 0; i < body.size(); i++) {
            if (i < pos) {
                newb.add(body.get(i));
                newd.add(directions.get(i));
            } else if (i > pos) {
                oldb.add(body.get(i));
                oldd.add(directions.get(i));
            }
        }

        this.body = oldb;
        this.directions = oldd;

        return new Enemy(newb, newd, g);
    }


    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.green);
        for (Vektor v : body) {
            g2.fillOval(v.xAsInt(), v.yAsInt(), Centipede.cellS, Centipede.cellS);
        }
    }
}
