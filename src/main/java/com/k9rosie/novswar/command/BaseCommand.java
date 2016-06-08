package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import org.bukkit.command.CommandSender;

public class BaseCommand extends NovsCommand {

    public BaseCommand(NovsWar novsWar, CommandSender sender) {
        super(novsWar, sender);
    }

    public void execute() {
        getSender().sendMessage(new String[] {
                "§9NovsWar §av1.0.0",
                "§3§oA monolithic Bukkit plugin created for team-based PvP combat.",
                "§bWritten by k9rosie.",
                "§4Use §c/novswar help §4for more information"
        });
    }
}
