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
        	//Get command type. If args[0] is not a command, defaults to HELP
        	CommandType commandArg = CommandType.getCommand(args[0]);
            switch (commandArg) {
                case ADMIN:
                    if (sender.hasPermission(commandArg.permission())) {
                        new AdminCommand(novsWar, sender, args).execute();
                        return true;
                    }
                case TEAM:
                    if (sender.hasPermission(commandArg.permission())) {
                        new TeamCommand(novsWar, sender, args).execute();
                        return true;
                    }
                case PLAYER:
                    if (sender.hasPermission(commandArg.permission())) {
                        new PlayerCommand(novsWar, sender, args).execute();
                        return true;
                    }
                case STATS:
                    if (sender.hasPermission(commandArg.permission())) {
                        new PlayerCommand(novsWar, sender, args).execute();
                        return true;
                    }
                case JOIN:
                    if (sender.hasPermission(commandArg.permission())) {
                        new JoinCommand(novsWar, sender, args).execute();
                        return true;
                    } 
                case VOTE:
                    if (sender.hasPermission(commandArg.permission())) {
                        new VoteCommand(novsWar, sender, args).execute();
                        return true;
                    }
                case SPECTATE:
                    if (sender.hasPermission(commandArg.permission())) {
                    	new SpectateCommand(novsWar, sender, args).execute();
                    	return true;
                    }
                case LEAVE:
                    if (sender.hasPermission(commandArg.permission())) {
                        new LeaveCommand(novsWar, sender, args).execute();
                        return true;
                    }
                case MAP:
                    if (sender.hasPermission(commandArg.permission())) {
                        new MapCommand(novsWar, sender, args).execute();
                        return true;
                    }
                case HELP:
                    if (sender.hasPermission(commandArg.permission())) {
                        new HelpCommand(novsWar, sender, args).execute();
                        return true;
                    }
            }
        }
        return false;
    }
}
