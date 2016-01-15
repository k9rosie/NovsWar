package com.k9rosie.novswar.manager;


import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsWorld;
import com.k9rosie.novswar.util.Gamemode;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class WorldManager {

    private NovsWar novswar;

    private HashMap<World, NovsWorld> worlds;

    public WorldManager(NovsWar novswar) {
        this.novswar = novswar;
    }

    public void initialize() {
        loadWorlds();
    }

    public HashMap<World, NovsWorld> getWorlds(){
        return worlds;
    }

    public void loadWorlds() {
        FileConfiguration worldConfig = novswar.getConfigurationCache().getConfig("worlds");
        List<String> enabledWorldNames = novswar.getConfigurationCache().getConfig("core").getStringList("core.worlds.enabled_worlds");

        for (String worldName : enabledWorldNames) {
            World world = novswar.getPlugin().getServer().getWorld(worldName);
            String name = worldConfig.getString("worlds."+worldName+".name");
            Gamemode gamemode = Gamemode.parseString(worldConfig.getString("worlds."+worldName+".gamemode"));

            NovsWorld novsWorld = new NovsWorld(name, gamemode);
            // TODO: load world regions here
            worlds.put(world, novsWorld);
        }
    }

}
