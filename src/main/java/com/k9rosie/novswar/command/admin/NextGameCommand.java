package com.k9rosie.novswar.command.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.game.Game;

public class NextGameCommand extends NovsCommand{
	
	private Game game;
	
    public NextGameCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
        game = getNovsWar().getGameHandler().getGame();
    }

    public void execute() {
    	Bukkit.broadcastMessage("Forcing game restart...");
    	getNovsWar().getGameHandler().newGame(game.getWorld());
    }
}
