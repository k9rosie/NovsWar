package com.k9rosie.novswar.command.admin;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.model.NovsWorld;
import com.k9rosie.novswar.util.Messages;

public class SaveRegionsCommand extends NovsCommand {
	
	public SaveRegionsCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
    }

    public void execute() {
        if (getArgs().length != 2) {
            getSender().sendMessage(Messages.INVALID_PARAMETERS.toString());
            return;
        } else {
            Player bukkitPlayer = (Player) getSender();
            World bukkitWorld = bukkitPlayer.getWorld();
            NovsWorld world = getNovsWar().getNovsWorldCache().getWorlds().get(bukkitWorld);

            if (world == null) {
                bukkitPlayer.sendMessage("The world you're in isn't enabled in NovsWar.");
                return;
            }

            getNovsWar().getNovsWorldCache().saveRegions();

            bukkitPlayer.sendMessage("Saved regions in this world.");
        }
    }
}
