package com.k9rosie.novswar.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.game.GameState;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.util.ChatFormat;

public class VoteCommand extends NovsCommand {

	private Game game;

    public VoteCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
        game = getNovsWar().getGameHandler().getGame();
    }

    public void execute() {
    	NovsPlayer player = getNovsWar().getNovsPlayerCache().getPlayers().get((Player) getSender());
    	if(game.getGameState().equals(GameState.POST_GAME)) {
	        if(player.hasVoted() == false) {
	        	ChatFormat.sendNotice(player, "Cast your Vote");
	    		player.getBukkitPlayer().openInventory(game.getBallotBox().getBallots());
	        }
	        else {
	        	ChatFormat.sendNotice(player, "You have already voted");
	        }
    	}
    	else {
    		ChatFormat.sendNotice(player, "You cannot vote now!");
    	}
       
    }
}
