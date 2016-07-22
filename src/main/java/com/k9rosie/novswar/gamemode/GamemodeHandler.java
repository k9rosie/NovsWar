package com.k9rosie.novswar.gamemode;

import com.k9rosie.novswar.NovsWar;
import java.util.HashMap;
import java.util.HashSet;

public class GamemodeHandler {
    public HashMap<String, Gamemode> gamemodes;
    public NovsWar novsWar;

    //Constructor
    public GamemodeHandler(NovsWar novsWar) {
        this.novsWar = novsWar;
        gamemodes = new HashMap<String, Gamemode>();
    }
    
    //Initialize gamemodes - add new gamemode classes here
    public void initialize() {
    	GamemodeTDM tdm = new GamemodeTDM();
    	gamemodes.put(tdm.getGamemodeName(), tdm);
    }

    public HashMap<String, Gamemode> getGamemodes() {
        return gamemodes;
    }

}
