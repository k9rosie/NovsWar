package com.k9rosie.novswar.command;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.game.GameState;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.team.NovsTeam;
import com.k9rosie.novswar.util.ChatUtil;

public class SpectateCommand extends NovsCommand{
	private Game game;

    public SpectateCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
        game = getNovsWar().getGameHandler().getGame();
    }

    public void execute() {
        NovsPlayer player = getNovsWar().getPlayerManager().getPlayers().get((Player) getSender());
        NovsTeam defaultTeam = getNovsWar().getTeamManager().getDefaultTeam();
        
        if(player.isSpectating()) {
        	//Return the player to the lobby
        	game.quitSpectating(player);     
        } else {
        	if(player.getTeam().equals(defaultTeam)) {
        		//Begin spectating
            	if(game.getGameState().equals(GameState.DURING_GAME) || game.getGameState().equals(GameState.PRE_GAME)) {
            		ArrayList<NovsPlayer> inGamePlayers = getNovsWar().getPlayerManager().getGamePlayers();
            		NovsPlayer target = inGamePlayers.get(0);
            		player.getBukkitPlayer().setGameMode(GameMode.SPECTATOR);
            		player.setSpectating(true); //must occur AFTER gamemode change
            		player.getBukkitPlayer().teleport(target.getBukkitPlayer().getLocation());
            		game.setSpectatorTarget(player, target);
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
}
