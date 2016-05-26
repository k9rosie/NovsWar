package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import org.bukkit.command.CommandSender;

public class BaseCommand implements ICommand {

    public NovsWar novsWar;
    public CommandSender sender;

    public BaseCommand(NovsWar novsWar, CommandSender sender) {
        this.novsWar = novsWar;
        this.sender = sender;
    }

    public void execute() {

    }
}
