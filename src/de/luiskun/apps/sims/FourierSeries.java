package de.luiskun.apps.sims;

import de.luiskun.apps.Application;
import de.luiskun.tools.Vektor;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class FourierSeries extends Application {

    Vektor firstCenter = new Vektor(220, 480);

    float time = 0;
    int maxDrawTimer = 1;
    int drawTimer = 0;

    int n = 10;

    int xDrawStart = 450;

    List<Vektor> resultPoints;
    Vektor[] circlePositions;

    public FourierSeries() {
        super();
        reset();
    }

    private void reset() {
        resultPoints = new ArrayList<>();
        circlePositions = new Vektor[n];
    }


    @Override
    public void update() {


        Vektor prevCenter = new Vektor(firstCenter);
        for (int i = 0; i < n; i++) {
            double radius = calcRadius(i+1);
            circlePositions[i] = Vektor.polarToCartesian(radius, time*(i+1)*(i%2==0?1:-1)).add(prevCenter);
            prevCenter = new Vektor(circlePositions[i]);
        }

        time += 0.05;
        drawTimer++;
        if (drawTimer > maxDrawTimer) {
            drawTimer = 0;
            double y = calcFourierHeight();
            resultPoints.add(new Vektor(xDrawStart, y));
            for (int i = resultPoints.size()-1; i >= 0; i--) {
                if (resultPoints.get(i).x > WIDTH) {
                    resultPoints.remove(i);
                } else {
                    resultPoints.get(i).x += 2;
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        g2.fillRect(0,0,WIDTH,HEIGHT);

        g2.setColor(Color.WHITE);

        Vektor center = new Vektor(firstCenter);
        for (int i = 0; i < n; i++) {
            double radius = calcRadius(i+1);
            Vektor pos = new Vektor(center).sub(new Vektor(radius, radius));
            g2.drawOval(pos.xAsInt(), pos.yAsInt(), (int)(radius*2), (int)(radius*2));

            //Vektor newCenter = new Vektor(center).add(Vektor.polarToCartesian(radius, time));
            g2.drawLine(center.xAsInt(), center.yAsInt(), circlePositions[i].xAsInt(), circlePositions[i].yAsInt());
            center = new Vektor(circlePositions[i]);
        }
        /*
        g2.drawOval(x, y, (int)(2*radius), (int)(2*radius));

        Vektor pos = Vektor.polarToCartesian(radius, time).add(center);
        g2.drawLine(center.xAsInt(),center.yAsInt(),pos.xAsInt(),pos.yAsInt());

        g2.drawLine(pos.xAsInt(), pos.yAsInt(), xDrawStart, pos.yAsInt());
*/

        double h = (int)calcFourierHeight();
        g2.drawLine(circlePositions[n-1].xAsInt(), (int)h, xDrawStart, (int)h);

        for (int i = 1; i < resultPoints.size(); i++) {
            g2.drawLine(resultPoints.get(i-1).xAsInt(), resultPoints.get(i-1).yAsInt(), resultPoints.get(i).xAsInt(), resultPoints.get(i).yAsInt());
        }


    }

    private double calcRadius(int n) {
        return 150d * (4d / (n * Math.PI));
    }

    private double calcFourierHeight() {
        return circlePositions[n-1].y;
    }
}
