package com.k9rosie.novswar.model;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.util.ChatUtil;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class NovsBlock {
    private Location location;
    private BlockState blockState;
    private Material material;
    private MaterialData materialData;

    // Directionable
    private BlockFace facing;


    // InventoryHolder
    private ItemStack[] inventoryContents;

    // Sign
    private String[] signText;

    // Banner
    private DyeColor bannerBaseColor;
    private ArrayList<Pattern> bannerPatterns;

    // Furnace
    private short furnaceBurnTime;
    private short furnaceCookTime;

    // BrewingStand
    private int brewingStandBrewingTime;
    private int brewingStandFuelLevel;

    // Beacon
    private PotionEffectType beaconPrimaryEffectType;
    private PotionEffectType beaconSecondaryEffectType;

    // CreatureSpawner
    private int creatureSpawnerDelay;
    private EntityType creatureSpawnerCreatureType;

    // NoteBlock
    private Note noteBlockNote;

    // Jukebox
    private Material jukeboxRecord;

    // Skull
    private OfflinePlayer skullOwningPlayer;
    private BlockFace skullRotation;
    private SkullType skullSkullType;

    // CommandBlock
    private String commandBlockCommand;
    private String commandBlockName;

    // EndGateway
    private boolean endGatewayExactTeleport;
    private Location endGatewayExitLocation;

    // FlowerPot
    private MaterialData flowerPotContents;

    public NovsBlock(Location location, BlockState blockState, Material material, MaterialData materialData) {
        this.location = location;
        this.blockState = blockState;
        this.material = material;
        this.materialData = materialData;
    }

    public void respawn() {
        location.getBlock().setType(material);
        blockState.setData(materialData);
        boolean update = blockState.update();
        if (!update) {
            System.out.println("block at "+location.getX()+", "+location.getY()+", "+location.getZ()+" update failed");
        }
                BlockState blockState = location.getBlock().getState();
                if (blockState instanceof InventoryHolder) {
                	ChatUtil.printDebug("Block at "+location.getBlock().getX()+", "+location.getBlock().getY()+", "+location.getBlock().getZ()+" is of type InventoryHolder. Respawning...");
                    InventoryHolder inventoryHolder = (InventoryHolder) blockState;
                    inventoryHolder.getInventory().setContents(inventoryContents);
                }

                if (blockState instanceof Sign) {
                	ChatUtil.printDebug("Block at "+location.getBlock().getX()+", "+location.getBlock().getY()+", "+location.getBlock().getZ()+" is of type Sign. Respawning...");
                    Sign sign = (Sign) blockState;
                    for (int i = 0; i < signText.length; i++) {
                        sign.setLine(i, signText[i]);
                    }
                }

                if (blockState instanceof Banner) {
                	ChatUtil.printDebug("Block at "+location.getBlock().getX()+", "+location.getBlock().getY()+", "+location.getBlock().getZ()+" is of type Banner. Respawning...");
                    Banner banner = (Banner) blockState;
                    banner.setBaseColor(bannerBaseColor);
                    banner.setPatterns(bannerPatterns);
                }

                if (blockState instanceof Furnace) {
                	ChatUtil.printDebug("Block at "+location.getBlock().getX()+", "+location.getBlock().getY()+", "+location.getBlock().getZ()+" is of type Furnace. Respawning...");
                    Furnace furnace = (Furnace) blockState;
                    furnace.setBurnTime(furnaceBurnTime);
                    furnace.setCookTime(furnaceCookTime);
                }

                if (blockState instanceof BrewingStand) {
                	ChatUtil.printDebug("Block at "+location.getBlock().getX()+", "+location.getBlock().getY()+", "+location.getBlock().getZ()+" is of type BrewingStand. Respawning...");
                    BrewingStand brewingStand = (BrewingStand) blockState;
                    brewingStand.setBrewingTime(brewingStandBrewingTime);
                    brewingStand.setFuelLevel(brewingStandFuelLevel);
                }

                if (blockState instanceof Beacon) {
                	ChatUtil.printDebug("Block at "+location.getBlock().getX()+", "+location.getBlock().getY()+", "+location.getBlock().getZ()+" is of type Beacon. Respawning...");
                    Beacon beacon = (Beacon) blockState;
                    beacon.setPrimaryEffect(beaconPrimaryEffectType);
                    beacon.setSecondaryEffect(beaconSecondaryEffectType);
                }

                if (blockState instanceof CreatureSpawner) {
                	ChatUtil.printDebug("Block at "+location.getBlock().getX()+", "+location.getBlock().getY()+", "+location.getBlock().getZ()+" is of type CreatureSpawner. Respawning...");
                    CreatureSpawner creatureSpawner = (CreatureSpawner) blockState;
                    creatureSpawner.setDelay(creatureSpawnerDelay);
                    creatureSpawner.setSpawnedType(creatureSpawnerCreatureType);
                }

                if (blockState instanceof NoteBlock) {
                	ChatUtil.printDebug("Block at "+location.getBlock().getX()+", "+location.getBlock().getY()+", "+location.getBlock().getZ()+" is of type NoteBlock. Respawning...");
                    NoteBlock noteBlock = (NoteBlock) blockState;
                    noteBlock.setNote(noteBlockNote);
                }

                if (blockState instanceof Jukebox) {
                	ChatUtil.printDebug("Block at "+location.getBlock().getX()+", "+location.getBlock().getY()+", "+location.getBlock().getZ()+" is of type Jukebox. Respawning...");
                    Jukebox jukebox = (Jukebox) blockState;
                    jukebox.setPlaying(jukeboxRecord);
                }

                if (blockState instanceof Skull) {
                	ChatUtil.printDebug("Block at "+location.getBlock().getX()+", "+location.getBlock().getY()+", "+location.getBlock().getZ()+" is of type Skull. Respawning...");
                    Skull skull = (Skull) blockState;
                    skull.setOwningPlayer(skullOwningPlayer);
                    skull.setRotation(skullRotation);
                    skull.setSkullType(skullSkullType);
                }

                if (blockState instanceof CommandBlock) {
                	ChatUtil.printDebug("Block at "+location.getBlock().getX()+", "+location.getBlock().getY()+", "+location.getBlock().getZ()+" is of type CommandBlock. Respawning...");
                    CommandBlock commandBlock = (CommandBlock) blockState;
                    commandBlock.setCommand(commandBlockCommand);
                    commandBlock.setName(commandBlockName);
                }

                if (blockState instanceof EndGateway) {
                	ChatUtil.printDebug("Block at "+location.getBlock().getX()+", "+location.getBlock().getY()+", "+location.getBlock().getZ()+" is of type EndGateway. Respawning...");
                    EndGateway endGateway = (EndGateway) blockState;
                    endGateway.setExactTeleport(endGatewayExactTeleport);
                    endGateway.setExitLocation(endGatewayExitLocation);
                }

                if (blockState instanceof FlowerPot) {
                	ChatUtil.printDebug("Block at "+location.getBlock().getX()+", "+location.getBlock().getY()+", "+location.getBlock().getZ()+" is of type FlowerPot. Respawning...");
                    FlowerPot flowerPot = (FlowerPot) blockState;
                    flowerPot.setContents(flowerPotContents);
                }
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String[] getSignText() {
        return signText;
    }

    public void setSignText(String[] signText) {
        this.signText = signText;
    }

    public DyeColor getBannerBaseColor() {
        return bannerBaseColor;
    }

    public void setBannerBaseColor(DyeColor bannerBaseColor) {
        this.bannerBaseColor = bannerBaseColor;
    }

    public ArrayList<Pattern> getBannerPatterns() {
        return bannerPatterns;
    }

    public void setBannerPatterns(ArrayList<Pattern> bannerPatterns) {
        this.bannerPatterns = bannerPatterns;
    }

    public ItemStack[] getInventoryContents() {
        return inventoryContents;
    }

    public void setInventoryContents(ItemStack[] inventoryContents) {
        this.inventoryContents = inventoryContents;
    }

    public short getFurnaceBurnTime() {
        return furnaceBurnTime;
    }

    public void setFurnaceBurnTime(short furnaceBurnTime) {
        this.furnaceBurnTime = furnaceBurnTime;
    }

    public short getFurnaceCookTime() {
        return furnaceCookTime;
    }

    public void setFurnaceCookTime(short furnaceCookTime) {
        this.furnaceCookTime = furnaceCookTime;
    }

    public int getBrewingStandBrewingTime() {
        return brewingStandBrewingTime;
    }

    public void setBrewingStandBrewingTime(int brewingStandBrewingTime) {
        this.brewingStandBrewingTime = brewingStandBrewingTime;
    }

    public int getBrewingStandFuelLevel() {
        return brewingStandFuelLevel;
    }

    public void setBrewingStandFuelLevel(int brewingStandFuelLevel) {
        this.brewingStandFuelLevel = brewingStandFuelLevel;
    }

    public PotionEffectType getBeaconPrimaryEffectType() {
        return beaconPrimaryEffectType;
    }

    public void setBeaconPrimaryEffectType(PotionEffectType beaconPrimaryEffectType) {
        this.beaconPrimaryEffectType = beaconPrimaryEffectType;
    }

    public PotionEffectType getBeaconSecondaryEffectType() {
        return beaconSecondaryEffectType;
    }

    public void setBeaconSecondaryEffectType(PotionEffectType beaconSecondaryEffectType) {
        this.beaconSecondaryEffectType = beaconSecondaryEffectType;
    }

    public int getCreatureSpawnerDelay() {
        return creatureSpawnerDelay;
    }

    public void setCreatureSpawnerDelay(int creatureSpawnerDelay) {
        this.creatureSpawnerDelay = creatureSpawnerDelay;
    }

    public EntityType getCreatureSpawnerCreatureType() {
        return creatureSpawnerCreatureType;
    }

    public void setCreatureSpawnerCreatureType(EntityType creatureSpawnerCreatureType) {
        this.creatureSpawnerCreatureType = creatureSpawnerCreatureType;
    }

    public Note getNoteBlockNote() {
        return noteBlockNote;
    }

    public void setNoteBlockNote(Note noteBlockNote) {
        this.noteBlockNote = noteBlockNote;
    }

    public Material getJukeboxRecord() {
        return jukeboxRecord;
    }

    public void setJukeboxRecord(Material jukeboxRecord) {
        this.jukeboxRecord = jukeboxRecord;
    }

    public OfflinePlayer getSkullOwningPlayer() {
        return skullOwningPlayer;
    }

    public void setSkullOwningPlayer(OfflinePlayer skullOwningPlayer) {
        this.skullOwningPlayer = skullOwningPlayer;
    }

    public BlockFace getSkullRotation() {
        return skullRotation;
    }

    public void setSkullRotation(BlockFace skullRotation) {
        this.skullRotation = skullRotation;
    }

    public SkullType getSkullSkullType() {
        return skullSkullType;
    }

    public void setSkullSkullType(SkullType skullSkullType) {
        this.skullSkullType = skullSkullType;
    }

    public String getCommandBlockCommand() {
        return commandBlockCommand;
    }

    public void setCommandBlockCommand(String commandBlockCommand) {
        this.commandBlockCommand = commandBlockCommand;
    }

    public String getCommandBlockName() {
        return commandBlockName;
    }

    public void setCommandBlockName(String commandBlockName) {
        this.commandBlockName = commandBlockName;
    }

    public boolean isEndGatewayExactTeleport() {
        return endGatewayExactTeleport;
    }

    public void setEndGatewayExactTeleport(boolean endGatewayExactTeleport) {
        this.endGatewayExactTeleport = endGatewayExactTeleport;
    }

    public Location getEndGatewayExitLocation() {
        return endGatewayExitLocation;
    }

    public void setEndGatewayExitLocation(Location endGatewayExitLocation) {
        this.endGatewayExitLocation = endGatewayExitLocation;
    }

    public MaterialData getFlowerPotContents() {
        return flowerPotContents;
    }

    public void setFlowerPotContents(MaterialData flowerPotContents) {
        this.flowerPotContents = flowerPotContents;
    }
}
