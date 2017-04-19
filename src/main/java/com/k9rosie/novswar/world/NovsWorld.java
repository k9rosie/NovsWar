package com.k9rosie.novswar.world;

import com.k9rosie.novswar.team.NovsTeam;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;

import java.util.HashMap;
import java.util.HashSet;

public class NovsWorld {

    private String name;
    private World bukkitWorld;
    private HashMap<NovsTeam, Location> teamSpawns;
    private HashMap<String, NovsCuboid> cuboids;
    private HashMap<Location, Sign> signs;

    public NovsWorld(String name, World bukkitWorld) {
        this.name = name;
        this.bukkitWorld = bukkitWorld;
        teamSpawns = new HashMap<NovsTeam, Location>();
        cuboids = new HashMap<String, NovsCuboid>();
        signs = new HashMap<Location, Sign>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public World getBukkitWorld() {
        return bukkitWorld;
    }

    public HashMap<NovsTeam, Location> getTeamSpawns() {
        return teamSpawns;
    }
    
    /**
     * Saves all blocks in Battlefield and Intermission Gate cuboids
     */
    public void saveRegionBlocks() {
    	for (NovsCuboid region : cuboids.values()) {
    		if(region.getCuboidType().equals(CuboidType.BATTLEFIELD) || region.getCuboidType().equals(CuboidType.INTERMISSION_GATE)) {
    			region.saveBlocks();
    		}
    	}
    }
    
    public Location getTeamSpawnLoc(NovsTeam team) {
    	Location loc;
    	if(teamSpawns.containsKey(team)) {
    		loc = teamSpawns.get(team);
    	} else {
    		loc = bukkitWorld.getSpawnLocation();
    	}
    	return loc;
    }

    public HashMap<String, NovsCuboid> getCuboids() {
        return cuboids;
    }
    
    public HashMap<Location, Sign> getSigns() {
    	return signs;
    }
    
    public HashSet<NovsCuboid> getBattlefields() {
        HashSet<NovsCuboid> battlefields = new HashSet<NovsCuboid>();
        for (NovsCuboid region : cuboids.values()) {
            if (region.getCuboidType().equals(CuboidType.BATTLEFIELD)) {
                battlefields.add(region);
            }
        }

        return battlefields;
    }
    
    public HashSet<NovsCuboid> getDeathRegions() {
        HashSet<NovsCuboid> battlefields = new HashSet<NovsCuboid>();
        for (NovsCuboid region : cuboids.values()) {
            if (region.getCuboidType().equals(CuboidType.DEATH_REGION)) {
                battlefields.add(region);
            }
        }

        return battlefields;
    }
    
    public HashSet<NovsCuboid> getTeamSpawnRegions() {
        HashSet<NovsCuboid> teamspawns = new HashSet<NovsCuboid>();
        for (NovsCuboid region : cuboids.values()) {
            if (region.getCuboidType().equals(CuboidType.TEAM_SPAWN)) {
            	teamspawns.add(region);
            }
        }

        return teamspawns;
    }
    
    public HashSet<NovsCuboid> getIntermissionGates() {
        HashSet<NovsCuboid> battlefields = new HashSet<NovsCuboid>();
        for (NovsCuboid region : cuboids.values()) {
            if (region.getCuboidType().equals(CuboidType.INTERMISSION_GATE)) {
                battlefields.add(region);
            }
        }

        return battlefields;
    }

    public void respawnBattlefields() {
        for (NovsCuboid region : cuboids.values()) {
            if (region.getCuboidType().equals(CuboidType.BATTLEFIELD)) {
                region.resetBlocks();
            }
        }
    }

    public void openIntermissionGates() {
        for (NovsCuboid region : cuboids.values()) {
            if (region.getCuboidType().equals(CuboidType.INTERMISSION_GATE)) {
                region.setBlocksWithinCuboid(Material.AIR);
            }
        }
    }

    public void closeIntermissionGates() {
        for (NovsCuboid region : cuboids.values()) {
            if (region.getCuboidType().equals(CuboidType.INTERMISSION_GATE)) {
                region.resetBlocks();
            }
        }
    }
}
