package com.k9rosie.novswar.command.admin;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.event.NovsWarLeaveTeamEvent;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.team.NovsTeam;

import com.k9rosie.novswar.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTeamCommand extends NovsCommand {

	private Game game;
	
    public SetTeamCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
        game = getNovsWar().getGameHandler().getGame();
    }

    public void execute() {
    	NovsPlayer player = getNovsWar().getPlayerManager().getPlayers().get(getSender());
    	NovsPlayer targetPlayer = getNovsWar().getPlayerManager().getPlayer(getArgs()[2]);
    	NovsTeam targetTeam = getNovsWar().getTeamManager().getTeam(getArgs()[3]);
    	if(targetPlayer != null && targetTeam != null) {
    		targetPlayer.getPlayerState().setTeam(targetTeam);
    		NovsWarLeaveTeamEvent invokeEvent = new NovsWarLeaveTeamEvent(targetPlayer, game);
            Bukkit.getPluginManager().callEvent(invokeEvent);
    	} else {
			ChatUtil.sendError(player, "Invalid arguments. Format: /nw admin setteam <Player Name> <Team Name>");
    	}
    }
}
