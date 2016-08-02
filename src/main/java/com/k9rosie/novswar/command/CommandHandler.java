package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.admin.AdminCommand;
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
            if (sender.hasPermission("novswar.command")) {
                new BaseCommand(novsWar, sender).execute();
                return true;
            }
        }

        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "admin":
                    if (sender.hasPermission("novswar.command.admin")) {
                        new AdminCommand(novsWar, sender, args).execute();
                        return true;
                    }
                case "team":
                    if (sender.hasPermission("novswar.command.team")) {
                        new TeamCommand(novsWar, sender, args).execute();
                        return true;
                    }
                case "player":
                    if (sender.hasPermission("novswar.command.player")) {
                        new PlayerCommand(novsWar, sender, args).execute();
                        return true;
                    }
                case "join":
                    if (sender.hasPermission("novswar.command.join")) {
                        new JoinCommand(novsWar, sender, args).execute();
                        return true;
                    } 
                case "vote":
                    if (sender.hasPermission("novswar.command.vote")) {
                        new VoteCommand(novsWar, sender, args).execute();
                        return true;
                    }
                case "spectate":
                    if (sender.hasPermission("novswar.command.spectate")) {
                        new SpectateCommand(novsWar, sender, args).execute();
                        return true;
                    }
            }
        }
        return false;
    }
}
