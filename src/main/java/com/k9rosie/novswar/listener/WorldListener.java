package com.k9rosie.novswar.listener;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.NovsWarPlugin;
import com.k9rosie.novswar.command.CommandType;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class WorldListener implements Listener {

    private NovsWarPlugin plugin;
    private NovsWar novswar;

    public WorldListener(NovsWarPlugin plugin) {
        this.plugin = plugin;
        novswar = plugin.getNovswarInstance();
    }
    
    /**
     * Monitors for sign changes
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChangeEvent(SignChangeEvent event) {
    	if(event.getLine(0).toLowerCase().equals("novswar")) {
    		if(CommandType.contains(event.getLine(1))) {
    			event.getPlayer().sendMessage("Successfully created NovsWar sign!");
        		event.setLine(0, "&2NovsWar");
    		} else {
    			event.getPlayer().sendMessage("Invalid command for NovsWar sign");
    		}
    	}
    }
}
