package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.admin.AdminCommand;
import com.k9rosie.novswar.util.ChatUtil;
import com.k9rosie.novswar.util.Messages;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    private NovsWar novsWar;

    public CommandHandler(NovsWar novsWar) {
        this.novsWar = novsWar;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getLogger().info("You need to be a player to issue commands.");
            return false;
        }
        if (args.length == 0) {
            if (sender.hasPermission("novswar.command")) {
                new BaseCommand(novsWar, sender).execute();
                return true;
            } else {
                ChatUtil.sendError((Player) sender, Messages.NO_PERMISSION.toString());
                return true;
            }
        }

        if (args.length >= 1) {
        	//Get command type. If args[0] is not a command, defaults to HELP
        	CommandType commandArg = CommandType.getCommand(args[0]);
        	if (sender.hasPermission(commandArg.permission())) {
        		switch (commandArg) {
                case ADMIN:
                    new AdminCommand(novsWar, sender, args).execute();
                    return true;
                case TEAM:
                    new TeamCommand(novsWar, sender, args).execute();
                    return true;
                case PLAYER:
                    new PlayerCommand(novsWar, sender, args).execute();
                    return true;
                case JOIN:
                    new JoinCommand(novsWar, sender, args).execute();
                    return true;
                case VOTE:
                    new VoteCommand(novsWar, sender, args).execute();
                    return true;
                case SPECTATE:
                	new SpectateCommand(novsWar, sender, args).execute();
                	return true;
                case LEAVE:
                    new LeaveCommand(novsWar, sender, args).execute();
                    return true;
                case MAP:
                    new MapCommand(novsWar, sender, args).execute();
                    return true;
                case HELP:
                    new HelpCommand(novsWar, sender, args).execute();
                    return true;
                case DEATHMESSAGE:
                    new DeathmessageCommand(novsWar, sender, args).execute();
                    return true;
                case CHAT:
                    new ChatCommand(novsWar, sender, args).execute();
                    return true;
                default:
                	ChatUtil.sendError((Player) sender, Messages.COMMAND_NONEXISTENT.toString());
                	break;
        		}
        	} else {
                ChatUtil.sendError((Player) sender, Messages.NO_PERMISSION.toString());
                return true;
        	}
        }
        return false;
    }
}
