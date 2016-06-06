package com.k9rosie.novswar.gamemode;

import com.k9rosie.novswar.NovsWar;
import java.util.HashMap;
import java.util.HashSet;

public class GamemodeHandler {
    public HashMap<String, Gamemode> gamemodes;
    public NovsWar novsWar;

    public GamemodeHandler(NovsWar novsWar) {
        this.novsWar = novsWar;
        gamemodes = new HashMap<String, Gamemode>();
    }

    public HashMap<String, Gamemode> getGamemodes() {
        return gamemodes;
    }
}
