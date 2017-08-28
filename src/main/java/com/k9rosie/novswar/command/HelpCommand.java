package com.k9rosie.novswar.command;

import com.k9rosie.novswar.util.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.player.NovsPlayer;

import java.util.Map;

public class HelpCommand implements NovsCommand {
    private String permissions;
    private String description;
    private int requiredNumofArgs;
    private boolean playerOnly;
    private CommandHandler commandHandler;

    public HelpCommand(CommandHandler commandHandler) {
        permissions = "novswar.command.help";
        description = "Displays the help message";
        requiredNumofArgs = 0;
        playerOnly = false;
        this.commandHandler = commandHandler;
    }

    public void execute(CommandSender sender, String[] args) {
        // TODO: write proper help command

        for (Map.Entry<String, NovsCommand> entry : commandHandler.getCommands().entrySet()) {
            String name = entry.getKey();
            NovsCommand command = entry.getValue();

            sender.sendMessage("/"+name+"\t"+command.getDescription());
        }
    }

    @Override
    public String getPermissions() {
        return permissions;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getRequiredNumofArgs() {
        return requiredNumofArgs;
    }

    @Override
    public boolean isPlayerOnly() {
        return playerOnly;
    }
}
