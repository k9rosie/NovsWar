package com.k9rosie.novswar.listener;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.NovsWarPlugin;
import com.k9rosie.novswar.command.CommandType;
import com.k9rosie.novswar.model.NovsInfoSign;
import com.k9rosie.novswar.model.NovsWorld;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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
    	
    	if(event.getPlayer().hasPermission(CommandType.ADMIN.permission())) {
    		String firstLine = event.getLine(0);
    		//Creating a NovsWar command sign
    		if(firstLine.equalsIgnoreCase("novswar")) {
        		if(CommandType.contains(event.getLine(1))) {
        			event.getPlayer().sendMessage("Successfully created NovsWar sign!");
            		event.setLine(0, "§2NovsWar");
        		} else {
        			event.getPlayer().sendMessage("Invalid command for NovsWar sign");
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
        			event.getPlayer().sendMessage("Could not create Info Sign, invalid world");
        		} else {
        			world.getInfoSigns().put(block.getLocation().toString(), new NovsInfoSign(block));
        			event.getPlayer().sendMessage("Created Info Sign in world "+block.getWorld());
        			System.out.println("Created NovsInfoSign with key "+block.getLocation().toString());
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
    			System.out.println("A sign was broken. Here is the NovsInfoSign map for world "+broken.getWorld());
    			System.out.println(world.getInfoSigns().toString());
    			if(world.getInfoSigns().remove(broken.getLocation().toString()) != null) {
    				event.getPlayer().sendMessage("Removed Info Sign in world "+broken.getWorld());
    				System.out.println("Found and removed NovsInfoSign with key "+broken.getLocation().toString());
    			} else {
    				System.out.println("Could not find NovsInfoSign with key "+broken.getLocation().toString());
    			}
    		}
    	}
    }
}
