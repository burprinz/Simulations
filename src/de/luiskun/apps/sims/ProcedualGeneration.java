package de.luiskun.apps.sims;

import de.luiskun.apps.Application;
import de.luiskun.apps.sims.classes.procedualGeneration.animals.Animal;
import de.luiskun.apps.sims.classes.procedualGeneration.animals.AnimalBodyPartGenerator;
import de.luiskun.apps.sims.classes.procedualGeneration.MovingSequence;
import de.luiskun.apps.sims.classes.procedualGeneration.PolygonInfos;
import de.luiskun.apps.sims.classes.procedualGeneration.animals.custom.FourLegsAnimal;
import de.luiskun.tools.Vektor;

import java.awt.*;

public class ProcedualGeneration extends Application {

    Animal currentAnimal;
    MovingSequence seq;


    public ProcedualGeneration() {
        //currentAnimal = new SnakeAnimal(50, 50, 1, 1, 100, new Vektor(WIDTH-100, 100));
       // currentAnimal = new FourLegsAnimal(200, 100, new Vektor(100, Screen.HEIGHT/2));
        currentAnimal = new FourLegsAnimal(50, new Vektor(WIDTH-100, HEIGHT/2));
        seq = MovingSequence.generateSimpleCornerSequence();
    }


    @Override
    public void update() {
        if (currentAnimal != null) {
           seq.nextStep(currentAnimal.head);
            currentAnimal.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        g2.setColor(Color.WHITE);
        currentAnimal.draw(g2);

    }

}
