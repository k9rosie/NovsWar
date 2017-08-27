package com.k9rosie.novswar.command.admin;

import com.k9rosie.novswar.util.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.player.NovsPlayer;

public class HelpAdminCommand implements NovsCommand {
	
	public HelpAdminCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
    }

    public void execute() {
        NovsPlayer player = getNovsWar().getPlayerManager().getPlayers().get((Player) getSender());
        
        String message = "";
        for(AdminCommandType cmd : AdminCommandType.values()) {
        	message = "/nw admin "+cmd.toString().toLowerCase()+" "+cmd.arguments()+": "+cmd.description();
            ChatUtil.sendNotice(player.getBukkitPlayer(), message);
        }
    }
}
