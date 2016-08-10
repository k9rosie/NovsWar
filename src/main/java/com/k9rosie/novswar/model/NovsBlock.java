package com.k9rosie.novswar.model;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.DirectionalContainer;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;

public class NovsBlock {
    private Location location;
    private Material material;
    private MaterialData materialData;

    private String[] signText;
    private DyeColor bannerBaseColor;
    private ArrayList<Pattern> bannerPatterns;
    private ItemStack[] inventoryContents;
    private short furnaceBurnTime;
    private short furnaceCookTime;
    private int brewingStandBrewingTime;
    private int brewingStandFuelLevel;
    private PotionEffect beaconPrimaryEffect;
    private PotionEffect beaconSecondaryEffect;



    public NovsBlock(Location location, Material material, MaterialData materialData) {
        this.location = location;
        this.material = material;
        this.materialData = materialData;
    }

    public void respawn() {

    }
}
