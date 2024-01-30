package de.luiskun.menu;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        JFrame frame = new JFrame();
        Screen s = new Screen();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(s);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        s.startThread();
    }

}
