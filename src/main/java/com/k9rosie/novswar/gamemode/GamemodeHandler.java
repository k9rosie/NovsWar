package com.k9rosie.novswar.gamemode;

import com.k9rosie.novswar.NovsWar;

import java.util.ArrayList;

public class GamemodeHandler {
    public ArrayList<Gamemode> gamemodes;
    public NovsWar novsWar;

    public GamemodeHandler(NovsWar novsWar) {
        this.novsWar = novsWar;
        gamemodes = new ArrayList<Gamemode>();
    }
}
