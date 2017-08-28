package com.k9rosie.novswar.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.util.ChatUtil;

public class DeathmessageCommand implements NovsCommand{
	private String permissions;
	private String description;
	private int requiredNumofArgs;
	private boolean playerOnly;
	private NovsWar novsWar;

    public DeathmessageCommand(NovsWar novsWar) {
    	permissions = "novswar.command.deathmessage";
    	description = "Toggles death messages being displayed in chat";
    	requiredNumofArgs = 0;
    	playerOnly = true;
    	this.novsWar = novsWar;
    }

    public void execute(CommandSender sender, String[] args) {
    	NovsPlayer player = novsWar.getPlayerManager().getPlayers().get(sender);

    	if (player.canSeeDeathMessages()) {
    		ChatUtil.sendNotice(player, "Death messages disabled");
    		player.setDeathMessages(false);
    	} else {
    		ChatUtil.sendNotice(player, "Death messages enabled");
    		player.setDeathMessages(true);
    	}
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
