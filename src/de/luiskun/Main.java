package de.luiskun;

import de.luiskun.ui.Screen;
import javax.swing.*;

/**
 * To whoever it may concern
 * my name is Luis Burmeier
 * I am the original author of this code
 *
 * This code should not be enhanced, tuned, upgraded, updated or otherwise changed
 *
 * I have no Idea how this code actually works, its truly the 8th wonder of this world
 * Nothing on this mortal plane of existance could explain the existance of this system
 *
 * The variables the names of the Gods to appease them
 * The loops an enigma
 *
 * please, I beg you, do not touch this code.
 * do not.
 *
 * Good Luck,
 * Luis Burmeier
 */
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
