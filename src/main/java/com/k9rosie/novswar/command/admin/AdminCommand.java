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
            switch (getArgs()[1]) {
                case "setspawn":
                    new SetSpawnCommand(getNovsWar(), getSender(), getArgs()).execute();
                    return;
                case "setregion":
                    new SetRegionCommand(getNovsWar(), getSender(), getArgs()).execute();
                    return;
                case "setteam":
                    new SetTeamCommand(getNovsWar(), getSender(), getArgs()).execute();
                    return;
                case "kick":
                    new KickCommand(getNovsWar(), getSender(), getArgs()).execute();
                    return;
                case "nextgame":
                    new NextGameCommand(getNovsWar(), getSender(), getArgs()).execute();
                    return;
                case "restart":
                    new RestartCommand(getNovsWar(), getSender(), getArgs()).execute();
                    return;
            }
        }
    }
}
