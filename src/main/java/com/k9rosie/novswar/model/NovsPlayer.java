package com.k9rosie.novswar.model;

import com.k9rosie.novswar.util.RegionType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class NovsPlayer {

    private Player bukkitPlayer;
    private NovsStats stats;
    private NovsTeam team;
    private boolean deathMessages;
    private boolean isDead;
    private boolean isSpectating;
    private boolean isSettingRegion;
    private boolean hasVoted;

    private Location cornerOneBuffer;
    private RegionType regionTypeBuffer;
    private String regionNameBuffer;

    public NovsPlayer (Player bukkitPlayer, NovsTeam team) {
        this.bukkitPlayer = bukkitPlayer;
        this.team = team;
        stats = new NovsStats(this);
        deathMessages = true;
        isDead = false;
        isSpectating = false;
        isSettingRegion = false;
        hasVoted = false;
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public NovsStats getStats() {
        return stats;
    }
    
    public NovsTeam getTeam() {
    	return team;
    }
    
    public void setTeam(NovsTeam team) {
        this.team.getScoreboardTeam().removeEntry(bukkitPlayer.getDisplayName());
        team.getScoreboardTeam().addEntry(bukkitPlayer.getDisplayName());
    	this.team = team;
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
    
    public boolean hasVoted() {
        return hasVoted;
    }

    public void setVoted(boolean voted) {
        this.hasVoted = voted;
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