package com.k9rosie.novswar.command.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.model.NovsPlayer;

public class HelpAdminCommand extends NovsCommand {
	
	public HelpAdminCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
    }

    public void execute() {
        NovsPlayer player = getNovsWar().getNovsPlayerCache().getPlayers().get((Player) getSender());
        
        String message = "";
        for(AdminCommandType cmd : AdminCommandType.values()) {
        	message = "/nw admin "+cmd.toString().toLowerCase()+" "+cmd.arguments()+": "+cmd.description();
        	player.getBukkitPlayer().sendMessage(message);
        }
    }
}
