package de.luiskun;

import de.luiskun.tools.Songs;
import de.luiskun.ui.Screen;
import org.jfugue.player.Player;
import org.jfugue.theory.Note;

import javax.sound.midi.*;
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
