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

public class KickCommand implements NovsCommand {
    private String permissions;
    private String description;
    private int requiredNumofArgs;
    private boolean playerOnly;
    private NovsWar novsWar;
	
    public KickCommand(NovsWar novsWar) {
        permissions = "novswar.command.admin.kick";
        description = "Kick a player from the game";
        requiredNumofArgs = 1;
        playerOnly = false;
        this.novsWar = novsWar;
    }

    public void execute(CommandSender sender, String[] args) {
    	NovsPlayer kickPlayer = novsWar.getPlayerManager().getPlayer(args[2]);
    	NovsTeam defaultTeam = novsWar.getTeamManager().getDefaultTeam();
    	Game game = novsWar.getGameHandler().getGame();
        if (kickPlayer != null) {
    		kickPlayer.getPlayerState().setTeam(defaultTeam);
            kickPlayer.getBukkitPlayer().teleport(novsWar.getWorldManager().getLobbyWorld().getTeamSpawns().get(defaultTeam));
            kickPlayer.getBukkitPlayer().setHealth(kickPlayer.getBukkitPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            kickPlayer.getBukkitPlayer().setFoodLevel(20);
            ChatUtil.sendNotice(kickPlayer.getBukkitPlayer(), "You have been kicked to the lobby");
            NovsWarLeaveTeamEvent invokeEvent = new NovsWarLeaveTeamEvent(kickPlayer, game);
            Bukkit.getPluginManager().callEvent(invokeEvent);
    	} else {
    		ChatUtil.sendError(sender, "Player not found");
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
