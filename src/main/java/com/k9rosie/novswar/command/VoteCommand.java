package com.k9rosie.novswar.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.game.GameState;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.util.ChatUtil;

public class VoteCommand implements NovsCommand {
    private String permissions;
    private String description;
    private int requiredNumofArgs;
    private boolean playerOnly;
    private NovsWar novsWar;

    public VoteCommand(NovsWar novsWar) {
        permissions = "novswar.command.vote";
        description = "Prompt the voting screen";
        requiredNumofArgs = 0;
        playerOnly = true;
        this.novsWar = novsWar;
    }

    public void execute(CommandSender sender, String[] args) {
    	NovsPlayer player = novsWar.getPlayerManager().getPlayers().get(sender);
    	Game game = novsWar.getGameHandler().getGame();

    	if (game.getGameState().equals(GameState.POST_GAME)) {
	        if (player.getPlayerState().hasVoted() == false) {
	        	ChatUtil.sendNotice(player, "Cast your Vote");
	    		player.getBukkitPlayer().openInventory(game.getBallotBox().getInventory());
	        } else {
	        	ChatUtil.sendNotice(player, "You have already voted");
	        }
    	} else {
    		ChatUtil.sendNotice(player, "You cannot vote now!");
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
