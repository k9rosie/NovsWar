package com.k9rosie.novswar.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.game.GameState;
import com.k9rosie.novswar.model.NovsPlayer;

public class VoteCommand extends NovsCommand {

	private Game game;

    public VoteCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
        game = getNovsWar().getGameHandler().getGame();
    }

    public void execute() {
    	NovsPlayer player = getNovsWar().getPlayerManager().getNovsPlayer((Player) getSender());
    	if(game.getGameState().equals(GameState.POST_GAME)) {
	        if(player.hasVoted() == false) {
	        	player.getBukkitPlayer().sendMessage("Cast your Vote");
	    		player.getBukkitPlayer().openInventory(Game.getBallotBox().getBallots());
	        }
	        else {
	        	player.getBukkitPlayer().sendMessage("You have already voted");
	        }
    	}
    	else {
    		player.getBukkitPlayer().sendMessage("You cannot vote now!");
    	}
       
    }
}
