package com.k9rosie.novswar.manager;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;

public class PlayerManager {

    private NovsWar novswar;

    private HashMap<Player, NovsPlayer> players;

    public PlayerManager(NovsWar novswar) {
        this.novswar = novswar;
    }

    public HashMap<Player, NovsPlayer> getPlayers() {
        return players;
    }



}
