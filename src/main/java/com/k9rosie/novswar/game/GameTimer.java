package com.k9rosie.novswar.game;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {

    private Game game;
    private Timer timer;
    private int time;

    public GameTimer(Game game) {
        this.game = game;
        time = 0;
        timer = new Timer();
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getSeconds() {
        return time % 60;
    }

    public int getMinutes() {
        return time / 60;
    }

    public void startTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                time--;
                game.update(); // called every second
                if (time <= 0) {
                    this.cancel();
                }
            }
        }, 0, 1000);
    }
}
