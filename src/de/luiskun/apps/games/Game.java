package de.luiskun.apps.games;

import de.luiskun.apps.Application;
import de.luiskun.tools.SoundPlayer;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Game extends Application {

    protected static final int MENUSTATE = 0;
    protected static final int GAMESTATE = 1;
    protected static final int GAMEOVERSTATE = 2;
    protected int gamestate = 0;

    List<SoundPlayer> playingSounds;

    public Game() {
        super();
        playingSounds = new ArrayList<>();
    }

    public void playScoreSound() {
        playingSounds.add(new SoundPlayer(0));
    }

    public abstract void gameOver(int score);
    public void update() {
        for (int i = 0; i < playingSounds.size(); i++) {
            if (playingSounds.get(i).finished) {
                playingSounds.remove(i);
            }
        }
    }

    @Override
    public abstract void paintComponent(Graphics g);

}

