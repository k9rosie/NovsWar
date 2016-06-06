package com.k9rosie.novswar.model;

import org.bukkit.entity.Player;

public class NovsPlayer {

    private Player bukkitPlayer;
    private NovsTeam team;
    private NovsStats stats;

    public NovsPlayer (Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
        stats = new NovsStats(this);
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public NovsStats getStats() {
        return stats;
    }

    public void setTeam(NovsTeam team) {
        this.team = team;
    }

    public NovsTeam getTeam() {
        return team;
    }
}