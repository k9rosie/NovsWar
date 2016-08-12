package com.k9rosie.novswar.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;

public class ChatCommand extends NovsCommand{
	

    public ChatCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
    }

    public void execute() {
    	NovsPlayer player = getNovsWar().getNovsPlayerCache().getPlayers().get((Player) getSender());
    	if(player.isGlobalChat()) {
    		player.getBukkitPlayer().sendMessage("Chat mode: Team");
    		player.setGlobalChat(false);
    	} else {
    		player.getBukkitPlayer().sendMessage("Chat mode: Global");
    		player.setGlobalChat(true);
    	}
    }
}
