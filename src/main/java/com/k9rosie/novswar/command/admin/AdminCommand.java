package com.k9rosie.novswar.command.admin;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import org.bukkit.command.CommandSender;

public class AdminCommand extends NovsCommand {

    public AdminCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
    }

    public void execute() {
        if (getArgs().length == 1) {
            getSender().sendMessage("reserved for display stats");
        } else if (getArgs().length >= 2) {
        	AdminCommandType commandArg = AdminCommandType.getCommand(getArgs()[1]);
            switch (commandArg) {
                case SETSPAWN:
                    new SetSpawnCommand(getNovsWar(), getSender(), getArgs()).execute();
                    return;
                case SETREGION:
                    new SetRegionCommand(getNovsWar(), getSender(), getArgs()).execute();
                    return;
                case SETTEAM:
                    new SetTeamCommand(getNovsWar(), getSender(), getArgs()).execute();
                    return;
                case KICK:
                    new KickCommand(getNovsWar(), getSender(), getArgs()).execute();
                    return;
                case NEXTGAME:
                    new NextGameCommand(getNovsWar(), getSender(), getArgs()).execute();
                    return;
                case RESTART:
                    new RestartCommand(getNovsWar(), getSender(), getArgs()).execute();
                    return;
                case HELP:
                    new HelpAdminCommand(getNovsWar(), getSender(), getArgs()).execute();
                    return;
                case LISTREGIONS:
                    new ListRegionsCommand(getNovsWar(), getSender(), getArgs()).execute();
                    return;
                case DELREGION:
                    new DelRegionCommand(getNovsWar(), getSender(), getArgs()).execute();
                    return;
                case SAVEREGIONS:
                    new SaveRegionsCommand(getNovsWar(), getSender(), getArgs()).execute();
                    return;
            }
        }
    }
}
