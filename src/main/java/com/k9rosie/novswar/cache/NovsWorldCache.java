package com.k9rosie.novswar.cache;


import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsRegion;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.model.NovsWorld;
import com.k9rosie.novswar.util.RegionType;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class NovsWorldCache {

    private NovsWar novswar;

    private HashMap<World, NovsWorld> worlds;

    public NovsWorldCache(NovsWar novswar) {
        this.novswar = novswar;
        worlds = new HashMap<World, NovsWorld>();
    }

    public void initialize() {
        loadWorlds();
    }

    public HashMap<World, NovsWorld> getWorlds(){
        return worlds;
    }

    public NovsWorld getLobbyWorld() {
        World bukkitWorld = Bukkit.getServer().getWorld(novswar.getNovsConfigCache().getConfig("core").getString("core.world.lobby_world"));
        return worlds.get(bukkitWorld);
    }
    
    public ArrayList<Sign> getActiveSigns() {
        ArrayList<Sign> signs = new ArrayList<Sign>();
    	for (NovsWorld world : worlds.values()) {
    	    signs.addAll(world.getSigns().values());
        }
    	return signs;
    }

    /**
     * Searches for world based on BukkitWorld name
     * @param worldName
     * @return
     */
    public NovsWorld getWorldFromName(String worldName) {
    	for(NovsWorld world : worlds.values()) {
    		if(world.getBukkitWorld().getName().equals(worldName)) {
    			return world;
    		}
    	}
    	return null;
    }
    /*		DEPRECIATED
    private void loadLobbyWorld() {
        FileConfiguration coreConfig = novswar.getNovsConfigCache().getConfig("core");
        String worldName = coreConfig.getString("core.lobby.lobby_world");
        World bukkitWorld = novswar.getPlugin().getServer().getWorld(worldName);
        if (bukkitWorld == null) {
            Bukkit.createWorld(new WorldCreator(worldName));
            bukkitWorld = novswar.getPlugin().getServer().getWorld(worldName);
        }
        lobbyWorld = new NovsWorld(worldName, bukkitWorld);



        NovsTeam defaultTeam = novswar.getNovsTeamCache().getDefaultTeam();
        int spawnX = coreConfig.getInt("core.lobby.spawn.x");
        int spawnY = coreConfig.getInt("core.lobby.spawn.y");
        int spawnZ = coreConfig.getInt("core.lobby.spawn.z");
        lobbyWorld.getTeamSpawns().put(defaultTeam, new Location(bukkitWorld, spawnX, spawnY, spawnZ));
    }*/

    private void loadWorlds() {
    	//Generate lobby world
    	FileConfiguration coreConfig = novswar.getNovsConfigCache().getConfig("core");
        String lobbyWorldName = coreConfig.getString("core.world.lobby_world");
        World bukkitWorld = novswar.getPlugin().getServer().getWorld(lobbyWorldName);
        if (bukkitWorld == null) {
            Bukkit.createWorld(new WorldCreator(lobbyWorldName));
            bukkitWorld = novswar.getPlugin().getServer().getWorld(lobbyWorldName);
        }
        worlds.put(bukkitWorld, new NovsWorld(lobbyWorldName, bukkitWorld));

        //Generate all worlds - ensure enabled_worlds are defined in Worlds.yml
        FileConfiguration worldConfig = novswar.getNovsConfigCache().getConfig("worlds");
        Set<String> allWorldNames = worldConfig.getConfigurationSection("worlds").getKeys(false);
        List<String> enabledWorldNames = novswar.getNovsConfigCache().getConfig("core").getStringList("core.world.enabled_worlds");
        for (String worldName : enabledWorldNames) {
        	if(allWorldNames.contains(worldName)) {
        		World world = novswar.getPlugin().getServer().getWorld(worldName);
                if (world == null) {
                    Bukkit.createWorld(new WorldCreator(worldName));
                    world = novswar.getPlugin().getServer().getWorld(worldName);
                }
                String name = worldConfig.getString("worlds."+worldName+".name");
                NovsWorld novsWorld = new NovsWorld(name, world);
                worlds.put(world, novsWorld);
        	} else {
        		System.out.println("WARNING: World "+worldName+" is specified in enabled_worlds but is not defined in Worlds.yml");
        	}
        }

        //Load regions for all worlds
        for(NovsWorld novsworld : worlds.values()) {
        	loadRegions(novsworld);
        }
    }

    private void loadRegions(NovsWorld world) {
        FileConfiguration regionsConfig = novswar.getNovsConfigCache().getConfig("regions");
        if (regionsConfig.get("regions."+world.getBukkitWorld().getName()) == null) {
        	System.out.println("There is no region section for world "+world.getBukkitWorld().getName()+". NovsWar will create this section when regions are made.");
            return;
        }

        Set<String> teamNames = regionsConfig.getConfigurationSection("regions."+world.getBukkitWorld().getName()+".spawns").getKeys(false);
        for (String teamName : teamNames) {
            double x = novswar.getNovsConfigCache().getConfig("regions").getDouble("regions."+world.getBukkitWorld().getName()+".spawns."+teamName+".x");
            double y = novswar.getNovsConfigCache().getConfig("regions").getDouble("regions."+world.getBukkitWorld().getName()+".spawns."+teamName+".y");
            double z = novswar.getNovsConfigCache().getConfig("regions").getDouble("regions."+world.getBukkitWorld().getName()+".spawns."+teamName+".z");
            float pitch = (float) novswar.getNovsConfigCache().getConfig("regions").getDouble("regions."+world.getBukkitWorld().getName()+".spawns."+teamName+".pitch");
            float yaw = (float) novswar.getNovsConfigCache().getConfig("regions").getDouble("regions."+world.getBukkitWorld().getName()+".spawns."+teamName+".yaw");
            NovsTeam team = novswar.getNovsTeamCache().getTeam(teamName);

            world.getTeamSpawns().put(team, new Location(world.getBukkitWorld(), x, y, z, pitch, yaw));
        }

        List<Map<?,?>> signInfoMaps = regionsConfig.getMapList("regions."+world.getBukkitWorld().getName()+".signs");
        for (Map<?, ?> signInfoMap : signInfoMaps) {
            HashMap<String, Double> signInfo = (HashMap<String, Double>) signInfoMap;
            double x = signInfo.get("x");
            double y = signInfo.get("y");
            double z = signInfo.get("z");

            Location location = new Location(world.getBukkitWorld(), x, y, z);
            //Sign sign = null;
            if (location.getBlock().getState() instanceof Sign) {
                Sign sign = (Sign) location.getBlock().getState();
                world.getSigns().put(location, sign);
                System.out.println("Loaded Info Sign at "+location.toString());
            } else {
            	System.out.println("Oops! Tried to load an info sign at "+location.toString()+" but the block wasn't a sign!");
            	System.out.println("The block is "+location.getBlock().getState().toString()+", "+location.getBlock().getState().getType());
            }
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
                    new Location(world.getBukkitWorld(), cornerTwoX, cornerTwoY, cornerTwoZ),
                    type);

            region.saveBlocks(); //TODO: possibly move this?
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

    /**
     * Sets all spawn, infosign and region coordinates in the regions.yml file.
     * Removes infosign keys that are not present in the corresponding world section
     */
    public void saveRegions() {
        FileConfiguration regionsConfig = novswar.getNovsConfigCache().getConfig("regions");
        regionsConfig.set("regions", null); // reset regions config

        ConfigurationSection root = regionsConfig.createSection("regions");
        //Create map with all worlds (game worlds and lobby) so we save everything

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

            ArrayList<HashMap<String, Double>> signs = new ArrayList<HashMap<String, Double>>();
            for (Location location : world.getSigns().keySet()) {
                HashMap<String, Double> map = new HashMap<String, Double>();
                map.put("x", location.getX());
                map.put("y", location.getY());
                map.put("z", location.getZ());
                signs.add(map);
            }
            worldSection.set("signs", signs);

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
