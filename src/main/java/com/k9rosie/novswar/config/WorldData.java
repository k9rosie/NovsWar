package com.k9rosie.novswar.config;

public class WorldData {
    private String world;
    private String name;
    private String gamemode;
    private String[] enabledTeams;

    public WorldData(String world, String name, String gamemode, String[] enabledTeams) {
        this.world = world;
        this.name = name;
        this.gamemode = gamemode;
        this.enabledTeams = enabledTeams;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGamemode() {
        return gamemode;
    }

    public void setGamemode(String gamemode) {
        this.gamemode = gamemode;
    }

    public String[] getEnabledTeams() {
        return enabledTeams;
    }

    public void setEnabledTeams(String[] enabledTeams) {
        this.enabledTeams = enabledTeams;
    }
}
