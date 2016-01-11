package com.k9rosie.novswar.model;

import org.bukkit.entity.Player;

public class NovsPlayer {

    private Player player;
    private NovsTeam team;
    private NovsLoadout loadout;

    public NovsPlayer(Player player, NovsTeam team, NovsLoadout loadout) {
        this.player = player;
        this.team = team;
        this.loadout = loadout;
    }

}