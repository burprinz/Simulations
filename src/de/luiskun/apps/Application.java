package de.luiskun.apps;

import javax.swing.*;
import java.awt.*;

public abstract class Application extends JPanel {

    public abstract void update();

    @Override
    public abstract void paintComponent(Graphics g);

}
