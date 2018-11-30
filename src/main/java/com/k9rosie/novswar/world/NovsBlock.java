package com.k9rosie.novswar.world;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.util.ChatUtil;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class NovsBlock {
    private Block block;
    private BlockData initialBlockData;

    public NovsBlock(Block block) {
        this.block = block;
        this.initialBlockData = block.getBlockData().clone();
    }

    public void resetBlock() {
        block.setBlockData(initialBlockData);
        block.getState().update();
    }
}
