package com.k9rosie.novswar.model;

import com.k9rosie.novswar.gamemode.Gamemode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class NovsWorld {

    private String name;
    private World bukkitWorld;
    private HashMap<NovsTeam, Location> teamSpawns;
    private HashSet<NovsRegion> intermissionGateRegions;
    private HashSet<NovsRegion> respawnGateRegions;

    public NovsWorld(String name, World bukkitWorld) {
        this.name = name;
        this.bukkitWorld = bukkitWorld;
        teamSpawns = new HashMap<NovsTeam, Location>();
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

    public HashSet<NovsRegion> getIntermissionGateRegions() {
        return intermissionGateRegions;
    }

    public HashSet<NovsRegion> getRespawnGateRegions() {
        return respawnGateRegions;
    }
}
