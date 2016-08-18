package com.k9rosie.novswar.model;

import com.k9rosie.novswar.util.RegionType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.MaterialData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class NovsRegion {

    private NovsWorld world;
    private Location cornerOne;
    private Location cornerTwo;
    private RegionType regionType;
    private ArrayList<NovsBlock> blocks;
    private HashSet<BlockState> regionBlockStates;
    private HashSet<NovsPlayer> playersInRegion;

    public NovsRegion(NovsWorld world, Location cornerOne, Location cornerTwo, RegionType regionType) {
        this.world = world;
        this.cornerOne = cornerOne;
        this.cornerTwo = cornerTwo;
        this.regionType = regionType;
        blocks = new ArrayList<NovsBlock>();
        regionBlockStates = new HashSet<BlockState>();
        playersInRegion = new HashSet<NovsPlayer>();
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
    
    public HashSet<BlockState> getBlockStates() {
    	return regionBlockStates;
    }

    public void setBlocks(ArrayList<NovsBlock> blocks) {
        this.blocks = blocks;
    }
    
    public HashSet<NovsPlayer> getPlayersInRegion() {
        return playersInRegion;
    }

    /**
     * NOT USED
     */
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
                    Block bukkitBlock = world.getBukkitWorld().getBlockAt(x, y, z);
                    NovsBlock block = createNovsBlock(bukkitBlock);
                    blocks.add(block);
                }
            }
        }

        return blocks;
    }
    /*
    public void saveBlocks() {
        blocks = getCuboid();
    }*/
    
    /**
     * Saves all block's BlockState inside the region
     */
    public void saveBlocks() {
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
                    regionBlockStates.add(bukkitBlock.getState());
                }
            }
        }
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

    /*
    public void resetBlocks() {
        for (NovsBlock block : blocks) {
            block.respawn();
        }
    }*/
    
    /**
     * Resets all block's BlockState inside the region
     */
    public void resetBlocks() {
    	boolean result;
    	for(BlockState bs : regionBlockStates) {
    		result = bs.update(true);
    		if(result == false) {
    			System.out.println("HOLY SHIT a blockstate update failed");
    		}
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

    /**
     * NOT USED
     */
    public NovsBlock createNovsBlock(Block bukkitBlock) {
        BlockState blockState = bukkitBlock.getState();
        NovsBlock block = new NovsBlock(bukkitBlock.getLocation(), bukkitBlock.getState(), bukkitBlock.getType(), bukkitBlock.getState().getData());

        if (blockState instanceof InventoryHolder) {
            System.out.println("Block at "+bukkitBlock.getX()+", "+bukkitBlock.getY()+", "+bukkitBlock.getZ()+" is of type InventoryHolder");
            InventoryHolder inventoryHolder = (InventoryHolder) blockState;
            block.setInventoryContents(inventoryHolder.getInventory().getContents());
        }

        if (blockState instanceof Sign) {
            System.out.println("Block at "+bukkitBlock.getX()+", "+bukkitBlock.getY()+", "+bukkitBlock.getZ()+" is of type Sign");
            Sign sign = (Sign) blockState;
            block.setSignText(sign.getLines());
        }

        if (blockState instanceof Banner) {
            System.out.println("Block at "+bukkitBlock.getX()+", "+bukkitBlock.getY()+", "+bukkitBlock.getZ()+" is of type Banner");
            Banner banner = (Banner) blockState;
            block.setBannerBaseColor(banner.getBaseColor());
            block.setBannerPatterns((ArrayList) banner.getPatterns());
        }

        if (blockState instanceof Furnace) {
            System.out.println("Block at "+bukkitBlock.getX()+", "+bukkitBlock.getY()+", "+bukkitBlock.getZ()+" is of type Furnace");
            Furnace furnace = (Furnace) blockState;
            block.setFurnaceBurnTime(furnace.getBurnTime());
            block.setFurnaceCookTime(furnace.getCookTime());
        }

        if (blockState instanceof BrewingStand) {
            System.out.println("Block at "+bukkitBlock.getX()+", "+bukkitBlock.getY()+", "+bukkitBlock.getZ()+" is of type BrewingStand");
            BrewingStand brewingStand = (BrewingStand) blockState;
            block.setBrewingStandBrewingTime(brewingStand.getBrewingTime());
            block.setBrewingStandFuelLevel(brewingStand.getFuelLevel());
        }

        if (blockState instanceof Beacon) {
            System.out.println("Block at "+bukkitBlock.getX()+", "+bukkitBlock.getY()+", "+bukkitBlock.getZ()+" is of type Beacon");
            Beacon beacon = (Beacon) blockState;
            if (beacon.getPrimaryEffect() != null) {
                block.setBeaconPrimaryEffectType(beacon.getPrimaryEffect().getType());
            }
            if (beacon.getSecondaryEffect() != null) {
                block.setBeaconSecondaryEffectType(beacon.getSecondaryEffect().getType());
            }
        }

        if (blockState instanceof CreatureSpawner) {
            System.out.println("Block at "+bukkitBlock.getX()+", "+bukkitBlock.getY()+", "+bukkitBlock.getZ()+" is of type CreatureSpawner");
            CreatureSpawner creatureSpawner = (CreatureSpawner) blockState;
            block.setCreatureSpawnerDelay(creatureSpawner.getDelay());
            block.setCreatureSpawnerCreatureType(creatureSpawner.getSpawnedType());
        }

        if (blockState instanceof NoteBlock) {
            System.out.println("Block at "+bukkitBlock.getX()+", "+bukkitBlock.getY()+", "+bukkitBlock.getZ()+" is of type NoteBlock");
            NoteBlock noteBlock = (NoteBlock) blockState;
            block.setNoteBlockNote(noteBlock.getNote());
        }

        if (blockState instanceof Jukebox) {
            System.out.println("Block at "+bukkitBlock.getX()+", "+bukkitBlock.getY()+", "+bukkitBlock.getZ()+" is of type Jukebox");
            Jukebox jukebox = (Jukebox) blockState;
            block.setJukeboxRecord(jukebox.getPlaying());
        }

        if (blockState instanceof Skull) {
            System.out.println("Block at "+bukkitBlock.getX()+", "+bukkitBlock.getY()+", "+bukkitBlock.getZ()+" is of type Skull");
            Skull skull = (Skull) blockState;
            block.setSkullOwningPlayer(skull.getOwningPlayer());
            block.setSkullRotation(skull.getRotation());
            block.setSkullSkullType(skull.getSkullType());
        }

        if (blockState instanceof CommandBlock) {
            System.out.println("Block at "+bukkitBlock.getX()+", "+bukkitBlock.getY()+", "+bukkitBlock.getZ()+" is of type CommandBlock");
            CommandBlock commandBlock = (CommandBlock) blockState;
            block.setCommandBlockCommand(commandBlock.getCommand());
            block.setCommandBlockName(commandBlock.getName());
        }

        if (blockState instanceof EndGateway) {
            System.out.println("Block at "+bukkitBlock.getX()+", "+bukkitBlock.getY()+", "+bukkitBlock.getZ()+" is of type EndGateway");
            EndGateway endGateway = (EndGateway) blockState;
            block.setEndGatewayExactTeleport(endGateway.isExactTeleport());
            block.setEndGatewayExitLocation(endGateway.getExitLocation());
        }

        if (blockState instanceof FlowerPot) {
            System.out.println("Block at "+bukkitBlock.getX()+", "+bukkitBlock.getY()+", "+bukkitBlock.getZ()+" is of type FlowerPot");
            FlowerPot flowerPot = (FlowerPot) blockState;
            block.setFlowerPotContents(flowerPot.getContents());
        }

        return block;
    }
}
