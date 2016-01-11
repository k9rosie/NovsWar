package com.k9rosie.novswar.model;

import org.bukkit.entity.Player;

public class NovsPlayer {

    private Player player;
    private NovsLoadout loadout;
    private NovsTeam team;

    public NovsPlayer(Player player) {
        this.player = player;
    }

    public Player getBukkitPlayer() {
        return player;
    }

    public void setBukkitPlayer(Player player) {
        this.player = player;
    }

    public NovsLoadout getLoadout() {
        return loadout;
    }

    public void setLoadout(NovsLoadout loadout) {
        this.loadout = loadout;
    }

    public NovsTeam getTeam() {
        return team;
    }

    public void setTeam(NovsTeam team) {
        this.team = team;
    }

}