package com.k9rosie.novswar.config;

import java.util.ArrayList;

public class RegionData {
    private String world;
    private ArrayList<SpawnData> spawns;
    private ArrayList<CuboidData> cuboids;
    private ArrayList<SignData> signs;

    public RegionData(String world, ArrayList<SpawnData> spawns, ArrayList<CuboidData> cuboids, ArrayList<SignData> signs) {
        this.world = world;
        this.spawns = spawns;
        this.cuboids = cuboids;
        this.signs = signs;
    }

    public String getWorld() {
        return world;
    }

    public ArrayList<SpawnData> getSpawns() {
        return spawns;
    }

    public ArrayList<CuboidData> getCuboids() {
        return cuboids;
    }

    public ArrayList<SignData> getSigns() {
        return signs;
    }
}
