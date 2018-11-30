package com.k9rosie.novswar.world;

import com.k9rosie.novswar.player.NovsPlayer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.HashSet;

public class NovsCuboid {

    private NovsWorld world;
    private Location cornerOne;
    private Location cornerTwo;
    private CuboidType cuboidType;
    private ArrayList<NovsBlock> blocks;
    private HashSet<NovsPlayer> playersInCuboid;

    public NovsCuboid(NovsWorld world, Location cornerOne, Location cornerTwo, CuboidType cuboidType) {
        this.world = world;
        this.cornerOne = cornerOne;
        this.cornerTwo = cornerTwo;
        this.cuboidType = cuboidType;
        blocks = new ArrayList<>();
        playersInCuboid = new HashSet<>();
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

    public CuboidType getCuboidType() {
        return cuboidType;
    }

    public void setCuboidType(CuboidType cuboidType) {
        this.cuboidType = cuboidType;
    }

    public ArrayList<NovsBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<NovsBlock> blocks) {
        this.blocks = blocks;
    }
    
    public HashSet<NovsPlayer> getPlayersInCuboid() {
        return playersInCuboid;
    }

    public ArrayList<NovsBlock> getCuboidBlocks() {
        ArrayList<NovsBlock> blocks = new ArrayList<NovsBlock>();

        int topBlockX = Math.max(cornerOne.getBlockX(), cornerTwo.getBlockX());
        int topBlockY = Math.max(cornerOne.getBlockY(), cornerTwo.getBlockY());
        int topBlockZ = Math.max(cornerOne.getBlockZ(), cornerTwo.getBlockZ());
        int bottomBlockX = Math.min(cornerOne.getBlockX(), cornerTwo.getBlockX());
        int bottomBlockY = Math.min(cornerOne.getBlockY(), cornerTwo.getBlockY());
        int bottomBlockZ = Math.min(cornerOne.getBlockZ(), cornerTwo.getBlockZ());

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int y = bottomBlockY; y <= topBlockY; y++) {
                for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                    Block bukkitBlock = world.getBukkitWorld().getBlockAt(x, y, z);
                    NovsBlock block = new NovsBlock(bukkitBlock);
                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

    public void saveBlocks() {
        blocks = getCuboidBlocks();
    }

    /**
     * Determines whether a location is inside this region
     * @param location
     * @return
     */
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
        for (NovsBlock block : blocks) {
            block.resetBlock();
        }
    }

    /**
     * Sets all blocks inside this region to a given material
     * @param material
     */
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
