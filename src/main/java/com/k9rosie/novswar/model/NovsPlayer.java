package com.k9rosie.novswar.model;

import com.k9rosie.novswar.util.RegionType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NovsPlayer {

    private Player bukkitPlayer;
    private NovsStats stats;
    private boolean deathMessages;
    private boolean isDead;
    private boolean isSpectating;
    private boolean isSettingRegion;

    private Location cornerOneBuffer;
    private RegionType regionTypeBuffer;
    private String regionNameBuffer;

    public NovsPlayer (Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
        stats = new NovsStats(this);
        deathMessages = true;
        isDead = false;
        isSpectating = false;
        isSettingRegion = false;
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public NovsStats getStats() {
        return stats;
    }

    public boolean canSeeDeathMessages() {
        return deathMessages;
    }

    public void setDeathMessages(boolean deathMessages) {
        this.deathMessages = deathMessages;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDeath(boolean isDead) {
        this.isDead = isDead;
    }

    public boolean isSpectating() {
        return isSpectating;
    }

    public void setSpectating(boolean isSpectating) {
        this.isSpectating = isSpectating;
    }

    public void setSettingRegion(boolean isSettingRegion) {
        this.isSettingRegion = isSettingRegion;
    }

    public boolean isSettingRegion() {
        return isSettingRegion;
    }

    public void setCornerOneBuffer(Location cornerOne) {
        cornerOneBuffer = cornerOne;
    }

    public Location getCornerOneBuffer() {
        return cornerOneBuffer;
    }

    public void setRegionTypeBuffer(RegionType regionType) {
        regionTypeBuffer = regionType;
    }

    public RegionType getRegionTypeBuffer() {
        return regionTypeBuffer;
    }

    public void setRegionNameBuffer(String regionName) {
        regionNameBuffer = regionName;
    }

    public String getRegionNameBuffer() {
        return regionNameBuffer;
    }
}