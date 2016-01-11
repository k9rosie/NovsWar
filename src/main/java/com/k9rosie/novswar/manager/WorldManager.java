package com.k9rosie.novswar.manager;


import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsWorld;

import java.util.HashSet;
import java.util.List;

public class WorldManager {

    private NovsWar novswar;

    private HashSet<NovsWorld> worlds;
    private NovsWorld lobbyWorld;

    public WorldManager(NovsWar novswar) {
        this.novswar = novswar;
    }

    public void initialize() {
        loadWorlds();
    }

    public HashSet<NovsWorld> getWorlds(){
        return worlds;
    }

    public void addWorld(NovsWorld world) {
        worlds.add(world);
    }

    public void loadWorlds() {
        List<String> enabledWorldNames = novswar.getConfigurationCache().getConfig("core").getStringList("core.worlds.enabled_worlds");

        for (String worldName : enabledWorldNames) {

        }
    }

}
