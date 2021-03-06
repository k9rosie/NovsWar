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

public class LeaveCommand implements NovsCommand {
    private String permissions;
    private String description;
    private int requiredNumofArgs;
    private boolean playerOnly;
	private NovsWar novsWar;

    public LeaveCommand(NovsWar novsWar) {
        permissions = "novswar.command.leave";
        description = "Leave the round";
        requiredNumofArgs = 0;
        playerOnly = true;
        this.novsWar = novsWar;
    }

    public void execute(CommandSender sender, String[] args) {
        NovsPlayer player = novsWar.getPlayerManager().getPlayers().get(sender);
        Game game = novsWar.getGameHandler().getGame();
        game.leaveGame(player);
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
