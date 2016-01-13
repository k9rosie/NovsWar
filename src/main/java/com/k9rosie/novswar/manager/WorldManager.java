package com.k9rosie.novswar.manager;


import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsWorld;
import com.k9rosie.novswar.util.Gamemode;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

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

    public NovsWorld getNovsWorld(World world) {
        for (NovsWorld novsWorld : world) {
            if (novsWorld.getBukkitWorld().equals(world)) {
                return novsWorld;
            }
        }
    }

    public void loadWorlds() {
        FileConfiguration worldConfig = novswar.getConfigurationCache().getConfig("worlds");
        List<String> enabledWorldNames = novswar.getConfigurationCache().getConfig("core").getStringList("core.worlds.enabled_worlds");

        for (String worldName : enabledWorldNames) {
            World world = novswar.getPlugin().getServer().getWorld(worldName);
            String name = worldConfig.getString("worlds".+worldName+".name");
            Gamemode gamemode = Gamemode.parseString(worldConfig.getString("worlds."+worldName+".gamemode"));

            NovsWorld novsWorld = new NovsWorld(world, name, gamemode);
            // TODO: load world regions here
            worlds.add(novsWorld);
        }
    }

}
