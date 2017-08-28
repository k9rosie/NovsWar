package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import org.bukkit.command.CommandSender;

public class BaseCommand implements NovsCommand {
    private String permissions;
    private String description;
    private int requiredNumofArgs;
    private boolean playerOnly;

    public BaseCommand() {
        permissions = "novswar.command";
        description = "Base command for the NovsWar plugin.";
        requiredNumofArgs = 0;
        playerOnly = false;
    }

    public void execute(CommandSender sender, String args[]) {
        sender.sendMessage(new String[] {
                "§9NovsWar §av1.0.0",
                "§3§oA monolithic Bukkit plugin created for team-based PvP combat.",
                "§bWritten by k9rosie and Rumsfield.",
                "§4Use §c/novswar help §4for more information"
        });
    }

    public String getPermissions() {
        return permissions;
    }

    public String getDescription() {
        return description;
    }

    public int getRequiredNumofArgs() {
        return requiredNumofArgs;
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }
}
