package com.k9rosie.novswar.game;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class DeathTimer {

    private Game game;
    private NovsPlayer player;
    private BukkitScheduler scheduler;
    private Runnable task;
    private int taskID;
    private int time;

    public DeathTimer(Game game, int time, NovsPlayer player) {
        this.game = game;
        this.time = time;
        this.player = player;
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
        task = new Runnable() {
            public void run() {
                game.deathTick(player);
                time--;
                if (time <= 0) {
                    game.respawn(player);
                }
            }
        };

        taskID = scheduler.scheduleSyncRepeatingTask(NovsWar.getInstance().getPlugin(), task, 0, 20);
    }

    public void pauseTimer() {
        scheduler.cancelTask(taskID);
    }

    public void stopTimer() {
        time = 0;
        scheduler.cancelTask(taskID);
    }
}
