package com.k9rosie.novswar.player;

import com.k9rosie.novswar.NovsWarPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class AttackTimer implements Comparable<AttackTimer>{
    private NovsPlayer player;
    private NovsPlayer attacker;
    private double damage;
    private BukkitScheduler scheduler;
    private Runnable task;
    private int taskID;
    private int time;
    private int startingTime;

    public AttackTimer(NovsPlayer player, NovsPlayer attacker, double damage, int time) {
        this.player = player;
        this.attacker = attacker;
        this.damage = damage;
        this.time = time;
        startingTime = time;
        scheduler = Bukkit.getScheduler();
    }

    public NovsPlayer getAttacker() {
        return attacker;
    }

    public double getDamage() {
        return damage;
    }

    public void incrementDamage(double increment) {
        damage += increment;
    }

    public void startTimer() {
        if (taskID != 0 && time > -1) {
            Bukkit.getLogger().severe("Warning: Attempted to start AttackTimer when one has already been started. Cancelling...");
            return;
        }

        task = new Runnable() {
            @Override
            public void run() {
                time--;
                if (time <= 1) {
                    stopTimer();
                    player.getPlayerState().getAttackers().remove(attacker);
                }
            }
        };

        NovsWarPlugin plugin = player.getPlayerState().getGame().getNovsWarInstance().getPlugin();
        taskID = scheduler.scheduleSyncRepeatingTask(plugin, task, 0, 20);
    }

    public void resetTimer() {
        time = startingTime;
    }

    public void stopTimer() {
        scheduler.cancelTask(taskID);
        taskID = 0;
    }

    public int compareTo(AttackTimer timer) {
        int result = 0;

        if (damage > timer.getDamage()) {
            result = 1;
        } else if (damage == timer.getDamage()) {
            result = 0;
        } else if (damage < timer.getDamage()) {
            result = -1;
        }

        return result;
    }
}
