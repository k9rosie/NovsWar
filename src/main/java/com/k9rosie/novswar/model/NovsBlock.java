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

        if (blockState instanceof InventoryHolder) {
            InventoryHolder container = (InventoryHolder) blockState;
            container.getInventory().setContents(inventoryContents);
        }

        if (blockState instanceof Sign) {
            Sign sign = (Sign) blockState;
            for (int i = 0; i >= signData.length; i++) {
                sign.setLine(i, signData[i]);
            }
        }
    }
}
