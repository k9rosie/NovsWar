package com.k9rosie.novswar.command.admin;

import com.k9rosie.novswar.config.MessagesConfig;
import com.k9rosie.novswar.util.ChatUtil;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.world.NovsWorld;

public class SaveRegionsCommand extends NovsCommand {
	
	public SaveRegionsCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
    }

    public void execute() {
        if (getArgs().length != 2) {
            ChatUtil.sendError((Player) getSender(), MessagesConfig.getInvalidParameters());
            return;
        } else {
            Player bukkitPlayer = (Player) getSender();
            World bukkitWorld = bukkitPlayer.getWorld();
            NovsWorld world = getNovsWar().getWorldManager().getWorlds().get(bukkitWorld);

            if (world == null) {
                ChatUtil.sendError(bukkitPlayer, "The world you're in isn't enabled in NovsWar.");
                return;
            }

            getNovsWar().getWorldManager().updateRegions();
            getNovsWar().getConfigManager().saveConfigs();
            world.saveRegionBlocks();

            ChatUtil.sendNotice(bukkitPlayer, "Saved regions in this world.");
        }
    }
}
