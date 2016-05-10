package com.k9rosie.novswar.manager;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class PlayerManager {

    private NovsWar novswar;

    private ArrayList<NovsPlayer> players;

    public PlayerManager(NovsWar novswar) {
        this.novswar = novswar;
    }

    public ArrayList<NovsPlayer> getPlayers() {
        return players;
    }

    public void createNovsPlayer(Player bukkitPlayer) {
        NovsPlayer player = new NovsPlayer(bukkitPlayer);
        players.add(player);
    }
}
