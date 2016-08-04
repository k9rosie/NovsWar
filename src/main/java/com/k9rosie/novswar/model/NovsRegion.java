package com.k9rosie.novswar.model;

import com.k9rosie.novswar.util.RegionType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.inventory.InventoryHolder;
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
    private ArrayList<NovsBlock> blocks;

    public NovsRegion(NovsWorld world, Location cornerOne, Location cornerTwo, RegionType regionType) {
        this.world = world;
        this.cornerOne = cornerOne;
        this.cornerTwo = cornerTwo;
        this.regionType = regionType;
        blocks = new ArrayList<NovsBlock>();
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

    public ArrayList<NovsBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<NovsBlock> blocks) {
        this.blocks = blocks;
    }

    public ArrayList<NovsBlock> getCuboid() {
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
                    Block bukkitBlock = getWorld().getBukkitWorld().getBlockAt(x, y, z);

                    NovsBlock block = new NovsBlock(bukkitBlock.getState(), bukkitBlock.getLocation());

                    if (bukkitBlock.getState() instanceof InventoryHolder) {
                        InventoryHolder container = (InventoryHolder) bukkitBlock.getState();
                        block.setInventoryContents(container.getInventory().getContents());
                        System.out.println("This block holds items! " + block.getInventoryContents().length + " items stored");
                    }

                    if (bukkitBlock.getState() instanceof Sign) {
                        Sign sign = (Sign) bukkitBlock.getState();
                        block.setSignData(sign.getLines());
                        System.out.println("This block is a sign! Here's the text: ");
                        for (String text : sign.getLines()) {
                            System.out.println(text);
                        }
                    }

                    blocks.add(block);
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
        for (NovsBlock block : blocks) {
            block.respawn();
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
