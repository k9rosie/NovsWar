package com.k9rosie.novswar.command.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.model.NovsWorld;
import com.k9rosie.novswar.util.Messages;

public class NextGameCommand extends NovsCommand{
	
	private Game game;
	
    public NextGameCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
        game = getNovsWar().getGameHandler().getGame();
    }

    public void execute() {
    	if (getArgs().length > 3) {
            getSender().sendMessage(Messages.INVALID_PARAMETERS.toString());
            return;
        }
    	
    	if(getArgs().length < 3) {
    		Bukkit.broadcastMessage("Forcing next game...");
        	game.nextGame(game.getBallotBox().nextWorld(game.getWorld()));
    		
    	} else {
    		NovsWorld world = getNovsWar().getNovsWorldCache().getWorldFromName(getArgs()[2]);
    		
        	if(world == null) {
        		String message = "";
        		
        		for(NovsWorld option : getNovsWar().getNovsWorldCache().getWorlds().values()) {
        			message += (option.getBukkitWorld().getName() + " ");
        		}
        		getSender().sendMessage("Invalid world name. Options are "+message);
        	}
        	Bukkit.broadcastMessage("Forcing next game to "+world.getBukkitWorld().getName());
        	game.nextGame(world);
    	}
    }
}
