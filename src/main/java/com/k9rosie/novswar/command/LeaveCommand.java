package com.k9rosie.novswar.command;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.event.NovsWarLeaveTeamEvent;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.game.GameState;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.team.NovsTeam;
import com.k9rosie.novswar.util.ChatUtil;

public class LeaveCommand extends NovsCommand{
	private Game game;

    public LeaveCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
        game = getNovsWar().getGameHandler().getGame();
    }

    public void execute() {
        NovsPlayer player = getNovsWar().getPlayerManager().getPlayers().get((Player) getSender());
        NovsTeam defaultTeam = getNovsWar().getTeamManager().getDefaultTeam();
        if (game.getGameState().equals(GameState.DURING_GAME) ||
          game.getGameState().equals(GameState.PRE_GAME) ||
          game.isPaused()) {
        	
        	if(player.getPlayerState().getTeam().equals(defaultTeam) == false) {
        		player.getPlayerState().setTeam(defaultTeam);
        		player.getBukkitPlayer().teleport(getNovsWar().getWorldManager().getLobbyWorld().getTeamSpawns().get(defaultTeam));
                player.getBukkitPlayer().setHealth(player.getBukkitPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                player.getBukkitPlayer().setFoodLevel(20);
                NovsWarLeaveTeamEvent invokeEvent = new NovsWarLeaveTeamEvent(player, game);
                Bukkit.getPluginManager().callEvent(invokeEvent);
        	} else {
        		ChatUtil.sendNotice(player, "You must be on a team to leave");
        	}
        } else {
        	ChatUtil.sendNotice(player, "You can only leave during the round");
        }
    }
}
