package com.k9rosie.novswar.manager;


import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsRegion;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.model.NovsWorld;
import com.k9rosie.novswar.util.RegionType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.logging.Level;

public class WorldManager {

    private NovsWar novswar;

    private HashMap<World, NovsWorld> worlds;
    private NovsWorld lobbyWorld;

    public WorldManager(NovsWar novswar) {
        this.novswar = novswar;
        worlds = new HashMap<World, NovsWorld>();
    }

    public void initialize() {
        loadWorlds();
        loadLobbyWorld();
    }

    public HashMap<World, NovsWorld> getWorlds(){
        return worlds;
    }

    public NovsWorld getLobbyWorld() {
        return lobbyWorld;
    }
    
    /**
     * Searches for world based on BukkitWorld name
     * @param worldName
     * @return
     */
    public NovsWorld getWorldByName(String worldName) {
    	NovsWorld result = null;
    	for(NovsWorld world : worlds.values()) {
    		if(world.getBukkitWorld().getName().equals(worldName)) {
    			result = world;
    		}
    	}
    	return result;
    }

    public void loadLobbyWorld() {
        FileConfiguration coreConfig = novswar.getConfigurationCache().getConfig("core");
        String worldName = coreConfig.getString("core.lobby.lobby_world");
        World bukkitWorld = novswar.getPlugin().getServer().getWorld(worldName);
        if (bukkitWorld == null) {
            Bukkit.createWorld(new WorldCreator(worldName));
            bukkitWorld = novswar.getPlugin().getServer().getWorld(worldName);
        }
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
            if (world == null) {
                Bukkit.createWorld(new WorldCreator(worldName));
                world = novswar.getPlugin().getServer().getWorld(worldName);
            }
            String name = worldConfig.getString("worlds."+worldName+".name");
            NovsWorld novsWorld = new NovsWorld(name, world);


            loadRegions(novsWorld);

            worlds.put(world, novsWorld);
        }
    }

    public void loadRegions(NovsWorld world) {
        FileConfiguration regionsConfig = novswar.getConfigurationCache().getConfig("regions");
        if (regionsConfig.get("regions."+world.getBukkitWorld().getName()) == null) {
            return;
        }
        Set<String> teamNames = regionsConfig.getConfigurationSection("regions."+world.getBukkitWorld().getName()+".spawns").getKeys(false);
        for (String teamName : teamNames) {
            double x = novswar.getConfigurationCache().getConfig("regions").getDouble("regions."+world.getBukkitWorld().getName()+".spawns."+teamName+".x");
            double y = novswar.getConfigurationCache().getConfig("regions").getDouble("regions."+world.getBukkitWorld().getName()+".spawns."+teamName+".y");
            double z = novswar.getConfigurationCache().getConfig("regions").getDouble("regions."+world.getBukkitWorld().getName()+".spawns."+teamName+".z");
            float pitch = (float) novswar.getConfigurationCache().getConfig("regions").getDouble("regions."+world.getBukkitWorld().getName()+".spawns."+teamName+".pitch");
            float yaw = (float) novswar.getConfigurationCache().getConfig("regions").getDouble("regions."+world.getBukkitWorld().getName()+".spawns."+teamName+".yaw");
            NovsTeam team = novswar.getTeamManager().getTeam(teamName);

            world.getTeamSpawns().put(team, new Location(world.getBukkitWorld(), x, y, z, pitch, yaw));
        }

        ConfigurationSection regions = regionsConfig.getConfigurationSection("regions."+world.getBukkitWorld().getName()+".regions");

        for (String regionName : regions.getKeys(false)) {
            RegionType type = RegionType.parseString(regions.getString(regionName+".type"));
            int cornerOneX = regions.getInt(regionName+".corner_one.x");
            int cornerOneY = regions.getInt(regionName+".corner_one.y");
            int cornerOneZ = regions.getInt(regionName+".corner_one.z");
            int cornerTwoX = regions.getInt(regionName+".corner_two.x");
            int cornerTwoY = regions.getInt(regionName+".corner_two.y");
            int cornerTwoZ = regions.getInt(regionName+".corner_two.z");

            NovsRegion region = new NovsRegion(world,
                    new Location(world.getBukkitWorld(), cornerOneX, cornerOneY, cornerOneZ),
                    new Location(world.getBukkitWorld(), cornerTwoX, cornerTwoY, cornerTwoZ), type);

            region.saveBlocks();
            world.getRegions().put(regionName, region);
        }
    }

    public HashSet<NovsRegion> getRegionsInLocation(Location location) {
        HashSet regions = new HashSet<NovsRegion>();
        NovsWorld world = worlds.get(location.getWorld());
        for (NovsRegion region : world.getRegions().values()) {
            if (region.inRegion(location)) {
                regions.add(region);
            }
        }
        return regions;
    }

    public void saveRegions() {
        FileConfiguration regionsConfig = novswar.getConfigurationCache().getConfig("regions");
        regionsConfig.set("regions", null);
        ConfigurationSection root = regionsConfig.createSection("regions");

        for (NovsWorld world : worlds.values()) {
            ConfigurationSection worldSection = root.createSection(world.getBukkitWorld().getName());
            ConfigurationSection spawnsSection = worldSection.createSection("spawns");

            for (Map.Entry<NovsTeam, Location> entry : world.getTeamSpawns().entrySet()) {
                ConfigurationSection teamSection = spawnsSection.createSection(entry.getKey().getTeamName());
                teamSection.set("x", entry.getValue().getX());
                teamSection.set("y", entry.getValue().getY());
                teamSection.set("z", entry.getValue().getZ());
                teamSection.set("pitch", entry.getValue().getPitch());
                teamSection.set("yaw", entry.getValue().getYaw());
            }

            ConfigurationSection regionsSection = worldSection.createSection("regions");
            for (Map.Entry<String, NovsRegion> entry : world.getRegions().entrySet()) {
                ConfigurationSection regionSection = regionsSection.createSection(entry.getKey());
                regionSection.set("type", entry.getValue().getRegionType().toString());

                ConfigurationSection cornerOneSection = regionSection.createSection("corner_one");
                cornerOneSection.set("x", (int) entry.getValue().getCornerOne().getBlockX());
                cornerOneSection.set("y", (int) entry.getValue().getCornerOne().getBlockY());
                cornerOneSection.set("z", (int) entry.getValue().getCornerOne().getBlockZ());

                ConfigurationSection cornerTwoSection = regionSection.createSection("corner_two");
                cornerTwoSection.set("x", (int) entry.getValue().getCornerTwo().getBlockX());
                cornerTwoSection.set("y", (int) entry.getValue().getCornerTwo().getBlockY());
                cornerTwoSection.set("z", (int) entry.getValue().getCornerTwo().getBlockZ());

            }
        }
    }

}
