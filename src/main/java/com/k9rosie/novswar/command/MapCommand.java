package com.k9rosie.novswar.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.util.ChatUtil;

public class MapCommand implements NovsCommand {
    private String permissions;
    private String description;
    private int requiredNumofArgs;
    private boolean playerOnly;
    private NovsWar novsWar;

    public MapCommand(NovsWar novsWar) {
        permissions = "novswar.command.map";
        description = "Display map info.";
        requiredNumofArgs = 0;
        playerOnly = false;
        this.novsWar = novsWar;
    }

    public void execute(CommandSender sender, String[] args) {
        Game game = novsWar.getGameHandler().getGame();
        ChatUtil.sendNotice(sender, "Map: "+game.getWorld().getName()+" playing "+game.getGamemode().getGamemodeName());
    }

    @Override
    public String getPermissions() {
        return permissions;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getRequiredNumofArgs() {
        return requiredNumofArgs;
    }

    @Override
    public boolean isPlayerOnly() {
        return playerOnly;
    }
}
