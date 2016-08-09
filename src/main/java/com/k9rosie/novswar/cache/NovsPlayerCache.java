package com.k9rosie.novswar.cache;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class NovsPlayerCache {

    private NovsWar novswar;

    private HashMap<Player, NovsPlayer> players;

    public NovsPlayerCache(NovsWar novswar) {
        this.novswar = novswar;
        players = new HashMap<Player, NovsPlayer>();
    }

    public HashMap<Player, NovsPlayer> getPlayers() {
        return players;
    }

    public NovsPlayer createNovsPlayer(Player bukkitPlayer) {
        NovsPlayer player = new NovsPlayer(bukkitPlayer, novswar.getNovsTeamCache().getDefaultTeam());
        players.put(bukkitPlayer, player);
        return player;
    }

    public NovsPlayer getPlayerFromName(String displayName) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getDisplayName().equalsIgnoreCase(displayName)) {
                return players.get(player);
            }
        }
        return null;
    }
}
