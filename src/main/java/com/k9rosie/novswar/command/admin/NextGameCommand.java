package com.k9rosie.novswar.command.admin;

import com.k9rosie.novswar.config.MessagesConfig;
import com.k9rosie.novswar.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.world.NovsWorld;
import org.bukkit.entity.Player;

public class NextGameCommand extends NovsCommand{
	
	private Game game;
	
    public NextGameCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
        game = getNovsWar().getGameHandler().getGame();
    }

    public void execute() {
    	if (getArgs().length > 3) {
            ChatUtil.sendError((Player) getSender(), MessagesConfig.getInvalidParameters());
            return;
        }
    	
    	if(getArgs().length < 3) {
    		Bukkit.broadcastMessage("Forcing next game...");
        	game.nextGame(game.nextWorld(game.getWorld()));
    		
    	} else {
    		NovsWorld world = getNovsWar().getWorldManager().getWorldFromName(getArgs()[2]);
    		
        	if(world == null) {
        		String message = "";
        		
        		for(NovsWorld option : getNovsWar().getWorldManager().getWorlds().values()) {
        			message += (option.getBukkitWorld().getName() + " ");
        		}
				ChatUtil.sendError((Player) getSender(), "Invalid world name. Options are "+message);
        	}
        	Bukkit.broadcastMessage("Forcing next game to "+world.getBukkitWorld().getName());
        	game.nextGame(world);
    	}
    }
}
