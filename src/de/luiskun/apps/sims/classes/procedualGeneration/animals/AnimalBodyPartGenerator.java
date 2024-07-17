package de.luiskun.apps.sims.classes.procedualGeneration.animals;

import de.luiskun.apps.sims.classes.procedualGeneration.PolygonInfos;
import de.luiskun.tools.Vektor;

public class AnimalBodyPartGenerator {

    public static PolygonInfos createRightLeg(Vektor startPos, double angle) {


        int[] x, y;

        x = new int[]{
                // Unterarm
                11,
                32,
                -19,
                -26,
                -35,
                -33,
                -64,
                -74,
                -46,
                -76,
                -76,
                -44,
                0,
                -13
        };

        y = new int[]{
                // Unterarm
                2,
                -58,
                -93,
                -130,
                -129,
                -86,
                -116,
                -108,
                -78,
                -76,
                -67,
                -64,
                -43,
                2
        };

        for (int i = 0; i < 14; i++) {
            x[i] += startPos.xAsInt();
            y[i] += startPos.yAsInt();
        }

        rotatePoints(x,y,startPos,angle);



       PolygonInfos po = new PolygonInfos();
       po.x = x;
       po.y = y;
       po.am = 14;

       return po;
    }

    public static void rotatePoints(int[] x, int[] y, Vektor p, double angle) {
        for (int i = 0; i < x.length; i++) {
            // Verschiebung
            int tempX = x[i] - p.xAsInt();
            int tempY = y[i] - p.yAsInt();

            // Rotation
            int rotatedX = (int) (tempX * Math.cos(angle) - tempY * Math.sin(angle));
            int rotatedY = (int) (tempX * Math.sin(angle) + tempY * Math.cos(angle));

            // Rückverschiebung
            x[i] = rotatedX + p.xAsInt();
            y[i] = rotatedY + p.yAsInt();
        }
    }

    public static PolygonInfos createLeftLeg(Vektor startPos, double angle) {


        int[] x, y;

        x = new int[]{
                // Unterarm
                11,
                32,
                -19,
                -26,
                -35,
                -33,
                -64,
                -74,
                -46,
                -76,
                -76,
                -44,
                0,
                -13
        };

        y = new int[]{
                // Unterarm
                -2,
                58,
                93,
                130,
                129,
                86,
                116,
                108,
                78,
                76,
                67,
                64,
                43,
                -2
        };

        for (int i = 0; i < 14; i++) {
            x[i] += startPos.xAsInt();
            y[i] += startPos.yAsInt();
        }

        rotatePoints(x,y,startPos,angle);



        PolygonInfos po = new PolygonInfos();
        po.x = x;
        po.y = y;
        po.am = 14;

        return po;
    }

}
