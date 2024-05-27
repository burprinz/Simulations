package de.luiskun.tools;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer implements Runnable {

    String[] paths = {"res/sounds/pickup01.wav"};
    int[] len =  {300};

    int i;

   public boolean finished = false;

    public SoundPlayer(int sound) {
        i = sound;
        Thread t = new Thread(this);
        t.start();
    }


    @Override
    public void run() {
        try {
            String filepath = paths[i];
            AudioInputStream aui =  AudioSystem.getAudioInputStream(new File(filepath).getAbsoluteFile());

            Clip clip = AudioSystem.getClip();
            clip.open(aui);
            clip.start();   //start Clip
            Thread.sleep(len[i]);
            clip.stop();    //Stop Clip
            clip.close();

        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
        finished = true;
    }
}
