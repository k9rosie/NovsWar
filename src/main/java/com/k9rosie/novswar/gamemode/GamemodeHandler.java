package com.k9rosie.novswar.gamemode;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.event.GamemodeHandlerInitialization;
import org.bukkit.Bukkit;

import java.util.HashMap;

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
        GamemodeHandlerInitialization event = new GamemodeHandlerInitialization(this);
        Bukkit.getPluginManager().callEvent(event);
    }

    public HashMap<String, Gamemode> getGamemodes() {
        return gamemodes;
    }

}
