package de.luiskun.apps.sims;

import de.luiskun.apps.sims.classes.Firework;
import de.luiskun.ui.uiparts.RangePicker;
import de.luiskun.ui.uiparts.RangeSelection;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Fireworks extends Application implements RangeSelection {

    List<Firework> fireworks;

    RangePicker gravity, particlesAm, fading, impactS;
    JLabel gravInfo, impactInfo, partInfo, fadeInfo;
    double grav = 0.2, impact = 10;
    int amParticles = 100;
    int fadingSpeed = 4;

    public Fireworks() {
        super();

        fireworks = new ArrayList<>();

        int x = HEIGHT + 5;
        int y = 5;

        gravInfo = new JLabel("Gravity: " + grav);
        gravInfo.setBounds(x,y,190,20);
        gravInfo.setForeground(Color.WHITE);
        this.add(gravInfo);

        y += 25;

        gravity = new RangePicker(this, 0, 100, (int)(grav*100), "gr");
        gravity.setBounds(x, y, 190, 25);
        this.add(gravity);

        y += 30;

        partInfo = new JLabel("Particles: " + amParticles);
        partInfo.setBounds(x,y,190,20);
        partInfo.setForeground(Color.WHITE);
        this.add(partInfo);

        y += 25;

        particlesAm = new RangePicker(this, 0, 200, amParticles, "amP");
        particlesAm.setBounds(x, y, 190, 25);
        this.add(particlesAm);

        y += 30;

        fadeInfo = new JLabel("Fading Speed: " + fadingSpeed);
        fadeInfo.setBounds(x,y,190,20);
        fadeInfo.setForeground(Color.WHITE);
        this.add(fadeInfo);

        y += 25;

        fading = new RangePicker(this, 1, 30, fadingSpeed, "fad");
        fading.setBounds(x, y, 190, 25);
        this.add(fading);

        y += 30;

        impactInfo = new JLabel("Explosion Impact: " + impact);
        impactInfo.setBounds(x,y,190,20);
        impactInfo.setForeground(Color.WHITE);
        this.add(impactInfo);

        y += 25;

        impactS = new RangePicker(this, 0, 500, (int)(impact*10), "imp");
        impactS.setBounds(x, y, 190, 25);
        this.add(impactS);

    }

    Random r = new Random();

    @Override
    public void update() {

        if(r.nextDouble() < 0.1) {
            fireworks.add(new Firework(grav, amParticles, fadingSpeed, impact));
        }

        for(int i = fireworks.size()-1; i >= 0; i--) {
            if(fireworks.get(i).done()) {
                fireworks.remove(i);
            } else {
                fireworks.get(i).update();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(0,0,0,50));
        g2.fillRect(0,0,HEIGHT,HEIGHT);

        g2.setColor(Color.BLACK);
        g2.fillRect(HEIGHT, 0, 200, HEIGHT);

        g2.setColor(Color.WHITE);

        for(int i = 0; i < fireworks.size(); i++) {
            fireworks.get(i).show(g2);
        }

    }

    @Override
    public void rangeChanged(String com, int am) {
        switch (com) {
            case "gr":
                grav = (double) gravity.getValue() / 100d;
                gravInfo.setText("Gravity: " + grav);
                break;
            case "amP":
                amParticles = particlesAm.getValue();
                partInfo.setText("Particles: " + amParticles);
                break;
            case "fad":
                fadingSpeed = fading.getValue();
                fadeInfo.setText("Fading Speed: " + fadingSpeed);
                break;
            case "imp":
                impact = (double) impactS.getValue()/10d;
                impactInfo.setText("Explosion Impact: " + impact);
                break;
        }
    }
}
