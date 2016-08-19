package com.k9rosie.novswar.command;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.game.GameState;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.util.ChatUtil;

public class SpectateCommand extends NovsCommand{
	private Game game;

    public SpectateCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
        game = getNovsWar().getGameHandler().getGame();
    }

    public void execute() {
        NovsPlayer player = getNovsWar().getNovsPlayerCache().getPlayers().get((Player) getSender());
        NovsTeam defaultTeam = getNovsWar().getNovsTeamCache().getDefaultTeam();
        
        if(player.isSpectating()) {
        	//Return the player to the lobby
        	player.setSpectating(false); //must occur BEFORE gamemode change
        	player.getBukkitPlayer().teleport(getNovsWar().getNovsWorldCache().getLobbyWorld().getTeamSpawns().get(defaultTeam));
            player.getBukkitPlayer().setGameMode(GameMode.SURVIVAL);
            
        } else {
        	if(player.getTeam().equals(defaultTeam)) {
        		//Begin spectating
            	if(game.getGameState().equals(GameState.DURING_GAME) || game.getGameState().equals(GameState.PRE_GAME)) {
            		ArrayList<NovsPlayer> inGamePlayers = getNovsWar().getGameHandler().getGame().getGamePlayers();
            		NovsPlayer target = inGamePlayers.get(0);
            		player.getBukkitPlayer().setGameMode(GameMode.SPECTATOR);
            		player.setSpectating(true); //must occur AFTER gamemode change
            		player.getBukkitPlayer().teleport(target.getBukkitPlayer().getLocation());
            		player.setSpectatorTarget(target);
            		target.getSpectatorObservers().add(player);
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
