package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.admin.AdminCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class CommandHandler implements CommandExecutor {

    private NovsWar novsWar;

    public CommandHandler(NovsWar novsWar) {
        this.novsWar = novsWar;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender.hasPermission("novswar.command")) {
                new BaseCommand(novsWar, sender).execute();
                return true;
            }
        }

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("admin")) {
                if (sender.hasPermission("novswar.command.admin")) {
                    new AdminCommand(novsWar, sender, args).execute();
                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("team")) {
                if (sender.hasPermission("novswar.command.team")) {
                    new TeamCommand(novsWar, sender, args).execute();
                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("stats")) {
                if (sender.hasPermission("novswar.command.stats")) {
                    new StatsCommand(novsWar, sender, args).execute();
                    return true;
                }
            }
        }


        return false;
    }
}
