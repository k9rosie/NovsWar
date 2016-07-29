package com.k9rosie.novswar.game;

import com.k9rosie.novswar.NovsWar;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {

    private Game game;
    private BukkitScheduler scheduler;
    private Runnable task;
    private int taskID;
    private int time;

    public GameTimer(Game game) {
        this.game = game;
        time = -1;
        taskID = 0;
        scheduler = Bukkit.getScheduler();
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

    public Runnable getTask() {
        return task;
    }

    public int getTaskID() {
        return taskID;
    }

    public void startTimer() {
        if (taskID != 0 && time > -1) {
            Bukkit.getLogger().severe("Warning: Attempted to create start GameTimer when one has already been started. Cancelling...");
            return;
        }

        task = new Runnable() {
            public void run() {
                time--;
                game.clockTick();
                if (time <= -1) {
                    stopTimer();
                    game.endTimer();
                }
            }
        };

        taskID = scheduler.scheduleSyncRepeatingTask(NovsWar.getInstance().getPlugin(), task, 0, 20);
    }

    public void pauseTimer() {
        taskID = 0;
        scheduler.cancelTask(taskID);
    }

    public void stopTimer() {
        time = -1;
        taskID = 0;
        scheduler.cancelTask(taskID);
    }
}
