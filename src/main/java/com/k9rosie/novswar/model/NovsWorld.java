package com.k9rosie.novswar.model;

import com.k9rosie.novswar.util.RegionType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class NovsWorld {

    private String name;
    private World bukkitWorld;
    private HashMap<NovsTeam, Location> teamSpawns;
    private HashMap<String, NovsRegion> regions;
    private HashMap<Location, Sign> signs;

    public NovsWorld(String name, World bukkitWorld) {
        this.name = name;
        this.bukkitWorld = bukkitWorld;
        teamSpawns = new HashMap<NovsTeam, Location>();
        regions = new HashMap<String, NovsRegion>();
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
    
    public Location getTeamSpawnLoc(NovsTeam team) {
    	Location loc;
    	if(teamSpawns.containsKey(team)) {
    		loc = teamSpawns.get(team);
    	} else {
    		loc = bukkitWorld.getSpawnLocation();
    		System.out.println("Oops! Tried to get team "+team.getTeamName()+" spawn but it wasn't in world "+name);
    	}
    	return loc;
    }

    public HashMap<String, NovsRegion> getRegions() {
        return regions;
    }
    
    public HashMap<Location, Sign> getSigns() {
    	return signs;
    }
    
    public HashSet<NovsRegion> getBattlefields() {
        HashSet<NovsRegion> battlefields = new HashSet<NovsRegion>();
        for (NovsRegion region : regions.values()) {
            if (region.getRegionType().equals(RegionType.BATTLEFIELD)) {
                battlefields.add(region);
            }
        }

        return battlefields;
    }
    
    public HashSet<NovsRegion> getDeathRegions() {
        HashSet<NovsRegion> battlefields = new HashSet<NovsRegion>();
        for (NovsRegion region : regions.values()) {
            if (region.getRegionType().equals(RegionType.DEATH_REGION)) {
                battlefields.add(region);
            }
        }

        return battlefields;
    }
    
    public HashSet<NovsRegion> getTeamSpawnRegions() {
        HashSet<NovsRegion> teamspawns = new HashSet<NovsRegion>();
        for (NovsRegion region : regions.values()) {
            if (region.getRegionType().equals(RegionType.TEAM_SPAWN)) {
            	teamspawns.add(region);
            }
        }

        return teamspawns;
    }
    
    public HashSet<NovsRegion> getIntermissionGates() {
        HashSet<NovsRegion> battlefields = new HashSet<NovsRegion>();
        for (NovsRegion region : regions.values()) {
            if (region.getRegionType().equals(RegionType.INTERMISSION_GATE)) {
                battlefields.add(region);
            }
        }

        return battlefields;
    }

    public void respawnBattlefields() {
        for (NovsRegion region : regions.values()) {
            if (region.getRegionType().equals(RegionType.BATTLEFIELD)) {
                region.resetBlocks();
            }
        }
    }

    public void openIntermissionGates() {
        for (NovsRegion region : regions.values()) {
            if (region.getRegionType().equals(RegionType.INTERMISSION_GATE)) {
                region.setBlocksWithinCuboid(Material.AIR);
            }
        }
    }

    public void closeIntermissionGates() {
        for (NovsRegion region : regions.values()) {
            if (region.getRegionType().equals(RegionType.INTERMISSION_GATE)) {
                region.resetBlocks();
            }
        }
    }
}
