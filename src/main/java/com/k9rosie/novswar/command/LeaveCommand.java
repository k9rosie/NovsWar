package com.k9rosie.novswar.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.event.NovsWarLeaveTeamEvent;
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
        NovsPlayer player = getNovsWar().getNovsPlayerCache().getPlayers().get((Player) getSender());
        NovsTeam defaultTeam = getNovsWar().getNovsTeamCache().getDefaultTeam();
        if(game.getGameState().equals(GameState.DURING_GAME) || 
          game.getGameState().equals(GameState.PRE_GAME) ||
          game.isPaused()) {
        	
        	if(player.getTeam().equals(defaultTeam)==false) {
        		player.setTeam(defaultTeam);
        		player.getBukkitPlayer().teleport(getNovsWar().getNovsWorldCache().getLobbyWorld().getTeamSpawns().get(defaultTeam));
                player.getBukkitPlayer().setHealth(player.getBukkitPlayer().getMaxHealth());
                player.getBukkitPlayer().setFoodLevel(20);
                NovsWarLeaveTeamEvent invokeEvent = new NovsWarLeaveTeamEvent(player, game);
                Bukkit.getPluginManager().callEvent(invokeEvent);
        	} else {
        		player.getBukkitPlayer().sendMessage("You must be on a team to leave");
        	}
        } else {
        	player.getBukkitPlayer().sendMessage("You can only leave during the round");
        }
    }
}
