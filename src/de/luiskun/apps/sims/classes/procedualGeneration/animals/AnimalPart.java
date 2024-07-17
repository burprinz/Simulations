package de.luiskun.apps.sims.classes.procedualGeneration.animals;

import de.luiskun.tools.Vektor;

public class AnimalPart {

    public static final int NOTHING = 0;
    public static final int LEGS = 1;

    public int specialPart = NOTHING;

    //Legs
    private double maxAngleOffset = Math.PI/7;
    public double rightAngle = Math.random()*(maxAngleOffset*2)-maxAngleOffset;
    public double leftAngle = Math.random()*(maxAngleOffset*2)-maxAngleOffset;
    private double rightStep = 0.03;
    private double leftStep = 0.03;

    public Vektor pos = null;
    public double size = 0;
    public double angle = 0;
    public AnimalPart head = null;
    public AnimalPart tail = null;
    public Vektor leftSide, rightSide;

    public AnimalPart(Vektor pos, double size, AnimalPart head, AnimalPart tail, double angle) {
        this.pos = pos;
        this.size = size;
        this.head = head;
        this.tail = tail;
        this.angle = angle;
    }

    public void updateSides() {
        leftSide = new Vektor(pos)
                .add(Vektor.createFromAngle(angle+Math.PI/2).mult(size/2));
        rightSide = new Vektor(pos)
                .add(Vektor.createFromAngle(angle-Math.PI/2).mult(size/2));

        leftAngle += leftStep;
        if (leftAngle > maxAngleOffset) {
            leftAngle = maxAngleOffset;
            leftStep *= -1;
        }
        if (leftAngle < -maxAngleOffset) {
            leftAngle = -maxAngleOffset;
            leftStep *= -1;
        }

        rightAngle += rightStep;
        if (rightAngle > maxAngleOffset) {
            rightAngle = maxAngleOffset;
            rightStep *= -1;
        }
        if (rightAngle < -maxAngleOffset) {
            rightAngle = -maxAngleOffset;
            rightStep *= -1;
        }

    }

    public static AnimalPart getHead(AnimalPart bodyPart) {

        AnimalPart head = bodyPart;

        while (head.head != null) {
            head = head.head;
        }

        return head;
    }
}
