package com.k9rosie.novswar.command.admin;

import com.k9rosie.novswar.config.MessagesConfig;
import com.k9rosie.novswar.util.ChatUtil;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.world.NovsWorld;

public class DelRegionCommand implements NovsCommand {
    private NovsWar novsWar;
    private CommandSender sender;
    private String regionName;
	
	public DelRegionCommand(NovsWar novsWar, CommandSender sender, String regionName) {
        this.novsWar = novsWar;
        this.sender = sender;
        this.regionName = regionName;
    }

    public void execute() {
	    Player bukkitPlayer = (Player) sender;
	    World bukkitWorld = bukkitPlayer.getWorld();
	    NovsWorld world = novsWar.getWorldManager().getWorlds().get(bukkitWorld);

	    if (world == null) {
	        ChatUtil.sendError(bukkitPlayer, "The world you're standing in isn't enabled in NovsWar");
	        return;
        }

        if (world.getCuboids().keySet().contains(regionName)) {
	        world.getCuboids().remove(regionName);
	        ChatUtil.sendNotice(bukkitPlayer, "Region "+regionName+" has been removed from "+world.getName());
        } else {
	        ChatUtil.sendError(bukkitPlayer, "Invalid region name");
        }
    }
}
