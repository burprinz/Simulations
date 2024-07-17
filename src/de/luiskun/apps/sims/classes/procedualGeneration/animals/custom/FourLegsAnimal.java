package de.luiskun.apps.sims.classes.procedualGeneration.animals.custom;

import de.luiskun.apps.sims.classes.procedualGeneration.animals.Animal;
import de.luiskun.apps.sims.classes.procedualGeneration.animals.AnimalPart;
import de.luiskun.tools.Vektor;

public class FourLegsAnimal extends Animal {
    public FourLegsAnimal(double bodyThickness, Vektor startPos) {
        super(18, bodyThickness);

        int amountHead = 3;
        AnimalPart head = createHead(bodyThickness - bodyThickness/5, bodyThickness+bodyThickness/5, amountHead, null, startPos);
        this.head = AnimalPart.getHead(head);


        head = sameSize(bodyThickness, 1, head);
        head = sameSize(bodyThickness+bodyThickness/5, 1, head);
        head.specialPart = AnimalPart.LEGS;
        head = sameSize(bodyThickness, 2, head);
        head = sameSize(bodyThickness+bodyThickness/5, 1, head);
        head.specialPart = AnimalPart.LEGS;
        head = sameSize(bodyThickness, 1, head);

        int placed = 9;

        head = decreasing(bodyThickness-bodyThickness/4, 1, length-placed, head);

        /*
        double legOffset = bodyThickness / 5;

        int headEnd = length/5;
        int tailStart = length - (length/5);
        int legGap = length/10;
        int firstLegStart = headEnd + 2;

        double headStartSize = bodyThickness - (bodyThickness/5);
        double headEndSize = bodyThickness + (bodyThickness/5);

        AnimalPart head = new AnimalPart(startPos, headStartSize, null, null, Math.PI);
        this.head = head;

        head = createHead(headStartSize, headEndSize, headEnd-1, head);

        head = sameSize(bodyThickness, firstLegStart-headEnd, head);

        head = sameSize(bodyThickness + legOffset, legGap, head);

        head = sameSize(bodyThickness, (tailStart-legGap) - (firstLegStart+legGap), head);

        head = sameSize(bodyThickness + legOffset, legGap, head);

        head = decreasing(bodyThickness, 1, length-tailStart, head);

         */
    }

    private AnimalPart createHead(double startSize, double endSize, int amountPoints, AnimalPart head, Vektor startPos) {

        double sizeDifference = endSize - startSize;
        double sizeStep = sizeDifference / amountPoints;

        for (int i = 0; i < amountPoints; i++) {

            if (head == null) {
                AnimalPart nextPart = new AnimalPart(startPos,
                        startSize + i*sizeStep,
                        null,
                        null,
                        Math.PI);
                head = nextPart;
                continue;
            }

            AnimalPart nextPart = new AnimalPart(new Vektor(head.pos).add(
                    new Vektor(100, 0)),
                    startSize + i*sizeStep,
                    head,
                    null,
                    Math.PI);

            head.tail = nextPart;
            head = nextPart;
        }

        return head;
    }

    private AnimalPart sameSize(double size, double amountPoints, AnimalPart head) {

        for (int i = 0; i < amountPoints; i++) {

            AnimalPart nextPart = new AnimalPart(
                    new Vektor(head.pos).add(new Vektor(100, 0)),
                    size,
                    head,
                    null,
                    Math.PI);

            head.tail = nextPart;
            head = nextPart;
        }

        return head;
    }

    private AnimalPart decreasing(double start, double end, int amount, AnimalPart head) {

        double delta = (start-end)/amount;

        for (int i = 0; i < amount; i++) {
            AnimalPart nextPart = new AnimalPart(new Vektor(head.pos).add(
                    new Vektor(100, 0)),
                    start - i*delta,
                    head,
                    null,
                    Math.PI);

            head.tail = nextPart;
            head = nextPart;
        }

        return head;
    }
}
