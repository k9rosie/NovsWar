package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import org.bukkit.command.CommandSender;

public class BaseCommand implements ICommand {

    public NovsWar novsWar;
    public CommandSender sender;

    public BaseCommand(NovsWar novsWar, CommandSender sender) {
        this.novsWar = novsWar;
        this.sender = sender;
    }

    public void execute() {
        sender.sendMessage(new String[] {
                "§9NovsWar §av1.0.0",
                "§3§oA monolithic Bukkit plugin created for team-based PvP combat.",
                "§bWritten by k9rosie.",
                "§4Use §c/novswar help §4for more information"
        });
    }
}
