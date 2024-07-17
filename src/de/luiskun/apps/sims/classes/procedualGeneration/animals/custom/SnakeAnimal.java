package de.luiskun.apps.sims.classes.procedualGeneration.animals.custom;

import de.luiskun.apps.sims.classes.procedualGeneration.animals.Animal;
import de.luiskun.apps.sims.classes.procedualGeneration.animals.AnimalPart;
import de.luiskun.tools.Vektor;

public class SnakeAnimal extends Animal {

    Vektor initialStartPos;

    public SnakeAnimal(int length, double headSize, double tailSize, int shrinkspeed, int distance, Vektor startPos) {
        super(length, headSize);

        double sizeDifference = headSize - tailSize;
        double shrinkStep = sizeDifference * (1d / (double) length / (double) shrinkspeed);

        AnimalPart head = new AnimalPart(startPos, headSize, null, null, Math.PI);
        this.head = head;



        initialStartPos = startPos;

        int count = 1;
        int shrinkCount = 0;

        while (count < length) {

            if (count % shrinkspeed == 0) {
                shrinkCount++;
            }

            Vektor pos = new Vektor(startPos);
            pos.add(new Vektor(count*distance, 0));

            AnimalPart nextPart = new AnimalPart(pos, headSize - shrinkCount*shrinkStep, head, null, Math.PI);
            initialStartPos = startPos;
            head.tail = nextPart;


            head = nextPart;
            count++;
        }
    }

}
