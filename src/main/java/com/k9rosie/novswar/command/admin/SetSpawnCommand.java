package com.k9rosie.novswar.command.admin;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.util.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class SetSpawnCommand extends NovsCommand {
    private FileConfiguration regions;

    public SetSpawnCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
        regions = novsWar.getConfigurationCache().getConfig("regions");
    }

    public void execute() {
        if (getArgs().length != 3) {
            getSender().sendMessage(Messages.INVALID_PARAMETERS.toString());
            return;
        } else {

        }
    }
}
