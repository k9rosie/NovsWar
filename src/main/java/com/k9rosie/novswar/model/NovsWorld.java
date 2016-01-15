package com.k9rosie.novswar.model;

import com.k9rosie.novswar.util.Gamemode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class NovsWorld implements Listener {

    private String name;
    private Gamemode gamemode;
    private HashMap<NovsTeam, Location> teamSpawns;
    private ArrayList<NovsRegion> intermissionGateRegions;
    private ArrayList<NovsRegion> respawnGateRegions;

    public NovsWorld(String name, Gamemode gamemode) {
        this.name = name;
        this.gamemode = gamemode;
        teamSpawns = new HashMap<NovsTeam, Location>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gamemode getGamemode() {
        return gamemode;
    }

    public void setGamemode(Gamemode gamemode) {
        this.gamemode = gamemode;
    }

    public HashMap<NovsTeam, Location> getTeamSpawns() {
        return teamSpawns;
    }

    public ArrayList<NovsRegion> getIntermissionGateRegions() {
        return intermissionGateRegions;
    }

    public ArrayList<NovsRegion> getRespawnGateRegions() {
        return respawnGateRegions;
    }
}
