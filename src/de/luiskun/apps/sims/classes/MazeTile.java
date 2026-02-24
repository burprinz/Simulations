package de.luiskun.apps.sims.classes;

public class MazeTile {

    public boolean upOpen, downOpen, rightOpen, leftOpen;
    public boolean start, finish;
    public boolean isSeen = false;
    public int x, y;
    public boolean isVisited = false;


    public void print() {
        System.out.println(x+" "+y+" up:"+upOpen+" down:"+downOpen+" right:"+rightOpen+" left:"+leftOpen);
    }

}
