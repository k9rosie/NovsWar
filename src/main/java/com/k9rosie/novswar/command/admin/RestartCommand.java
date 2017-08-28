package com.k9rosie.novswar.command.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.game.Game;

public class RestartCommand implements NovsCommand {
    private String permissions;
    private String description;
    private int requiredNumofArgs;
    private boolean playerOnly;
	private NovsWar novsWar;
	
    public RestartCommand(NovsWar novsWar) {
        permissions = "novswar.command.admin.restart";
        description = "Restarts the game";
        requiredNumofArgs = 0;
        playerOnly = false;
        this.novsWar = novsWar;
    }

    public void execute(CommandSender sender, String[] args) {
        Game game = novsWar.getGameHandler().getGame();
    	Bukkit.broadcastMessage("Forcing game restart...");
    	game.nextGame(game.getWorld());
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
