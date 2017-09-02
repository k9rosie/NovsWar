package com.k9rosie.novswar.world;

import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.team.NovsTeam;

import com.k9rosie.novswar.util.ChatUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class NovsWorld {

    private String name;
    private World bukkitWorld;
    private HashMap<NovsTeam, Location> teamSpawns;
    private TreeMap<String, NovsCuboid> cuboids;
    private HashMap<Location, Sign> signs;

    public NovsWorld(String name, World bukkitWorld) {
        this.name = name;
        this.bukkitWorld = bukkitWorld;
        teamSpawns = new HashMap<>();
        cuboids = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        signs = new HashMap<>();
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

    public TreeMap<String, NovsCuboid> getCuboids() {
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

    public void updateSigns(Game game) {
        for (Sign sign : signs.values()) {
            sign.setLine(0, ChatUtil.PLUGIN_TAG);
            sign.setLine(1, game.getWorld().getName());
            sign.setLine(2, game.getInGamePlayers().size() + " players");
        }
    }
}
