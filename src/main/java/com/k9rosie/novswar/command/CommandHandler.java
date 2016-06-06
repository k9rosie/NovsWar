package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {

    private NovsWar novsWar;

    public CommandHandler(NovsWar novsWar) {
        this.novsWar = novsWar;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            new BaseCommand(novsWar, sender).execute();
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("team")) {
                new TeamCommand(novsWar, sender).execute();
                return true;
            } else if (args[0].equalsIgnoreCase("stats")) {
                new StatsCommand(novsWar, sender).execute();
                return true;
            }
        }

        return false;
    }
}
