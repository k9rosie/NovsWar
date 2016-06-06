package com.k9rosie.novswar.manager;


import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.model.NovsWorld;
import org.bukkit.Location;
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
        worlds = new HashSet<NovsWorld>();
    }

    public void initialize() {
        loadWorlds();
        loadLobbyWorld();
    }

    public HashSet<NovsWorld> getWorlds(){
        return worlds;
    }

    public NovsWorld getLobbyWorld() {
        return lobbyWorld;
    }

    public NovsWorld getWorldFromBukkitWorld(World bukkitWorld) {
        for (NovsWorld world : worlds) {
            if (world.getBukkitWorld().equals(bukkitWorld)) {
                return world;
            }
        }
        return null;
    }

    public void loadLobbyWorld() {
        FileConfiguration coreConfig = novswar.getConfigurationCache().getConfig("core");
        String worldName = coreConfig.getString("core.lobby.lobby_world");
        World bukkitWorld = novswar.getPlugin().getServer().getWorld(worldName);
        lobbyWorld = new NovsWorld(worldName, bukkitWorld);

        NovsTeam defaultTeam = novswar.getTeamManager().getDefaultTeam();
        int spawnX = coreConfig.getInt("core.lobby.spawn.x");
        int spawnY = coreConfig.getInt("core.lobby.spawn.y");
        int spawnZ = coreConfig.getInt("core.lobby.spawn.z");

        lobbyWorld.getTeamSpawns().put(defaultTeam, new Location(bukkitWorld, spawnX, spawnY, spawnZ));
    }

    public void loadWorlds() {
        FileConfiguration worldConfig = novswar.getConfigurationCache().getConfig("worlds");
        List<String> enabledWorldNames = novswar.getConfigurationCache().getConfig("core").getStringList("core.worlds.enabled_worlds");

        for (String worldName : enabledWorldNames) {
            World world = novswar.getPlugin().getServer().getWorld(worldName);
            String name = worldConfig.getString("worlds."+worldName+".name");

            NovsWorld novsWorld = new NovsWorld(name, world);
            // TODO: load world regions here
            worlds.add(novsWorld);
        }
    }

}
