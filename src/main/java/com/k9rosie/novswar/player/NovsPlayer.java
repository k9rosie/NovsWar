package com.k9rosie.novswar.player;

import com.k9rosie.novswar.world.RegionBuffer;
import org.bukkit.entity.Player;

public class NovsPlayer {
    private Player bukkitPlayer;
    private PlayerState playerState;
    private NovsStats stats;
    private boolean deathMessages;
    private boolean settingRegion;
    private boolean teamChat;

    private RegionBuffer regionBuffer;

    public NovsPlayer (Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
        stats = new NovsStats(this);
        deathMessages = true;
        settingRegion = false;
        teamChat = false;
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

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public boolean isSettingRegion() {
        return settingRegion;
    }

    public void setSettingRegion(boolean settingRegion) {
        this.settingRegion = settingRegion;
    }

    public RegionBuffer getRegionBuffer() {
        return regionBuffer;
    }

    public void setRegionBuffer(RegionBuffer regionBuffer) {
        this.regionBuffer = regionBuffer;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public boolean isTeamChat() {
        return teamChat;
    }

    public void setTeamChat(boolean teamChat) {
        this.teamChat = teamChat;
    }

}