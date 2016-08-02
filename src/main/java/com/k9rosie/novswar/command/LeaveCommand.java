package com.k9rosie.novswar.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.game.GameState;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;

public class LeaveCommand extends NovsCommand{
	private Game game;

    public LeaveCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
        game = getNovsWar().getGameHandler().getGame();
    }

    public void execute() {
        NovsPlayer player = getNovsWar().getPlayerManager().getPlayers().get((Player) getSender());
        NovsTeam defaultTeam = getNovsWar().getTeamManager().getDefaultTeam();
        if(game.getGameState().equals(GameState.DURING_GAME) || 
          game.getGameState().equals(GameState.PRE_GAME) ||
          game.getGameState().equals(GameState.PAUSED)) {
        	
        	if(player.getTeam().equals(defaultTeam)==false) {
        		player.setTeam(defaultTeam);
        		player.getBukkitPlayer().teleport(getNovsWar().getWorldManager().getLobbyWorld().getTeamSpawns().get(defaultTeam));
                player.getBukkitPlayer().setHealth(player.getBukkitPlayer().getMaxHealth());
                player.getBukkitPlayer().setFoodLevel(20);
        	} else {
        		player.getBukkitPlayer().sendMessage("You must be on a team to leave");
        	}
        } else {
        	player.getBukkitPlayer().sendMessage("You can only leave during the round");
        }
    }
}
