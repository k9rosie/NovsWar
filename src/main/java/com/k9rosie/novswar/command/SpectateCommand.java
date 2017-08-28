package com.k9rosie.novswar.command;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.game.GameState;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.team.NovsTeam;
import com.k9rosie.novswar.util.ChatUtil;

public class SpectateCommand implements NovsCommand {
	private String permissions;
	private String description;
	private int requiredNumofArgs;
	private boolean playerOnly;
	private NovsWar novsWar;

    public SpectateCommand(NovsWar novsWar) {
        permissions = "novswar.command.spectate";
        description = "Enter spectator mode";
        requiredNumofArgs = 0;
        playerOnly = true;
        this.novsWar = novsWar;
    }

    public void execute(CommandSender sender, String[] args) {
        NovsPlayer player = novsWar.getPlayerManager().getPlayers().get(sender);
        NovsTeam defaultTeam = novsWar.getTeamManager().getDefaultTeam();
        Game game = novsWar.getGameHandler().getGame();

        if (player.getPlayerState().isSpectating()) {
        	// Return the player to the lobby
        	player.getPlayerState().quitSpectating();
        } else {
        	if (player.getPlayerState().getTeam().equals(defaultTeam)) {
        		// Begin spectating
            	if (game.getGameState().equals(GameState.DURING_GAME) || game.getGameState().equals(GameState.PRE_GAME)) {
            		ArrayList<NovsPlayer> inGamePlayers = game.getInGamePlayers();
            		NovsPlayer target = inGamePlayers.get(0);
            		player.getBukkitPlayer().setGameMode(GameMode.SPECTATOR);
            		player.getPlayerState().setSpectating(true); //must occur AFTER gamemode change
            		player.getBukkitPlayer().teleport(target.getBukkitPlayer().getLocation());
            		player.getPlayerState().setSpectatorTarget(target);
            		ChatUtil.sendNotice(player, "Spectate next player with LSHIFT. F5 to change view.");
            		ChatUtil.sendBroadcast(player.getBukkitPlayer().getName()+" is spectating the round!");

            	} else {
            		ChatUtil.sendNotice(player, "You can only spectate during the round");
            	}
        	} else {
        		ChatUtil.sendNotice(player, "You can only spectate while in the Lobby");
        	}
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
