package com.k9rosie.novswar.command.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsWorld;

public class NextGameCommand extends NovsCommand{
	
	private Game game;
	
    public NextGameCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
        game = getNovsWar().getGameHandler().getGame();
    }

    public void execute() {
    	NovsPlayer player = getNovsWar().getNovsPlayerCache().getPlayers().get((Player) getSender());
    	String mapName = getArgs()[2];
    	
    	if(mapName == null) { //There is no specified map
    		Bukkit.broadcastMessage("Forcing next game...");
        	game.nextGame(game.getBallotBox().nextWorld(game.getWorld()));
        	
    	} else { //There is a specific map to start
    		NovsWorld world = getNovsWar().getNovsWorldCache().getWorldFromName(mapName);
    		
        	if(world == null) {
        		String message = "";
        		
        		for(NovsWorld option : getNovsWar().getNovsWorldCache().getWorlds().values()) {
        			message += (option.getBukkitWorld().getName() + " ");
        		}
        		player.getBukkitPlayer().sendMessage("Invalid world name. Options are "+message);
        	}
        	Bukkit.broadcastMessage("Forcing next game to "+world.getBukkitWorld().getName());
        	game.nextGame(world);
    	}
    }
}
