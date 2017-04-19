package com.k9rosie.novswar.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.util.ChatUtil;

public class ChatCommand extends NovsCommand{
	

    public ChatCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
    }

    public void execute() {
    	NovsPlayer player = getNovsWar().getPlayerManager().getPlayers().get((Player) getSender());
    	if(player.isGlobalChat()) {
    		ChatUtil.sendNotice(player, "Chat mode: Team");
    		player.setGlobalChat(false);
    	} else {
    		ChatUtil.sendNotice(player, "Chat mode: Global");
    		player.setGlobalChat(true);
    	}
    }
}
