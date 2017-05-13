package com.k9rosie.novswar.player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.util.SendTitle;
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
        if (taskID != 0 && time > -1) {
            Bukkit.getLogger().severe("Warning: Attempted to start DeathTimer when one has already been started. Cancelling...");
            return;
        }

        task = new Runnable() {
            public void run() {
                deathTick();
                time--;
                if (time <= -1) {
                    player.getPlayerState().respawn();
                }
            }
        };

        taskID = scheduler.scheduleSyncRepeatingTask(game.getNovsWarInstance().getPlugin(), task, 0, 20);
    }

    public void pauseTimer() {
        scheduler.cancelTask(taskID);
        taskID = 0;
    }

    public void deathTick() {
        SendTitle.sendTitle(player.getBukkitPlayer(), 0, 2000, 0, " ", "Respawn in " + Integer.toString(getSeconds()) + "...");
    }

    public void stopTimer() {
        time = -1;
        scheduler.cancelTask(taskID);
        taskID = 0;
    }
}
