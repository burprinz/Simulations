package de.luiskun.json;

import de.luiskun.tools.Vektor;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class GOL_Preset {

    List<IntegerPunkt> points;

    public GOL_Preset() {

    }

    public GOL_Preset(Vektor[] p) {
        points = new ArrayList();
        for (Vektor v : p) {
            points.add(new IntegerPunkt(v.xAsInt(), v.yAsInt()));
        }
    }

    public GOL_Preset(List<IntegerPunkt> points) {
        this.points = points;
    }

    public List<IntegerPunkt> getPoints() {
        return points;
    }

    public void setPoints(List<IntegerPunkt> points) {
        this.points = points;
    }
}
