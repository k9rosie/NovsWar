package com.k9rosie.novswar.listener;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.NovsWarPlugin;

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
    	System.out.println("SignChangeEvent! Sign content is...");
    	String[] lines = event.getLines();
    	for(int i = 0; i < lines.length; i++) {
    		System.out.println(lines[i]);
    	}
    }

}
