package com.k9rosie.novswar.manager;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;

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

}
