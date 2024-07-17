package de.luiskun.apps.sims.classes.procedualGeneration;

import de.luiskun.apps.sims.classes.procedualGeneration.animals.AnimalPart;

import java.util.ArrayList;
import java.util.List;

public class MovingSequence {

    List<MovingInstruction> instructions;
    int currentInstruction = 0;
    boolean withWobbling = false;

    public static MovingSequence generateSimpleCornerSequence() {

        MovingSequence sequence = new MovingSequence();

        sequence.withWobbling = true;

        List<MovingInstruction> instructions = new ArrayList<MovingInstruction>();

        instructions.add(new MovingInstruction(MovingInstruction.MOVE_FORWARD, 650, 2, 0));
        instructions.add(new MovingInstruction(MovingInstruction.TURN_LEFT, Math.PI/2, 2, Math.PI/96));
        instructions.add(new MovingInstruction(MovingInstruction.MOVE_FORWARD, 300, 2, 0));
        instructions.add(new MovingInstruction(MovingInstruction.TURN_LEFT, Math.PI/2, 2, Math.PI/96));
        instructions.add(new MovingInstruction(MovingInstruction.MOVE_FORWARD, 650, 2, 0));
        instructions.add(new MovingInstruction(MovingInstruction.TURN_LEFT, Math.PI/2, 2, Math.PI/96));
        instructions.add(new MovingInstruction(MovingInstruction.MOVE_FORWARD, 300, 2, 0));
        instructions.add(new MovingInstruction(MovingInstruction.TURN_LEFT, Math.PI/2, 2, Math.PI/96));


        sequence.instructions = instructions;

        return sequence;
    }

    public MovingSequence() {

    }

    public void nextStep(AnimalPart head) {
        if (currentInstruction >= instructions.size()) {
            currentInstruction = 0;
            return;
        }


        if (instructions.get(currentInstruction).nextStep(head)) {
            instructions.get(currentInstruction).reset();
            currentInstruction++;
        }

        if (withWobbling) {
            wobbleEffect(head);
        }
    }




    int off = 40;
    int cur = 0;
    int speed = 1;

    private void wobbleEffect(AnimalPart head) {

        double angle = (speed<0?-1:1)*Math.PI/2;
        angle *= 0.01;

        head.angle += angle;

        cur += speed;
        if (cur > off || cur < -off) {
            cur = cur>off?off:-off;
            speed = -speed;
        }

    }

}
