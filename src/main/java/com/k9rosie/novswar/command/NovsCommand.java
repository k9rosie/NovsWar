package com.k9rosie.novswar.command;

import org.bukkit.command.CommandSender;

public interface NovsCommand {
    String getPermissions();
    String getDescription();
    int getRequiredNumofArgs();
    boolean isPlayerOnly();
    void execute(CommandSender sender, String[] args);
}
