package com.k9rosie.novswar.manager;


import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsInfoSign;
import com.k9rosie.novswar.model.NovsRegion;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.model.NovsWorld;
import com.k9rosie.novswar.util.RegionType;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
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
        //loadLobbyWorld();
    }

    public HashMap<World, NovsWorld> getWorlds(){
        return worlds;
    }

    public NovsWorld getLobbyWorld() {
        return lobbyWorld;
    }
    
    public HashMap<String, NovsInfoSign> getActiveInfoSigns() {
    	HashMap<String, NovsInfoSign> result = new HashMap<String, NovsInfoSign>();
    	result.putAll(lobbyWorld.getInfoSigns());
    	result.putAll(novswar.getGameHandler().getGame().getWorld().getInfoSigns());
    	return result;
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
    /*		DEPRECIATED
    private void loadLobbyWorld() {
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
    }*/

    private void loadWorlds() {
    	//Generate lobby world
    	FileConfiguration coreConfig = novswar.getConfigurationCache().getConfig("core");
        String lobbyWorldName = coreConfig.getString("core.lobby.lobby_world");
        World bukkitWorld = novswar.getPlugin().getServer().getWorld(lobbyWorldName);
        if (bukkitWorld == null) {
            Bukkit.createWorld(new WorldCreator(lobbyWorldName));
            bukkitWorld = novswar.getPlugin().getServer().getWorld(lobbyWorldName);
        }
        lobbyWorld = new NovsWorld(lobbyWorldName, bukkitWorld, true);
    	
        //Generate all worlds
        List<NovsWorld> allWorlds = new ArrayList<NovsWorld>();
        allWorlds.add(lobbyWorld);
        FileConfiguration worldConfig = novswar.getConfigurationCache().getConfig("worlds");
        List<String> enabledWorldNames = novswar.getConfigurationCache().getConfig("core").getStringList("core.world.enabled_worlds");
        for (String worldName : enabledWorldNames) {
            World world = novswar.getPlugin().getServer().getWorld(worldName);
            if (world == null) {
                Bukkit.createWorld(new WorldCreator(worldName));
                world = novswar.getPlugin().getServer().getWorld(worldName);
            }
            String name = worldConfig.getString("worlds."+worldName+".name");
            NovsWorld novsWorld = new NovsWorld(name, world, false);
            allWorlds.add(novsWorld);
            worlds.put(world, novsWorld);
        }
        
        //Load regions for all worlds
        for(NovsWorld novsworld : allWorlds) {
        	loadRegions(novsworld);
        }
    }

    private void loadRegions(NovsWorld world) {
        FileConfiguration regionsConfig = novswar.getConfigurationCache().getConfig("regions");
        if (regionsConfig.get("regions."+world.getBukkitWorld().getName()) == null) {
        	System.out.println("There is no region section for world "+world.getBukkitWorld().getName());
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
        //For the Lobby, this code runs when there is no Teamless spawn defined in the config
        if(world.isDefault()) {
        	if(world.getTeamSpawns().size() == 0) {
        		NovsTeam defaultTeam = novswar.getTeamManager().getDefaultTeam();
        		world.getTeamSpawns().put(defaultTeam, world.getBukkitWorld().getSpawnLocation());
        		System.out.println("There was no defined spawn point for Teamless! Spawning at bukkit world spawn.");
        	}
        }
        
        ConfigurationSection infoSignsSection = regionsConfig.getConfigurationSection("regions."+world.getBukkitWorld().getName()+".infosigns");
        for(String locationKey : infoSignsSection.getKeys(false)) {
        	int x = infoSignsSection.getInt(locationKey+".x");
        	int y = infoSignsSection.getInt(locationKey+".y");
        	int z = infoSignsSection.getInt(locationKey+".z");
        	//Verify there is a sign at this position
        	Block signBlock = world.getBukkitWorld().getBlockAt(x, y, z);
        	if(signBlock.getState() instanceof Sign) {
        		NovsInfoSign infoSign = new NovsInfoSign(signBlock);
            	world.getInfoSigns().put(locationKey, infoSign);
        	} else {
        		System.out.println("Uh Oh! Attempted to load a NovsInfoSign in world "+world.getName()+" at "+x+", "+y+", "+z+" but there was no sign block");
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

    /**
     * Sets all spawn, infosign and region coordinates in the regions.yml file.
     * Removes infosign keys that are not present in the corresponding world section
     */
    public void saveRegions() {
        FileConfiguration regionsConfig = novswar.getConfigurationCache().getConfig("regions");
        regionsConfig.set("regions", null);
        ConfigurationSection root = regionsConfig.createSection("regions");
        //Create map with all worlds (game worlds and lobby) so we save everything
        HashMap<World, NovsWorld> allWorlds = new HashMap<World, NovsWorld>();
        allWorlds.putAll(worlds);
		allWorlds.put(lobbyWorld.getBukkitWorld(), lobbyWorld);

        for (NovsWorld world : allWorlds.values()) {
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
            
            ConfigurationSection infoSignsSection = worldSection.createSection("infosigns");
            //Remove destroyed info signs from the config file
            Set<String> sectionSet = infoSignsSection.getKeys(false); //gets a set of location strings
            for(String path : sectionSet) {
            	if(world.getInfoSigns().keySet().contains(path)==false) {
            		infoSignsSection.set(path, null);
            	}
            }
            //Update valid info sign sections
            for(Map.Entry<String, NovsInfoSign> entry : world.getInfoSigns().entrySet()) {
            	//Create key that's the block location toString
            	ConfigurationSection signSection = infoSignsSection.createSection(entry.getKey().toString());
            	signSection.set("x", (int) entry.getValue().getBlock().getX());
            	signSection.set("y", (int) entry.getValue().getBlock().getY());
            	signSection.set("z", (int) entry.getValue().getBlock().getZ());
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
