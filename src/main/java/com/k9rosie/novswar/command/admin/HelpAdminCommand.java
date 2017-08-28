package com.k9rosie.novswar.command.admin;

import com.k9rosie.novswar.util.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.player.NovsPlayer;

import java.util.Map;

public class HelpAdminCommand implements NovsCommand {
    private String permissions;
    private String description;
    private int requiredNumofArgs;
    private boolean playerOnly;
    private AdminCommand commandHandler;

	public HelpAdminCommand(AdminCommand commandHandler) {
        permissions = "novswar.command.admin.help";
        description = "Display the admin help message";
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
