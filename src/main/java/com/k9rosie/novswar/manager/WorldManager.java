package com.k9rosie.novswar.manager;


import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsRegion;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.model.NovsWorld;
import com.k9rosie.novswar.util.RegionType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        List<String> enabledWorldNames = novswar.getConfigurationCache().getConfig("core").getStringList("core.world.enabled_worlds");
        for (String worldName : enabledWorldNames) {
            World world = novswar.getPlugin().getServer().getWorld(worldName);
            String name = worldConfig.getString("worlds."+worldName+".name");
            NovsWorld novsWorld = new NovsWorld(name, world);

            loadRegions(novsWorld);

            worlds.add(novsWorld);
        }
    }

    public void loadRegions(NovsWorld world) {
        FileConfiguration regionsConfig = novswar.getConfigurationCache().getConfig("regions");
        if (regionsConfig.get("regions."+world.getBukkitWorld().getName()) == null) {
            return;
        }
        Set<String> teamNames = regionsConfig.getConfigurationSection("regions."+world.getBukkitWorld().getName()+".spawns").getKeys(false);
        for (String teamName : teamNames) {
            int x = novswar.getConfigurationCache().getConfig("regions").getInt("regions."+world.getBukkitWorld().getName()+".spawns."+teamName+".x");
            int y = novswar.getConfigurationCache().getConfig("regions").getInt("regions."+world.getBukkitWorld().getName()+".spawns."+teamName+".y");
            int z = novswar.getConfigurationCache().getConfig("regions").getInt("regions."+world.getBukkitWorld().getName()+".spawns."+teamName+".z");
            NovsTeam team = novswar.getTeamManager().getTeam(teamName);

            world.getTeamSpawns().put(team, new Location(world.getBukkitWorld(), x, y, z));
        }

        ConfigurationSection regions = regionsConfig.getConfigurationSection("regions."+world.getBukkitWorld().getName()+".regions");

        for (String regionName : regions.getKeys(false)) {
            System.out.println(regions.getString(regionName+".type"));
            RegionType type = RegionType.parseString(regions.getString(regionName+".type"));
            int cornerOneX = regions.getInt(regionName+".corner_one.x");
            int cornerOneY = regions.getInt(regionName+".corner_one.y");
            int cornerOneZ = regions.getInt(regionName+".corner_one.z");
            int cornerTwoX = regions.getInt(regionName+".corner_two.x");
            int cornerTwoY = regions.getInt(regionName+".corner_two.y");
            int cornerTwoZ = regions.getInt(regionName+".corner_two.z");

            NovsRegion region = new NovsRegion(world, regionName,
                    new Location(world.getBukkitWorld(), cornerOneX, cornerOneY, cornerOneZ),
                    new Location(world.getBukkitWorld(), cornerTwoX, cornerTwoY, cornerTwoZ), type);

            region.setBlocks(region.getCuboid());

            switch (type) {
                case BATTLEFIELD:
                    world.setBattlefield(region);
                case DEATH_REGION:
                    world.getDeathRegions().add(region);
                case INTERMISSION_GATE:
                    world.getIntermissionGates().add(region);
            }
        }
    }
}
