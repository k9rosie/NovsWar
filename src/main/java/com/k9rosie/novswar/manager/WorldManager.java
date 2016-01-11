package com.k9rosie.novswar.manager;


import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Gamemode;
import com.k9rosie.novswar.model.NovsWorld;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            FileConfiguration regionsConfig = novswar.getConfigurationCache().getConfig("regions");
            // create a section if the world doesn't have one
            if (!regionsConfig.contains("regions."+worldName)) {
                regionsConfig.createSection("regions."+worldName+".spawns");
                regionsConfig.createSection("regions."+worldName+".regions");
            }



        }
    }

}
