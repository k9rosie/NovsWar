package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import org.bukkit.command.CommandSender;

public abstract class NovsCommand {

    private NovsWar novsWar;
    private CommandSender sender;
    private String[] args;

    public NovsCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        this.novsWar = novsWar;
        this.sender = sender;
        this.args = args;
    }

    public NovsCommand(NovsWar novsWar, CommandSender sender) {
        this.novsWar = novsWar;
        this.sender = sender;
    }

    public NovsWar getNovsWar() {
        return novsWar;
    }

    public CommandSender getSender() {
        return sender;
    }

    public String[] getArgs() {
        return args;
    }

    public abstract void execute();
}
