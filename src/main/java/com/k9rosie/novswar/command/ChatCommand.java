package com.k9rosie.novswar.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.util.ChatUtil;

public class ChatCommand implements NovsCommand {
	private String permissions;
	private String description;
	private int requiredNumofArgs;
	private boolean playerOnly;
	private NovsWar novsWar;

	public ChatCommand(NovsWar novsWar) {
		permissions = "novswar.command.chat";
		description = "Switches chat mode between team and global";
		requiredNumofArgs = 0;
		playerOnly = true;
		this.novsWar = novsWar;
	}

    public void execute(CommandSender sender, String args[]) {
    	NovsPlayer player = novsWar.getPlayerManager().getPlayers().get(sender);

    	if(player.isTeamChat()) {
    		ChatUtil.sendNotice(player, "Chat mode: Team");
    		player.setTeamChat(true);
    	} else {
    		ChatUtil.sendNotice(player, "Chat mode: Global");
    		player.setTeamChat(false);
    	}
    }

    public String getPermissions() {
        return permissions;
    }

    public String getDescription() {
        return description;
    }

    public int getRequiredNumofArgs() {
        return requiredNumofArgs;
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }
}
