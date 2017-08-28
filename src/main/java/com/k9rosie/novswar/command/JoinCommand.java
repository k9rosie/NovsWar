package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.util.ChatUtil;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements NovsCommand {
    private String permissions;
    private String description;
    private int requiredNumofArgs;
    private boolean playerOnly;
    private NovsWar novsWar;

    public JoinCommand(NovsWar novsWar) {
        permissions = "novswar.command.join";
        description = "Join the current round";
        requiredNumofArgs = 0;
        playerOnly = true;
        this.novsWar = novsWar;
    }

    public void execute(CommandSender sender, String[] args) {
        NovsPlayer player = novsWar.getPlayerManager().getPlayers().get(sender);
        Game game = novsWar.getGameHandler().getGame();

        if (player.getPlayerState().isSpectating()) {
        	ChatUtil.sendNotice(player, "You cannot join while spectating");
        } else {
        	game.joinGame(player);
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
