package com.k9rosie.novswar.player;

import com.k9rosie.novswar.NovsWar;

import com.k9rosie.novswar.util.SendTitle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerManager {

    private NovsWar novswar;
    private HashMap<Player, NovsPlayer> players;

    public PlayerManager(NovsWar novswar) {
        this.novswar = novswar;
        players = new HashMap<>();
    }

    public HashMap<Player, NovsPlayer> getPlayers() {
        return players;
    }

    public NovsPlayer getPlayer(Player bukkitPlayer) {
        return players.get(bukkitPlayer);
    }

    public NovsPlayer getPlayer(String displayName) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getDisplayName().equalsIgnoreCase(displayName)) {
                return players.get(player);
            }
        }
        return null;
    }

    public ArrayList<NovsPlayer> getNovsPlayers() {
        return (ArrayList<NovsPlayer>) players.values();
    }

    public NovsPlayer createNovsPlayer(Player bukkitPlayer) {
        NovsPlayer player = new NovsPlayer(bukkitPlayer);
        players.put(bukkitPlayer, player);
        return player;
    }
}
