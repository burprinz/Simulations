package de.luiskun.apps;

import de.luiskun.Vektor;
import de.luiskun.apps.classes.Firework;
import de.luiskun.apps.classes.Particle;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Fireworks extends Application {

    List<Firework> fireworks;

    JSlider gravity, particlesAm, fading, impactS;
    double grav = 0.2, impact = 10;
    int amParticles = 100;
    int fadingSpeed = 4;

    public Fireworks() {
        super();

        fireworks = new ArrayList<>();

        int x = HEIGHT + 5;
        int y = 5;

        JLabel gravInfo = new JLabel("Gravity: " + grav);
        gravInfo.setBounds(x,y,190,20);
        gravInfo.setForeground(Color.WHITE);
        this.add(gravInfo);

        y += 25;

        gravity = new JSlider();
        gravity.setBounds(x, y, 190, 25);
        gravity.setMinimum(0);
        gravity.setMaximum(100);
        gravity.setValue((int)(grav*100));
        gravity.setBackground(Color.BLACK);
        gravity.addChangeListener(listener -> {
            grav = (double) gravity.getValue() / 100d;
            gravInfo.setText("Gravity: " + grav);
        });
        this.add(gravity);

        y += 30;

        JLabel partInfo = new JLabel("Particles: " + amParticles);
        partInfo.setBounds(x,y,190,20);
        partInfo.setForeground(Color.WHITE);
        this.add(partInfo);

        y += 25;

        particlesAm = new JSlider();
        particlesAm.setBounds(x, y, 190, 25);
        particlesAm.setMinimum(0);
        particlesAm.setMaximum(200);
        particlesAm.setValue(amParticles);
        particlesAm.setBackground(Color.BLACK);
        particlesAm.addChangeListener(listener -> {
            amParticles = particlesAm.getValue();
            partInfo.setText("Particles: " + amParticles);
        });
        this.add(particlesAm);

        y += 30;

        JLabel fadeInfo = new JLabel("Fading Speed: " + fadingSpeed);
        fadeInfo.setBounds(x,y,190,20);
        fadeInfo.setForeground(Color.WHITE);
        this.add(fadeInfo);

        y += 25;

        fading = new JSlider();
        fading.setBounds(x, y, 190, 25);
        fading.setMinimum(1);
        fading.setMaximum(30);
        fading.setValue(fadingSpeed);
        fading.setBackground(Color.BLACK);
        fading.addChangeListener(listener -> {
            fadingSpeed = fading.getValue();
            fadeInfo.setText("Fading Speed: " + fadingSpeed);
        });
        this.add(fading);

        y += 30;

        JLabel impactInfo = new JLabel("Explosion Impact: " + impact);
        impactInfo.setBounds(x,y,190,20);
        impactInfo.setForeground(Color.WHITE);
        this.add(impactInfo);

        y += 25;

        impactS = new JSlider();
        impactS.setBounds(x, y, 190, 25);
        impactS.setMinimum(0);
        impactS.setMaximum(500);
        impactS.setValue((int)(impact*10));
        impactS.setBackground(Color.BLACK);
        impactS.addChangeListener(listener -> {
            impact = (double) impactS.getValue()/10d;
            impactInfo.setText("Explosion Impact: " + impact);
        });
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
}
