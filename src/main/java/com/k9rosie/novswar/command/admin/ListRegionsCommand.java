package com.k9rosie.novswar.command.admin;

import com.k9rosie.novswar.config.MessagesConfig;
import com.k9rosie.novswar.util.ChatUtil;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.world.NovsWorld;

public class ListRegionsCommand implements NovsCommand {
    private String permissions;
    private String description;
    private int requiredNumofArgs;
    private boolean playerOnly;
	private NovsWar novsWar;

	public ListRegionsCommand(NovsWar novsWar) {
	    permissions = "novswar.command.admin.listregions";
	    description = "Lists regions in the world you're currently standing in.";
	    requiredNumofArgs = 0;
	    playerOnly = true;
	    this.novsWar = novsWar;
    }

    public void execute(CommandSender sender, String[] args) {
        Player bukkitPlayer = (Player) sender;
        World bukkitWorld = bukkitPlayer.getWorld();
        NovsWorld world = novsWar.getWorldManager().getWorlds().get(bukkitWorld);

        if (world == null) {
            ChatUtil.sendError(bukkitPlayer, "The world you're in isn't enabled in NovsWar.");
            return;
        }

        String message = "";
        for(String name : world.getCuboids().keySet()) {
        	message += ("§a"+name+" ");
        }

        ChatUtil.sendNotice(bukkitPlayer, "Regions in this world are: "+message);
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
