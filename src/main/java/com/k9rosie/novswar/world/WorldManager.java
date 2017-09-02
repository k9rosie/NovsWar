package com.k9rosie.novswar.world;


import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.config.*;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.team.NovsTeam;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.*;

public class WorldManager {

    private NovsWar novswar;

    private HashMap<World, NovsWorld> worlds;
    private NovsWorld lobbyWorld;

    public WorldManager(NovsWar novswar) {
        this.novswar = novswar;
        worlds = new HashMap<>();
    }

    public HashMap<World, NovsWorld> getWorlds(){
        return worlds;
    }

    public NovsWorld getWorld(World bukkitWorld) {
        return worlds.get(bukkitWorld);
    }

    public NovsWorld getLobbyWorld() {
        return lobbyWorld;
    }

    public NovsWorld getWorld(String worldName) {
    	for(NovsWorld world : worlds.values()) {
    		if(world.getBukkitWorld().getName().equals(worldName)) {
    			return world;
    		}
    	}
    	return null;
    }

    public void loadWorlds() {
        CoreConfig coreConfig = novswar.getCoreConfig();
        WorldsConfig worldsConfig = novswar.getWorldsConfig();
        Bukkit.getServer().createWorld(new WorldCreator(coreConfig.getLobbyWorld())); // load the world in bukkit
        // load lobby world
        World lobbyWorld = Bukkit.getServer().getWorld(coreConfig.getLobbyWorld());
        if (lobbyWorld == null) {
            NovsWar.error(coreConfig.getLobbyWorld() + " doesn't exist!");
            return;
        }

        NovsWorld world = new NovsWorld(coreConfig.getLobbyWorld(), lobbyWorld);
        worlds.put(lobbyWorld, world);
        this.lobbyWorld = world;

        // load worlds
        for (WorldData worldData : worldsConfig.getWorlds()) {
            Bukkit.getServer().createWorld(new WorldCreator(worldData.getWorld())); // load the world in bukkit
            World bukkitWorld = Bukkit.getServer().getWorld(worldData.getWorld());
            if (bukkitWorld == null) {
                NovsWar.error(worldData.getWorld() + " doesn't exist!");
                return;
            }
            worlds.put(bukkitWorld, new NovsWorld(worldData.getWorld(), bukkitWorld));
        }

        // load regions for each world
        for (NovsWorld novsworld : worlds.values()) {
            loadRegions(novsworld);
        }

        NovsWar.debug("All worlds loaded successfully");
    }

    private void loadRegions(NovsWorld world) {
        World bukkitWorld = world.getBukkitWorld();
        RegionsConfig regionsConfig = novswar.getConfigManager().getRegionsConfig();
        RegionData regionData = regionsConfig.getRegionData().get(world.getBukkitWorld().getName());

        if (regionData == null) { // if there's no region data for the world yet
            return;
        }

        for (SpawnData spawnData : regionData.getSpawns()) {
            NovsTeam team = novswar.getTeamManager().getTeam(spawnData.getTeam());
            Location location = new Location(bukkitWorld,
                    spawnData.getX(),
                    spawnData.getY(),
                    spawnData.getZ(),
                    spawnData.getPitch(),
                    spawnData.getYaw());

            world.getTeamSpawns().put(team, location);
        }

        for (CuboidData cuboidData : regionData.getCuboids()) {
             CuboidType cuboidType = CuboidType.parseString(cuboidData.getType());
             Location cornerOne = new Location(bukkitWorld,
                     cuboidData.getCornerOneX(),
                     cuboidData.getCornerOneY(),
                     cuboidData.getCornerOneZ());
             Location cornerTwo = new Location(bukkitWorld,
                     cuboidData.getCornerTwoX(),
                     cuboidData.getCornerTwoY(),
                     cuboidData.getCornerTwoZ());
             NovsCuboid cuboid = new NovsCuboid(world, cornerOne, cornerTwo, cuboidType);
             cuboid.setBlocks(cuboid.getCuboidBlocks());
             world.getCuboids().put(cuboidData.getName(), cuboid);
        }

        for (SignData signData : regionData.getSigns()) {
            Location location = new Location(world.getBukkitWorld(),
                    signData.getX(),
                    signData.getY(),
                    signData.getZ());
            Block block = world.getBukkitWorld().getBlockAt(location);
            Sign sign = (Sign) block.getState();

            world.getSigns().put(location, sign);
        }

        NovsWar.debug("Regions loaded for " + bukkitWorld.getName());
    }

    public void updateRegions() {
        RegionsConfig regionsConfig = novswar.getConfigManager().getRegionsConfig();
        HashMap<String, RegionData> regionData = regionsConfig.getRegionData();

        for (NovsWorld world : worlds.values()) {
            ArrayList<SpawnData> spawnData = new ArrayList<>();
            ArrayList<CuboidData> cuboidData = new ArrayList<>();
            ArrayList<SignData> signData = new ArrayList<>();

            // generate spawn data from novsworld and populate spawnData array list
            for (Map.Entry<NovsTeam, Location> entry : world.getTeamSpawns().entrySet()) {
                NovsTeam team = entry.getKey();
                Location location = entry.getValue();
                SpawnData data = new SpawnData(team.getTeamName(),
                        location.getX(),
                        location.getY(),
                        location.getZ(),
                        location.getPitch(),
                        location.getYaw());

                spawnData.add(data);
            }

            // generate cuboid data from novsworld and populate cuboidData array list
            for (Map.Entry<String, NovsCuboid> entry : world.getCuboids().entrySet()) {
                String cuboidName = entry.getKey();
                NovsCuboid cuboid = entry.getValue();
                CuboidData data = new CuboidData(cuboidName,
                        cuboid.getCuboidType().toString(),
                        cuboid.getCornerOne().getX(),
                        cuboid.getCornerOne().getY(),
                        cuboid.getCornerOne().getZ(),
                        cuboid.getCornerTwo().getX(),
                        cuboid.getCornerTwo().getY(),
                        cuboid.getCornerTwo().getZ());

                cuboidData.add(data);
            }

            for (Location location : world.getSigns().keySet()) {
                SignData data = new SignData(location.getX(),
                        location.getY(),
                        location.getZ());

                signData.add(data);
            }

            RegionData data = new RegionData(world.getName(), spawnData, cuboidData, signData);

            if (regionData.containsKey(world.getBukkitWorld().getName())) {
                regionData.replace(world.getBukkitWorld().getName(), data);
            } else {
                regionData.put(world.getBukkitWorld().getName(), data);
            }
        }
    }

    public void updateSigns(Game game) {
        for (NovsWorld world : worlds.values()) {
            world.updateSigns(game);
        }
    }
}
