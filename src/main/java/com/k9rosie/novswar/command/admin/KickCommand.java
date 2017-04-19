package com.k9rosie.novswar.command.admin;

import com.k9rosie.novswar.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.event.NovsWarLeaveTeamEvent;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.team.NovsTeam;

public class KickCommand extends NovsCommand {
	private Game game;
	
    public KickCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
        game = getNovsWar().getGameHandler().getGame();
    }

    public void execute() {
    	NovsPlayer player = getNovsWar().getPlayerManager().getPlayers().get((Player) getSender());
    	NovsPlayer kickPlayer = getNovsWar().getPlayerManager().getPlayerFromName(getArgs()[2]);
    	NovsTeam defaultTeam = getNovsWar().getTeamManager().getDefaultTeam();
    	
    	if(kickPlayer != null) {
    		kickPlayer.setTeam(defaultTeam);
            kickPlayer.getBukkitPlayer().teleport(getNovsWar().getWorldManager().getLobbyWorld().getTeamSpawns().get(defaultTeam));
            kickPlayer.getBukkitPlayer().setHealth(player.getBukkitPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            kickPlayer.getBukkitPlayer().setFoodLevel(20);
            ChatUtil.sendNotice(kickPlayer.getBukkitPlayer(), "You have been kicked to the lobby");
            NovsWarLeaveTeamEvent invokeEvent = new NovsWarLeaveTeamEvent(kickPlayer, game);
            Bukkit.getPluginManager().callEvent(invokeEvent);
    	} else {
    		ChatUtil.sendError(player, "Invalid arguments. Format: /nw admin kick <Player Name>");
    	}
    }
}
