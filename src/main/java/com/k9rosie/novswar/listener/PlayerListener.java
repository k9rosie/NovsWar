package com.k9rosie.novswar.listener;


import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.NovsWarPlugin;
import com.k9rosie.novswar.game.DeathTimer;
import com.k9rosie.novswar.manager.PlayerManager;
import com.k9rosie.novswar.command.CommandType;
import com.k9rosie.novswar.event.NovsWarJoinServerEvent;
import com.k9rosie.novswar.event.NovsWarLeaveTeamEvent;
import com.k9rosie.novswar.event.NovsWarRegionEnterEvent;
import com.k9rosie.novswar.event.NovsWarRegionExitEvent;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.game.GameState;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsRegion;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.model.NovsWorld;
import com.k9rosie.novswar.util.ChatUtil;
import com.k9rosie.novswar.util.RegionType;

import com.k9rosie.novswar.util.SendTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class PlayerListener implements Listener {

    private NovsWar novswar;
    private PlayerManager novsPlayerCache;

    public PlayerListener(NovsWarPlugin plugin) {
        novswar = plugin.getNovswarInstance();
        novsPlayerCache = novswar.getNovsPlayerCache();
    }

    /**
     * Fires when a player joins the server
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
    	Game game = novswar.getGameHandler().getGame();
        Player bukkitPlayer = event.getPlayer();
        NovsPlayer player = novsPlayerCache.createNovsPlayer(bukkitPlayer); //handles assignment to default team
        NovsTeam defaultTeam = novswar.getNovsTeamCache().getDefaultTeam();

        novswar.getDatabase().fetchPlayerData(player);
        novswar.getNovsTeamCache().getDefaultTeam().getScoreboardTeam().addEntry(player.getBukkitPlayer().getDisplayName());
        bukkitPlayer.setScoreboard(game.getScoreboard().getBukkitScoreboard());
        bukkitPlayer.teleport(novswar.getNovsWorldCache().getLobbyWorld().getTeamSpawnLoc(defaultTeam));
        bukkitPlayer.setGameMode(GameMode.SURVIVAL);
        bukkitPlayer.setHealth(19);
        bukkitPlayer.setHealth(bukkitPlayer.getMaxHealth());
        bukkitPlayer.setFoodLevel(20);
        SendTitle.sendTitle(bukkitPlayer, 0, 2000, 0, " ", ""); // clear any title messages they may have
        player.getStats().incrementConnects();
        ChatUtil.printDebug("Player count: " + novswar.getNovsPlayerCache().getPlayers().values().size());
        NovsWarJoinServerEvent invokeEvent = new NovsWarJoinServerEvent(player);
        Bukkit.getPluginManager().callEvent(invokeEvent);
    }

    /**
     * Fires on chat events
     * All players always see global chat messages and team chat messages
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        
        //Check if the event was caused by a player
        if(event.isAsynchronous()) {
        	Player bukkitPlayer = event.getPlayer();
            NovsPlayer player = novsPlayerCache.getPlayers().get(bukkitPlayer);
            NovsTeam team = player.getTeam();
            
            if(player.isGlobalChat()) {
                if (player.isDead()) {
                    event.setFormat("§7*DEAD* " + team.getColor() + bukkitPlayer.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
                } else if (player.isSpectating()) {
                    event.setFormat("§7*SPECTATING* " + team.getColor() + bukkitPlayer.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
                } else {
                    event.setFormat(team.getColor() + bukkitPlayer.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
                }
            } else {
            	//Team chat only
            	event.setCancelled(true);
            	for(NovsPlayer teamPlayer : team.getPlayers()) {
            		teamPlayer.getBukkitPlayer().sendMessage(ChatColor.GREEN + "[Team] "+bukkitPlayer.getDisplayName() +": "+ ChatColor.ITALIC + event.getMessage());
            	}
            }
        }
    }

    /**
     * Fires when a player quits the server
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
    	Game game = novswar.getGameHandler().getGame();
        Player bukkitPlayer = event.getPlayer();
        NovsPlayer player = novsPlayerCache.getPlayers().get(bukkitPlayer);

        player.getBukkitPlayer().setWalkSpeed(0.2f);
        player.getBukkitPlayer().setFlySpeed(0.2f);

        novswar.getDatabase().flushPlayerData(player);
        novsPlayerCache.getPlayers().remove(bukkitPlayer);
        if(player.getTeam().equals(novswar.getNovsTeamCache().getDefaultTeam())==false) {
        	//If player is on a team, invoke event
        	NovsWarLeaveTeamEvent invokeEvent = new NovsWarLeaveTeamEvent(player, game);
            Bukkit.getPluginManager().callEvent(invokeEvent);
        }

        if (game.getDeathTimers().containsKey(player)) { // if, for some reason, a death timer exists for a player when they exit the game
            DeathTimer deathTimer = game.getDeathTimers().get(player);
            deathTimer.stopTimer();
            game.getDeathTimers().remove(player);
        }
        //System.out.println("Player count: " + novswar.getNovsPlayerCache().getPlayers().values().size());
    }

    /**
     * This event fires only when a player takes damage from another entity (player or arrow)
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
    	ChatUtil.printDebug("EntityDamageByEntityEvent: " + event.getFinalDamage() + " / " + event.getCause());
        Player victimBukkitPlayer;
        Player attackerBukkitPlayer = null;
        boolean arrowDeath = false;
        Game game = novswar.getGameHandler().getGame();

        if (event.getEntity() instanceof Player) {
        	victimBukkitPlayer = (Player) event.getEntity();

            if (event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();

                if (arrow.getShooter() instanceof Player) {
                	attackerBukkitPlayer = (Player) arrow.getShooter();
                    arrowDeath = true;
                }
            } else if (event.getDamager() instanceof Player) {
            	attackerBukkitPlayer = (Player) event.getDamager();
            } else { // if neither player nor arrow
                return;
            }
            NovsPlayer victim = novsPlayerCache.getPlayers().get(victimBukkitPlayer);
            NovsPlayer attacker = novsPlayerCache.getPlayers().get(attackerBukkitPlayer);
            //Check for player damage DURING_GAME
            if(game.getGameState().equals(GameState.DURING_GAME)==false) {
            	ChatUtil.sendNotice(attacker, "You can only attack players during the game.");
            	event.setCancelled(true);
                return;
            }
            //Check if player is in a team spawn
            if(onPlayerAttackedInSpawn(victim)) {
            	ChatUtil.sendNotice(attacker, "You cannot attack players in spawn.");
            	event.setCancelled(true);
            	ChatUtil.printDebug("EntityDamageByEntity event cancelled due to being in spawn");
                return;
            }
            
            double damage = event.getFinalDamage();
            victim.getStats().incrementDamageTaken(damage);
            attacker.getStats().incrementDamageGiven(damage);
            victim.addAttacker(attacker, damage);

            // if damage is fatal
            if (victim.getBukkitPlayer().getHealth() - damage <= 0) {
                event.setCancelled(true);
                game.killPlayer(victim, attacker, arrowDeath);
            }
        }
    }

    /**
     * This event fires any time a player takes damage (even from environment)
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDamage(EntityDamageEvent event) {
    	ChatUtil.printDebug("EntityDamageEvent: " + event.getFinalDamage() + " / " + event.getCause());
    	Game game = novswar.getGameHandler().getGame();
    	//If the entity being damaged is a player
        if (event.getEntity() instanceof Player) {
            //Prevent damage outside of DURING_GAME
            if(game.getGameState().equals(GameState.DURING_GAME) == false) {
                event.setCancelled(true);
                return;
            }
            Player bukkitPlayer = (Player) event.getEntity();
            NovsPlayer victim = novsPlayerCache.getPlayers().get(bukkitPlayer);
            NovsPlayer killer = null;
            //Cancel the event if the player can't be damaged
            if (!victim.getTeam().canBeDamaged()) {
                event.setCancelled(true);
                return;
            }
            //Prevent damage to players in spawn
            if(onPlayerAttackedInSpawn(victim)) {
            	event.setCancelled(true);
            	ChatUtil.printDebug("EntityDamageEvent event cancelled due to being in spawn");
                return;
            }
            
            //Determine whether there is a killer
            if (bukkitPlayer.getKiller() != null && bukkitPlayer.getKiller() instanceof Player) {
            	//The victim has a valid killer
                if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                	//The player was killed by another entity. onPlayerDamageByPlayer handles the rest.
                    return;
                } else {
                	//The player was killed by the environment, but still has a valid killer
                    killer = novsPlayerCache.getPlayers().get(bukkitPlayer.getKiller());
                }
            }
            
            //At this point, if killer is null, the victim has been hurt purely by environment.
            //Otherwise, the victim has died from environmental damage but was attacked beforehand.
            
        	double damage = event.getFinalDamage();
        	victim.getStats().incrementDamageTaken(damage);

            // if damage is fatal or fell into void
            if ((bukkitPlayer.getHealth() - damage <= 0) || event.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
                event.setCancelled(true);
                game.killPlayer(victim, killer, false);
            }
        }
    }

    /**
     * Handles when players are setting regions and clicking signs
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player bukkitPlayer = event.getPlayer();
        NovsPlayer player = novsPlayerCache.getPlayers().get(bukkitPlayer);

        if (player.isSettingRegion()) {
            if (event.getClickedBlock() == null) {
            	ChatUtil.sendNotice(player, "You need to click a block.");
                event.setCancelled(true);
                return;
            }
            Location location = event.getClickedBlock().getLocation();

            if (novswar.getNovsWorldCache().getWorlds().get(bukkitPlayer.getWorld()) == null) {
            	ChatUtil.sendNotice(player, "The world you're in isn't enabled in NovsWar.");
                event.setCancelled(true);
                return;
            }

            if (player.getCornerOneBuffer() == null) {
                player.setCornerOneBuffer(location);
                ChatUtil.sendNotice(player, "Setting corner two...");
            } else if (player.getCornerOneBuffer() != null) {
                NovsWorld world = novswar.getNovsWorldCache().getWorlds().get(bukkitPlayer.getWorld());
                NovsRegion region = new NovsRegion(world,
                        player.getCornerOneBuffer(), location, player.getRegionTypeBuffer());

                world.getRegions().put(player.getRegionNameBuffer(), region);

                ChatUtil.sendNotice(player, "Region set: "+player.getRegionTypeBuffer().toString());
                player.setCornerOneBuffer(null);
                player.setRegionTypeBuffer(null);
                player.setRegionNameBuffer(null);
                player.setSettingRegion(false);
            }
            event.setCancelled(true);
        } else {
        	//Check for sign click
        	if(event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.WALL_SIGN)) {
        		Sign clicked = (Sign) event.getClickedBlock().getState();
        		ChatUtil.printDebug("Sign content is...");
            	String[] lines = clicked.getLines();
            	for(int i = 0; i < lines.length; i++) {
            		ChatUtil.printDebug(lines[i]);
            	}
            	if(clicked.getLine(0).toLowerCase().contains("novswar")) {
            		ChatUtil.printDebug("Sign is a NovsWar sign!");
            		//Check for valid command
            		String command = clicked.getLine(1);
            		if(CommandType.contains(command)) {
            			bukkitPlayer.performCommand("nw "+command);
            		} else {
            			ChatUtil.sendNotice(player, "This novswar sign is invalid!");
            		}
            		event.setCancelled(true);
            	}
        	}
        }
    }
    
    /** 
     * onInventoryClick
     * Handles voting when the ballot box inventory is clicked
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryClick(InventoryClickEvent event) {
    	Inventory inventory = event.getInventory();
    	Game game = novswar.getGameHandler().getGame();
		Inventory ballotBox = game.getBallotBox().getBallots();
		//check to make sure click occurs inside voting Inventory screen
		if(inventory != null && inventory.getName().equals(ballotBox.getName())) {
			ChatUtil.printDebug("InventoryClickEvent! "+event.toString());
			Player bukkitPlayer = (Player) event.getWhoClicked();
			NovsPlayer player = novswar.getNovsPlayerCache().getPlayers().get(bukkitPlayer);
			int slot = event.getSlot();
			ItemStack clicked = event.getCurrentItem();
			//check that the click was on a BEDROCK voting item
			if(clicked != null && clicked.getType().equals(game.getBallotBox().getVoteItem()) && bukkitPlayer != null){
				game.getBallotBox().recordResult(slot);
				bukkitPlayer.closeInventory();
				ChatUtil.printDebug(bukkitPlayer.getName()+" voted for slot "+slot+", map "+clicked.getItemMeta().getDisplayName());
				ChatUtil.sendNotice(player, "You voted for "+clicked.getItemMeta().getDisplayName());
				player.setVoted(true);
			}
			event.setCancelled(true);
		}
	}

    /**
     * onPlayerMove
     * Disables spectators from moving their camera.
     * Detects when a player enters a region and acts appropriately.
     * Supports adding/removing players from multiple regions on a single move event (for overlapping regions)
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player bukkitPlayer = event.getPlayer();
        NovsPlayer player = novsPlayerCache.getPlayers().get(bukkitPlayer);
        NovsWorld currentGameWorld = novswar.getGameHandler().getGame().getWorld();
        Game game = novswar.getGameHandler().getGame();
        /*
        if (player.isDead() && (bukkitPlayer.getSpectatorTarget() == null)) {
            event.getTo().setDirection(event.getFrom().getDirection());
            return;
        }*/
        
        for (NovsRegion region : currentGameWorld.getRegions().values()) {
        	
        	//If a player moved into this region
            if (region.inRegion(event.getTo())) {

            	//Perform specific actions based on region type
                switch(region.getRegionType()) {
                case TEAM_SPAWN :
                	//Assume that players teleporting into the teamspawn do not trigger this code...
                	if(region.inRegion(event.getFrom())==false) { //if the player is moving from outside the Team Spawn
                		bukkitPlayer.teleport(event.getFrom());
                    	ChatUtil.sendNotice(player, "You cannot go there!");
                    	event.setCancelled(true);
                	}
                	break;
                case DEATH_REGION :
                	//Determine the player that has done the most damage
                	NovsPlayer attacker = player.getAssistAttacker(null);
                	//if attacker is null, there are no damagers
                	game.killPlayer(player, attacker, false);
                	event.setCancelled(true);
                	break;
                default :
                	break;
                } 
            } 
        }
        onPlayerEnterLeaveRegion(event);
    }
    
    /**
     * Checks for players teleporting into regions
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
    	
    	onPlayerEnterLeaveRegion(event);
    }

    /**
     * Disables sneaking for dead players & repurposes the LSHIFT key to switch spectator targets
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
    	Game game = novswar.getGameHandler().getGame();
        NovsPlayer player = novsPlayerCache.getPlayers().get(event.getPlayer());

        if (player.isDead() || player.isSpectating()) {
            event.setCancelled(true);
        }
        if(player.isSpectating()) {
        	if(player.isShiftToggled()) {
        		//Switch spectator targets
        		player.setShiftToggled(false);
        		//Remove this player from their current target's observer list
        		novswar.getNovsPlayerCache().getPlayers().get(player.getBukkitPlayer().getSpectatorTarget()).getSpectatorObservers().remove(player);
        		//Set this player's next spectator target, if available
        		game.nextSpectatorTarget(player);
        	} else {
        		player.setShiftToggled(true);
        	}
        }
    }
    
    /**
     * Prevents other plugins from forcing a player's gamemode if they are spectating
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
    	NovsPlayer player = novsPlayerCache.getPlayers().get(event.getPlayer());

        if (player.isSpectating()) {
        	ChatUtil.printDebug("PlayerGameModeChange! Player is spectating... cancelling");
            event.setCancelled(true);
        }
    }
    
    /**
     * Cancels hunger if enable_hunger is false in core
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent event) {
    	if(novswar.getNovsConfigCache().getConfig("core").getBoolean("core.game.enable_hunger") == false) {
    		event.setCancelled(true);
    		//Keep saturation full if enabled
    		if(event.getEntity() instanceof Player) {
    			Player player = (Player)event.getEntity();
    			if(novswar.getNovsConfigCache().getConfig("core").getBoolean("core.game.fast_health_regen") == true) {
    				player.setSaturation(20);
    			} else {
    				//keep saturation zero
    				player.setSaturation(0);
    			}
    		}
    	}
    }

    private void onPlayerEnterLeaveRegion(PlayerMoveEvent event) {
    	NovsPlayer player = novsPlayerCache.getPlayers().get(event.getPlayer());
    	NovsWorld currentGameWorld = novswar.getGameHandler().getGame().getWorld();
    	Game game = novswar.getGameHandler().getGame();
    	
    	if(event.isCancelled()==false) {
    		for (NovsRegion region : currentGameWorld.getRegions().values()) {
    			//If a player moved into this region
                if (region.inRegion(event.getTo())) {
                	
                	//If the player moved from outside the region
                	if(region.inRegion(event.getFrom())==false) {
                		NovsWarRegionEnterEvent invokeEvent = new NovsWarRegionEnterEvent(game, player, region);
                        Bukkit.getServer().getPluginManager().callEvent(invokeEvent);
                        if(invokeEvent.isCancelled()==false) {
                        	region.getPlayersInRegion().add(player);
                        	ChatUtil.printDebug("Added "+player.getBukkitPlayer().getName()+" to region "+region.getRegionType());
                        }
                	}
                	
                } else {
                	//The player moved out of this region
                	if(region.inRegion(event.getFrom())) {
                		NovsWarRegionExitEvent invokeEvent = new NovsWarRegionExitEvent(game, player, region);
                        Bukkit.getServer().getPluginManager().callEvent(invokeEvent);
                        if(invokeEvent.isCancelled()==false) {
                        	region.getPlayersInRegion().remove(player);
                        	ChatUtil.printDebug("Removed "+player.getBukkitPlayer().getName()+" from region "+region.getRegionType());
                        }
                	}
                }
    		}
    	}
    }
    
    /**
     * Helper method to check if a victim player is in a TEAM_SPAWN region
     * @param victim
     * @return
     */
    private boolean onPlayerAttackedInSpawn(NovsPlayer victim) {
    	Game game = novswar.getGameHandler().getGame();
    	
    	boolean isPlayerInSpawn = false;
        for(NovsRegion region : game.getWorld().getRegions().values()) {
        	if(region.getRegionType().equals(RegionType.TEAM_SPAWN)) {
        		if(region.getPlayersInRegion().contains(victim)) {
        			isPlayerInSpawn = true;
            		//System.out.println(victim.getBukkitPlayer().getName()+" was attacked in spawn!");
        		}
        	}
        }
        return isPlayerInSpawn;
    }
}
