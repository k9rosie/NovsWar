package com.k9rosie.novswar.manager;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class PlayerManager {

    private NovsWar novswar;

    private HashMap<Player, NovsPlayer> players;

    public PlayerManager(NovsWar novswar) {
        this.novswar = novswar;
        players = new HashMap<Player, NovsPlayer>();
    }

    public Collection<Player> getBukkitPlayers() {
        return players.keySet();
    }

    public Collection<NovsPlayer> getNovsPlayers() {
        return players.values();
    }

    public HashMap<Player, NovsPlayer> getPlayers() {
        return players;
    }

    public NovsPlayer createNovsPlayer(Player bukkitPlayer) {
        NovsPlayer player = new NovsPlayer(bukkitPlayer, novswar.getTeamManager().getDefaultTeam());
        players.put(bukkitPlayer, player);
        return player;
    }

    public NovsPlayer getNovsPlayerFromUsername(String displayName) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getDisplayName().equalsIgnoreCase(displayName)) {
                return players.get(player);
            }
        }
        return null;
    }
}
