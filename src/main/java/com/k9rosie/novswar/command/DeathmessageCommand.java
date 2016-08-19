package com.k9rosie.novswar.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.util.ChatFormat;

public class DeathmessageCommand extends NovsCommand{


    public DeathmessageCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
    }

    public void execute() {
    	NovsPlayer player = getNovsWar().getNovsPlayerCache().getPlayers().get((Player) getSender());
    	if(player.canSeeDeathMessages()) {
    		ChatFormat.sendNotice(player, "Death messages disabled");
    		player.setDeathMessages(false);
    	} else {
    		ChatFormat.sendNotice(player, "Death messages enabled");
    		player.setDeathMessages(true);
    	}
    }
}
