package de.luiskun.json;

import java.util.List;

public class Farbpalette {

    List<Farbe> colors;

    public Farbpalette() {

    }

    public Farbpalette(List<Farbe> colors) {
        this.colors = colors;
    }

    public List<Farbe> getColors() {
        return colors;
    }

    public void setColors(List<Farbe> colors) {
        this.colors = colors;
    }
}
