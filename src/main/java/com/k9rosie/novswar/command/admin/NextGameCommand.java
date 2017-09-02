package com.k9rosie.novswar.command.admin;

import com.k9rosie.novswar.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.world.NovsWorld;

public class NextGameCommand implements NovsCommand {
	private String permissions;
	private String description;
	private int requiredNumofArgs;
	private boolean playerOnly;
	private NovsWar novsWar;
	
    public NextGameCommand(NovsWar novsWar) {
        permissions = "novswar.command.admin.nextgame";
        description = "Forces the next round.";
        requiredNumofArgs = 0;
        playerOnly = false;
        this.novsWar = novsWar;
    }

    public void execute(CommandSender sender, String[] args) {
        Game game = novsWar.getGameHandler().getGame();
        if (args.length < 3) { // no args
    		Bukkit.broadcastMessage("Forcing next game...");
        	game.nextGame(game.nextWorld(game.getWorld()));
    	} else {
    		NovsWorld world = novsWar.getWorldManager().getWorld(args[2]);
    		
        	if(world == null) {
        		String message = "";
        		
        		for(NovsWorld option : novsWar.getWorldManager().getWorlds().values()) {
        			message += (option.getBukkitWorld().getName() + " ");
        		}
				ChatUtil.sendError(sender, "Invalid world name. Options are "+message);
        	}
        	Bukkit.broadcastMessage("Forcing next game to "+world.getBukkitWorld().getName());
        	game.nextGame(world);
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
