package com.k9rosie.novswar.listener;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.NovsWarPlugin;
import com.k9rosie.novswar.world.NovsWorld;
import com.k9rosie.novswar.util.ChatUtil;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

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
    	
    	if (event.getPlayer().hasPermission(novswar.getCommandHandler().getAdminCommand().getPermissions())) {
    		String firstLine = event.getLine(0);
    		//Creating a NovsWar command sign

    		if (firstLine.equalsIgnoreCase("novswar")) {
        		if (novswar.getCommandHandler().getCommands().containsKey(event.getLine(1))) {
        			ChatUtil.sendNotice(event.getPlayer(), "Successfully created NovsWar sign!");
            		event.setLine(0, "§2NovsWar");
            		event.getBlock().getState().update();
        		} else {
					ChatUtil.sendError(event.getPlayer(), "Invalid command for NovsWar sign");
        		}
        	//Creating a round Info Sign
        	} else if(firstLine.equalsIgnoreCase("infosign")) {
        		Block block = event.getBlock();
        		NovsWorld world;
        		if(novswar.getWorldManager().getLobbyWorld().getBukkitWorld().equals(block.getWorld())) {
        			world = novswar.getWorldManager().getLobbyWorld();
        		} else {
        			world = novswar.getWorldManager().getWorlds().get(block.getWorld());
        		}
        		//Verify that world is valid
        		if(world == null) {
					ChatUtil.sendError(event.getPlayer(), "Could not create Info Sign, invalid world");
        		} else {
        			world.getSigns().put(block.getLocation(), (Sign) block.getState());
					novswar.getWorldManager().updateSigns(novswar.getGameHandler().getGame());
        		}
        	}
    	}
    }
    
    /**
     * Monitors for breaking signs
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
    	Block broken = event.getBlock();
    	if(broken.getState() instanceof Sign) {
    		//Get the world where the break occurred
    		NovsWorld world;
    		if(novswar.getWorldManager().getLobbyWorld().getBukkitWorld().equals(broken.getWorld())) {
    			world = novswar.getWorldManager().getLobbyWorld();
    		} else {
    			world = novswar.getWorldManager().getWorlds().get(broken.getWorld());
    		}
    		//Verify that world is valid
    		if(world != null) {
    			if(world.getSigns().remove(broken.getLocation().toString()) != null) {
    				ChatUtil.sendNotice(event.getPlayer(), "Removed Info Sign in world "+broken.getWorld());
    			}
    		}
    	}
    }
}
