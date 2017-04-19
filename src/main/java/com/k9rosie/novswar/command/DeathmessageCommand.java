package com.k9rosie.novswar.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.util.ChatUtil;

public class DeathmessageCommand extends NovsCommand{


    public DeathmessageCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
    }

    public void execute() {
    	NovsPlayer player = getNovsWar().getPlayerManager().getPlayers().get((Player) getSender());
    	if(player.canSeeDeathMessages()) {
    		ChatUtil.sendNotice(player, "Death messages disabled");
    		player.setDeathMessages(false);
    	} else {
    		ChatUtil.sendNotice(player, "Death messages enabled");
    		player.setDeathMessages(true);
    	}
    }
}
