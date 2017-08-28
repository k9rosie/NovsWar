package com.k9rosie.novswar.command.admin;

import com.k9rosie.novswar.config.MessagesConfig;
import com.k9rosie.novswar.util.ChatUtil;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.world.NovsWorld;

public class DelRegionCommand implements NovsCommand {
    private String permissions;
    private String description;
    private int requiredNumofArgs;
    private boolean playerOnly;
    private NovsWar novsWar;
	
	public DelRegionCommand(NovsWar novsWar) {
        permissions = "novswar.command.admin.delregion";
        description = "Deletes the specified region from the world";
        requiredNumofArgs = 1;
        playerOnly = true;
        this.novsWar = novsWar;
    }

    public void execute(CommandSender sender, String[] args) {
	    Player bukkitPlayer = (Player) sender;
	    World bukkitWorld = bukkitPlayer.getWorld();
	    NovsWorld world = novsWar.getWorldManager().getWorlds().get(bukkitWorld);
	    String regionName = args[2];

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
