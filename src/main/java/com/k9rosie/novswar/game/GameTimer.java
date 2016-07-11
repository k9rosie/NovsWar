package com.k9rosie.novswar.game;

import org.bukkit.Bukkit;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {

    private Game game;
    private Timer timer;
    private TimerTask task;
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

    public TimerTask getTask() {
        return task;
    }

    public void startTimer() {
        task = new TimerTask() {
            public void run() {
                Bukkit.broadcastMessage(getMinutes() + ":" + getSeconds());
                time--;
                if (time <= 0) {
                    game.endTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
}
