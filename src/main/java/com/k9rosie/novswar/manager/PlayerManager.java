package com.k9rosie.novswar.manager;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class PlayerManager {

    private NovsWar novswar;

    private HashSet<NovsPlayer> players;

    public PlayerManager(NovsWar novswar) {
        this.novswar = novswar;
        players = new HashSet<NovsPlayer>();
    }

    public HashSet<NovsPlayer> getPlayers() {
        return players;
    }

    public NovsPlayer getPlayerFromBukkitPlayer(Player bukkitPlayer) {
        for (NovsPlayer player : players) {
            if (player.getBukkitPlayer().equals(bukkitPlayer)) {
                return player;
            }
        }
        return null;
    }

    public NovsPlayer createNovsPlayer(Player bukkitPlayer) {
        NovsPlayer player = new NovsPlayer(bukkitPlayer);
        players.add(player);
        return player;
    }
}
