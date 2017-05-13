package com.k9rosie.novswar.game;

import com.k9rosie.novswar.NovsWar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitScheduler;

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
                clockTick();
                time--;
                if (time <= -1) {
                    stopTimer();
                    game.endTimer();
                }
            }
        };

        taskID = scheduler.scheduleSyncRepeatingTask(NovsWar.getInstance().getPlugin(), task, 0, 20);
    }

    public void pauseTimer() {
        scheduler.cancelTask(taskID);
        taskID = 0;
    }

    public void stopTimer() {
        time = -1;
        scheduler.cancelTask(taskID);
        taskID = 0;
    }

    public void clockTick() {
        String secondsString = Integer.toString(getSeconds());
        String minutesString = Integer.toString(getMinutes());
        String gameStateString = "";

        switch (game.getGameState()) {
            case PRE_GAME :
                gameStateString = ChatColor.GRAY + "Setting up: ";
                break;
            case DURING_GAME :
                gameStateString = "";
                break;
            case POST_GAME :
                gameStateString = ChatColor.GRAY + "Post game: ";
                break;
            default :
                gameStateString = "";
                break;
        }
        if (game.isPaused()) {
            gameStateString = ChatColor.GRAY + "Game Paused ";
        }
        if (getSeconds() < 10) {
            secondsString = "0" + Integer.toString(getSeconds());
        } else if (getSeconds() <= 0) {
            secondsString = "00";
        }
        if (getMinutes() < 10) {
            minutesString = "0" + Integer.toString(getMinutes());
        } else if (getMinutes() <= 0) {
            minutesString = "00";
        }
        game.getScoreboard().setSidebarTitle(gameStateString + ChatColor.GREEN + minutesString + ":" + secondsString);
    }
}
