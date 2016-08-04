package com.k9rosie.novswar.model;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.DirectionalContainer;
import org.bukkit.material.MaterialData;

public class NovsBlock {
    private BlockState blockState;
    private Location location;

    private ItemStack[] inventoryContents;
    private String[] signData;

    public NovsBlock(BlockState blockState, Location location) {
        this.blockState = blockState;
        this.location = location;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;
    }

    public ItemStack[] getInventoryContents() {
        return inventoryContents;
    }

    public void setInventoryContents(ItemStack[] inventoryContents) {
        this.inventoryContents = inventoryContents;
    }

    public String[] getSignData() {
        return signData;
    }

    public void setSignData(String[] signData) {
        this.signData = signData;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void respawn() {
        blockState.update(true, false);

        Block block = location.getBlock();

        if (block.getState() instanceof InventoryHolder) {
            System.out.println("Block at ("+block.getX()+", "+block.getY()+", "+block.getZ()+") is an InventoryHolder");
            InventoryHolder container = (InventoryHolder) blockState;
            container.getInventory().setContents(inventoryContents);
            System.out.println("This block being restored holds items! " + inventoryContents.length + " items restored");
        }

        if (block.getState() instanceof Sign) {
            System.out.println("Block at ("+block.getX()+", "+block.getY()+", "+block.getZ()+") is a Sign");
            Sign sign = (Sign) blockState;
            System.out.println("This block being restored is a sign! Here's the text: ");
            for (int i = 0; i < signData.length; i++) {
                sign.setLine(i, signData[i]);
                System.out.println(signData[i]);
            }

        }
    }
}
