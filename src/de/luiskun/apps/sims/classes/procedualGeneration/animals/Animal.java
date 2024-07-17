package de.luiskun.apps.sims.classes.procedualGeneration.animals;

import de.luiskun.apps.sims.classes.procedualGeneration.MovingSequence;
import de.luiskun.apps.sims.classes.procedualGeneration.PolygonInfos;
import de.luiskun.tools.Vektor;
import de.luiskun.ui.uiparts.Updatable;

import java.awt.*;

public class Animal implements Updatable {

    public int length;
    public double bodysize;

    public AnimalPart head;
    Vektor frontPos;

    MovingSequence instructions;

    public Animal(int length, double bodysize) {
        this.length = length;
        this.bodysize = bodysize;
    }

    public void updateRelativePositions() {
        AnimalPart part = head.tail;

        head.updateSides();

        while (part != null) {
            Vektor direction = Vektor.dir(part.head.pos, part.pos);


            direction.limit(part.head.size - (part.size/2))
                    .add(part.head.pos);

            part.pos = new Vektor(direction);
            part.angle = Vektor.fullAngle(part.pos, part.head.pos);
            part = part.tail;
        }
    }

    @Override
    public void update() {
        updateRelativePositions();

        AnimalPart part = head.tail;

        while (part != null) {
            part.updateSides();
            part = part.tail;
        }

    }

    @Override
    public void draw(Graphics2D g2) {
        drawBody(g2);
        drawSpecialParts(g2);
        drawHead(g2);
    }

    private void drawSpecialParts(Graphics2D g2) {

        AnimalPart anchor = this.head;

        int i = 0;

        while (anchor != null) {

            if (i == 2) {
                System.out.println(anchor.rightSide.yAsInt() + " " + anchor.leftSide.yAsInt());
            }
            i++;

            if (anchor.specialPart != AnimalPart.NOTHING) {
                if (anchor.specialPart == AnimalPart.LEGS) {

                    double rightAngle = anchor.angle + anchor.rightAngle;
                    double leftAngle = anchor.angle + anchor.leftAngle;

                    Vektor rightArmPos = new Vektor(anchor.pos)
                            .add(Vektor.createFromAngle(rightAngle-Math.PI/2).mult((anchor.size/2)*0.9));

                    Vektor leftArmPos = new Vektor(anchor.pos)
                            .add(Vektor.createFromAngle(leftAngle+Math.PI/2).mult((anchor.size/2)*0.9));


                    PolygonInfos po = AnimalBodyPartGenerator.createRightLeg(rightArmPos, rightAngle, bodysize);
                    g2.fillPolygon(po.x, po.y, po.am);

                    po = AnimalBodyPartGenerator.createLeftLeg(leftArmPos, leftAngle, bodysize);
                    g2.fillPolygon(po.x, po.y, po.am);

                }
            }

            anchor = anchor.tail;
        }

    }

    private void drawBody(Graphics2D g2) {
        g2.setColor(Color.GREEN);
        PolygonInfos shape = getShape();
        g2.fillPolygon(shape.x, shape.y, shape.am);
        int x = (int)(head.pos.x - head.size/2);
        int y = (int)(head.pos.y - head.size/2);

        g2.fillOval(x, y, (int)head.size, (int) head.size);
    }

    private void drawHead(Graphics2D g2) {
        double headAngle = head.angle;

        double eyeSize = head.size/5;
        double eyeGap = (head.size/2) - (eyeSize*0.8);

        double rightEyeAngle = headAngle + Math.PI/4;
        double leftEyeAngle = headAngle - Math.PI/4;

        Vektor rightEyePos = Vektor
                .createFromAngle(rightEyeAngle)
                .mult(eyeGap)
                .add(head.pos);
        Vektor leftEyePos = Vektor
                .createFromAngle(leftEyeAngle)
                .mult(eyeGap)
                .add(head.pos);

        g2.setColor(Color.BLACK);
        g2.fillOval(rightEyePos.xAsInt() - (int)(eyeSize/2),rightEyePos.yAsInt() - (int)(eyeSize/2), (int)eyeSize, (int)eyeSize);
        g2.fillOval(leftEyePos.xAsInt() - (int)(eyeSize/2),leftEyePos.yAsInt() - (int)(eyeSize/2), (int)eyeSize, (int)eyeSize);
    }

    private PolygonInfos getShape() {

        PolygonInfos shape = new PolygonInfos();

        int amCoords = size()*2;

        shape.am = amCoords;

        shape.x = new int[amCoords];
        shape.y = new int[amCoords];

        int pos = 0;

        AnimalPart part = head;

        while (part != null) {

            shape.x[pos] = part.leftSide.xAsInt();
            shape.y[pos] = part.leftSide.yAsInt();
            shape.x[amCoords-1-pos] = part.rightSide.xAsInt();
            shape.y[amCoords-1-pos] = part.rightSide.yAsInt();


            pos++;
            part = part.tail;
        }

        swap(shape.x, 0, amCoords-1);
        swap(shape.y, 0, amCoords-1);


        return shape;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


    private int size() {
        return length;
    }

}
