package com.k9rosie.novswar.manager;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class PlayerManager {

    private NovsWar novswar;

    private HashSet<NovsPlayer> players;

    public PlayerManager(NovsWar novswar) {
        this.novswar = novswar;
    }

    public HashSet<NovsPlayer> getPlayers() {
        return players;
    }

    public NovsPlayer getNovsPlayer(Player player) {
        for (NovsPlayer novsPlayer : player) {
            if (novsPlayer.getBukkitPlayer().equals(player)) {
                return novsPlayer;
            }
        }
        return null;
    }

}
