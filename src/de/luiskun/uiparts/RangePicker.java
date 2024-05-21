package de.luiskun.uiparts;

import javax.swing.*;
import java.awt.*;

public class RangePicker extends JSlider {

    int defaultVal;
    RangeSelection r;
    String command;

    public RangePicker(RangeSelection r, int start, int end, int cur, String command) {

        this.defaultVal = cur;
        this.r = r;
        this.command = command;

        this.setMinimum(start);
        this.setMaximum(end);
        this.setValue(cur);
        this.setBackground(Color.BLACK);
        this.addChangeListener(listener -> {
            r.rangeChanged(command, this.getValue());
        });

    }

    public void setDefaultVal() {
        this.setValue(defaultVal);
        r.rangeChanged(command, this.getValue());
    }

}
