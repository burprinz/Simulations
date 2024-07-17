package de.luiskun.apps.sims.classes.procedualGeneration;

import de.luiskun.apps.sims.classes.procedualGeneration.animals.AnimalPart;
import de.luiskun.tools.Vektor;

public class MovingInstruction {

    public static final int MOVE_FORWARD = 0;
    public static final int TURN_LEFT = 1;
    public static final int TURN_RIGHT = 2;


    int type;

    double maxProgress;
    double progress;
    double moveSpeed;
    double angleSpeed;

    public MovingInstruction(int type, double maxProgress, double moveSpeed, double angleSpeed) {
        this.type = type;
        this.progress = 0;
        this.maxProgress = maxProgress;
        this.moveSpeed = moveSpeed;
        this.angleSpeed = angleSpeed;
    }

    public void reset() {
        progress = 0;
    }

    /**
     *
     * @return true, wenn finished
     */
    public boolean nextStep(AnimalPart head) {
        if (progress >= maxProgress) {
            return true;
        }

        switch (type) {
            case MOVE_FORWARD:
                moveForward(head);
                break;
            case TURN_LEFT:
                turnLeft(head);
                break;
        }

        return false;
    }

    private void moveForward(AnimalPart head) {

        Vektor dir = Vektor.createFromAngle(head.angle);
        dir.mult(moveSpeed);

        head.pos.add(dir);

        progress+=moveSpeed;
    }

    private void turnLeft(AnimalPart head) {
        double nextProgress = progress+angleSpeed;
        double speed = angleSpeed;
        if (nextProgress > maxProgress) {
            speed = maxProgress - nextProgress;
            nextProgress = maxProgress;
        }

        head.angle -= speed;
        Vektor dir = Vektor.createFromAngle(head.angle);
        dir.mult(moveSpeed);
        head.pos.add(dir);


        progress+=angleSpeed;
    }

}
