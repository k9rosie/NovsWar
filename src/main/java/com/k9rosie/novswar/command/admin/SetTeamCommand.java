package com.k9rosie.novswar.command.admin;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.config.MessagesConfig;
import com.k9rosie.novswar.event.NovsWarLeaveTeamEvent;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.team.NovsTeam;

import com.k9rosie.novswar.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTeamCommand implements NovsCommand {
	private String permissions;
	private String description;
	private int requiredNumofArgs;
	private boolean playerOnly;
	private NovsWar novsWar;

    public SetTeamCommand(NovsWar novsWar) {
        permissions = "novswar.command.admin.setteam";
        description = "Moves a player to the specified team";
        requiredNumofArgs = 2;
        playerOnly = false;
        this.novsWar = novsWar;
    }

    public void execute(CommandSender sender, String[] args) {
    	NovsPlayer player = novsWar.getPlayerManager().getPlayers().get(sender);
    	NovsPlayer targetPlayer = novsWar.getPlayerManager().getPlayer(args[2]);
    	NovsTeam targetTeam = novsWar.getTeamManager().getTeam(args[3]);
    	Game game = novsWar.getGameHandler().getGame();
    	if (targetPlayer != null && targetTeam != null) {
    		targetPlayer.getPlayerState().setTeam(targetTeam);
    		NovsWarLeaveTeamEvent invokeEvent = new NovsWarLeaveTeamEvent(targetPlayer, game);
            Bukkit.getPluginManager().callEvent(invokeEvent);
    	} else {
			ChatUtil.sendError(player, MessagesConfig.getInvalidParameters());
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
