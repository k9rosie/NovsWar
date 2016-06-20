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
            if (getArgs()[1].equalsIgnoreCase("setspawn")) {
                new SetSpawnCommand(getNovsWar(), getSender(), getArgs()).execute();
                return;
            }

            if (getArgs()[1].equalsIgnoreCase("setregion")) {
                new SetRegionCommand(getNovsWar(), getSender(), getArgs()).execute();
                return;
            }

            if (getArgs()[1].equalsIgnoreCase("setteam")) {
                new SetTeamCommand(getNovsWar(), getSender(), getArgs()).execute();
                return;
            }
        }
    }
}
