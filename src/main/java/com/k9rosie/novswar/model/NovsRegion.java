package com.k9rosie.novswar.model;

import com.k9rosie.novswar.util.RegionType;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NovsRegion {

    private NovsWorld world;
    private String name;
    private Location cornerOne;
    private Location cornerTwo;
    private RegionType regionType;
    private ArrayList<Block> blocks;

    public NovsRegion(NovsWorld world, String name, Location cornerOne, Location cornerTwo, RegionType regionType) {
        this.world = world;
        this.name = name;
        this.cornerOne = cornerOne;
        this.cornerTwo = cornerTwo;
        this.regionType = regionType;
        blocks = new ArrayList<Block>();
    }

    public NovsWorld getWorld() {
        return world;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getCornerOne() {
        return cornerOne;
    }

    public void setCornerOne(Location cornerOne) {
        this.cornerOne = cornerOne;
    }

    public Location getCornerTwo() {
        return cornerTwo;
    }

    public void setCornerTwo(Location cornerTwo) {
        this.cornerTwo = cornerTwo;
    }

    public RegionType getRegionType() {
        return regionType;
    }

    public void setRegionType(RegionType regionType) {
        this.regionType = regionType;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<Block> blocks) {
        this.blocks = blocks;
    }

    public ArrayList<Block> getCuboid() {
        ArrayList<Block> blocks = new ArrayList<Block>();

        int topBlockX = (cornerOne.getBlockX() < cornerTwo.getBlockX() ? cornerTwo.getBlockX() : cornerOne.getBlockX());
        int topBlockY = (cornerOne.getBlockY() < cornerTwo.getBlockY() ? cornerTwo.getBlockY() : cornerOne.getBlockY());
        int topBlockZ = (cornerOne.getBlockZ() < cornerTwo.getBlockZ() ? cornerTwo.getBlockZ() : cornerOne.getBlockZ());

        int bottomBlockX = (cornerOne.getBlockX() > cornerTwo.getBlockX() ? cornerTwo.getBlockX() : cornerOne.getBlockX());
        int bottomBlockY = (cornerOne.getBlockY() < cornerTwo.getBlockY() ? cornerTwo.getBlockY() : cornerOne.getBlockY());
        int bottomBlockZ = (cornerOne.getBlockZ() < cornerTwo.getBlockZ() ? cornerTwo.getBlockZ() : cornerOne.getBlockZ());

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int y = bottomBlockY; y <= topBlockY; y++) {
                for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                    Block block = cornerOne.getWorld().getBlockAt(x, y, z);
                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

    public void saveBlocks() {
        blocks = getCuboid();
    }

}
