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

    private ItemStack[] inventoryContents;
    private String[] signData;

    public NovsBlock(BlockState blockState) {
        this.blockState = blockState;
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

    public void respawn() {
        blockState.update(true, false);

        Block block = blockState.getBlock().getWorld().getBlockAt(blockState.getLocation());
        System.out.println(block.getState().getClass().getName());
        if (block.getState() instanceof InventoryHolder) {
            InventoryHolder container = (InventoryHolder) blockState;
            container.getInventory().setContents(inventoryContents);
            System.out.println("This block being restored holds items! " + inventoryContents.length + " items restored");
        }

        if (block.getState() instanceof Sign) {
            Sign sign = (Sign) blockState;
            System.out.println("This block being restored is a sign! Here's the text: ");
            for (int i = 0; i < signData.length; i++) {
                sign.setLine(i, signData[i]);
                System.out.println(signData[i]);
            }

        }
    }
}
