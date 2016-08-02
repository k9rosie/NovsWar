package com.k9rosie.novswar.model;

import com.k9rosie.novswar.util.RegionType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.material.MaterialData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NovsRegion {

    private NovsWorld world;
    private Location cornerOne;
    private Location cornerTwo;
    private RegionType regionType;
    private HashMap<BlockState, MaterialData> blocks;

    public NovsRegion(NovsWorld world, Location cornerOne, Location cornerTwo, RegionType regionType) {
        this.world = world;
        this.cornerOne = cornerOne;
        this.cornerTwo = cornerTwo;
        this.regionType = regionType;
        blocks = new HashMap<BlockState, MaterialData>();
    }

    public NovsWorld getWorld() {
        return world;
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

    public HashMap<BlockState, MaterialData> getBlocks() {
        return blocks;
    }

    public void setBlocks(HashMap<BlockState, MaterialData> blocks) {
        this.blocks = blocks;
    }

    public HashMap<BlockState, MaterialData> getCuboid() {
        HashMap<BlockState, MaterialData> blocks = new HashMap<BlockState, MaterialData>();

        int topBlockX = Math.max(cornerOne.getBlockX(), cornerTwo.getBlockX());
        int topBlockY = Math.max(cornerOne.getBlockY(), cornerTwo.getBlockY());
        int topBlockZ = Math.max(cornerOne.getBlockZ(), cornerTwo.getBlockZ());
        int bottomBlockX = Math.min(cornerOne.getBlockX(), cornerTwo.getBlockX());
        int bottomBlockY = Math.min(cornerOne.getBlockY(), cornerTwo.getBlockY());
        int bottomBlockZ = Math.min(cornerOne.getBlockZ(), cornerTwo.getBlockZ());

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int y = bottomBlockY; y <= topBlockY; y++) {
                for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                    BlockState block = cornerOne.getWorld().getBlockAt(x, y, z).getState();
                    MaterialData data = block.getData();
                    blocks.put(block, data);
                }
            }
        }

        return blocks;
    }

    public void saveBlocks() {
        blocks = getCuboid();
    }

    public boolean inRegion(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        int topX = Math.max(cornerOne.getBlockX(), cornerTwo.getBlockX());
        int topY = Math.max(cornerOne.getBlockY(), cornerTwo.getBlockY());
        int topZ = Math.max(cornerOne.getBlockZ(), cornerTwo.getBlockZ());
        int bottomX = Math.min(cornerOne.getBlockX(), cornerTwo.getBlockX());
        int bottomY = Math.min(cornerOne.getBlockY(), cornerTwo.getBlockY());
        int bottomZ = Math.min(cornerOne.getBlockZ(), cornerTwo.getBlockZ());

        return x >= bottomX && x <= topX && y >= bottomY && y <= topY && z >= bottomZ && z <= topZ;
    }

    public void resetBlocks() {
        for (Map.Entry<BlockState, MaterialData> entry : blocks.entrySet()) {
            entry.getKey().setData(entry.getValue());
            entry.getKey().update(true, false);
        }
    }

    public void setBlocksWithinCuboid(Material material) {
        int topBlockX = Math.max(cornerOne.getBlockX(), cornerTwo.getBlockX());
        int topBlockY = Math.max(cornerOne.getBlockY(), cornerTwo.getBlockY());
        int topBlockZ = Math.max(cornerOne.getBlockZ(), cornerTwo.getBlockZ());
        int bottomBlockX = Math.min(cornerOne.getBlockX(), cornerTwo.getBlockX());
        int bottomBlockY = Math.min(cornerOne.getBlockY(), cornerTwo.getBlockY());
        int bottomBlockZ = Math.min(cornerOne.getBlockZ(), cornerTwo.getBlockZ());

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int y = bottomBlockY; y <= topBlockY; y++) {
                for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                    Block block = cornerOne.getWorld().getBlockAt(x, y, z);
                    block.setType(material);
                }
            }
        }
    }

}
