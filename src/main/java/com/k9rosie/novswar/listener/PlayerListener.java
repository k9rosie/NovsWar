package com.k9rosie.novswar.listener;


import java.util.HashSet;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.NovsWarPlugin;
import com.k9rosie.novswar.command.CommandType;
import com.k9rosie.novswar.event.NovsWarLeaveTeamEvent;
import com.k9rosie.novswar.event.NovsWarPlayerAssistEvent;
import com.k9rosie.novswar.event.NovsWarPlayerKillEvent;
import com.k9rosie.novswar.event.NovsWarTeamVictoryEvent;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.game.GameState;
import com.k9rosie.novswar.manager.PlayerManager;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsRegion;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.model.NovsWorld;
import com.k9rosie.novswar.util.Messages;
import com.k9rosie.novswar.util.RegionType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class PlayerListener implements Listener {

    private NovsWarPlugin plugin;
    private NovsWar novswar;
    private PlayerManager playerManager;

    public PlayerListener(NovsWarPlugin plugin) {
        this.plugin = plugin;
        novswar = plugin.getNovswarInstance();
        playerManager = novswar.getPlayerManager();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
    	Game game = novswar.getGameHandler().getGame();
        Player bukkitPlayer = event.getPlayer();
        NovsPlayer player = playerManager.createNovsPlayer(bukkitPlayer); //handles assignment to default team
        NovsTeam defaultTeam = novswar.getTeamManager().getDefaultTeam();

        novswar.getDatabase().fetchPlayerData(player);
        novswar.getTeamManager().getDefaultTeam().getScoreboardTeam().addEntry(player.getBukkitPlayer().getDisplayName());
        bukkitPlayer.setScoreboard(game.getScoreboard().getBukkitScoreboard());
        bukkitPlayer.teleport(novswar.getWorldManager().getLobbyWorld().getTeamSpawns().get(defaultTeam));

        player.getStats().incrementConnects();
        System.out.println("Player count: " + novswar.getPlayerManager().getPlayers().values().size());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player bukkitPlayer = event.getPlayer();
        NovsPlayer player = playerManager.getPlayers().get(bukkitPlayer);
        NovsTeam team = player.getTeam();

        event.setFormat(team.getColor() + bukkitPlayer.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
    	Game game = novswar.getGameHandler().getGame();
        Player bukkitPlayer = event.getPlayer();
        NovsPlayer player = playerManager.getPlayers().get(bukkitPlayer);

        novswar.getDatabase().flushPlayerData(player);
        playerManager.getPlayers().remove(bukkitPlayer);
        if(player.getTeam().equals(novswar.getTeamManager().getDefaultTeam())==false) {
        	//If player is on a team, invoke event
        	NovsWarLeaveTeamEvent invokeEvent = new NovsWarLeaveTeamEvent(player, game);
            Bukkit.getPluginManager().callEvent(invokeEvent);
        }
        //System.out.println("Player count: " + novswar.getPlayerManager().getPlayers().values().size());
    }

    /**
     * This event fires only when a player takes damage from another entity (player or arrow)
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
        System.out.println("EntityDamageByEntityEvent: " + event.getFinalDamage() + " / " + event.getCause());
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
            
            //Check for player damage DURING_GAME
            if(game.getGameState().equals(GameState.DURING_GAME)==false) {
            	attackerBukkitPlayer.sendMessage("You can only attack players during the game.");
            	event.setCancelled(true);
                return;
            }
            NovsPlayer victim = playerManager.getPlayers().get(victimBukkitPlayer);
            NovsPlayer attacker = playerManager.getPlayers().get(attackerBukkitPlayer);
            onPlayerAttacked(event, attacker, victim, arrowDeath);
        }
    }

    /**
     * This event fires any time a player takes damage (even from environment)
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDamage(EntityDamageEvent event) {
        System.out.println("EntityDamageEvent: " + event.getFinalDamage() + " / " + event.getCause());
    	Game game = novswar.getGameHandler().getGame();
    	//Prevent damage outside of DURING_GAME
    	if(game.getGameState().equals(GameState.DURING_GAME) == false) {
    		event.setCancelled(true);
            return;
    	}
    	//If the entity being damaged is a player
        if (event.getEntity() instanceof Player) {
            Player bukkitPlayer = (Player) event.getEntity();
            NovsPlayer player = playerManager.getPlayers().get(bukkitPlayer);
            //Cancel the event if the player can't be damaged
            if (!player.getTeam().canBeDamaged()) {
                event.setCancelled(true);
                return;
            }
            //If there is a killer player
            if (bukkitPlayer.getKiller() != null && bukkitPlayer.getKiller() instanceof Player) {
                if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                	//The player was killed by another entity. onPlayerDamageByPlayer handles the rest.
                    return;
                } else {
                	//The player was killed by the environment, but still has a valid killer
                    NovsPlayer killer = playerManager.getPlayers().get(bukkitPlayer.getKiller());
                    NovsPlayer victim = playerManager.getPlayers().get(bukkitPlayer);
                    onPlayerAttacked(event, killer, victim, false);
                }
            } else { //The player got hurt purely by environment
            	double damage = event.getFinalDamage();
                player.getStats().incrementDamageTaken(damage);

                // if damage is fatal
                if (bukkitPlayer.getHealth() - damage <= 0) {
                    event.setCancelled(true);

                    String deathMessage = Messages.DEATH_MESSAGE.toString();
                    deathMessage = deathMessage.replace("%player_tcolor%", player.getTeam().getColor().toString())
                    .replace("%player%", bukkitPlayer.getDisplayName());

                    for (NovsPlayer p : playerManager.getPlayers().values()) {
                        if (p.canSeeDeathMessages()) {
                            p.getBukkitPlayer().sendMessage(deathMessage);
                        }
                    }
                    //player.getStats().incrementSuicides();
                    game.killPlayer(player, null);
                    //game.scheduleDeath(player, game.getGamemode().getDeathTime());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player bukkitPlayer = event.getPlayer();
        NovsPlayer player = playerManager.getPlayers().get(bukkitPlayer);

        if (player.isSettingRegion()) {
            if (event.getClickedBlock() == null) {
                bukkitPlayer.sendMessage("You need to click a block");
                event.setCancelled(true);
                return;
            }
            Location location = event.getClickedBlock().getLocation();

            if (novswar.getWorldManager().getWorlds().get(bukkitPlayer.getWorld()) == null) {
                bukkitPlayer.sendMessage("The world you're in isn't enabled in NovsWar.");
                event.setCancelled(true);
                return;
            }

            if (player.getCornerOneBuffer() == null) {
                player.setCornerOneBuffer(location);
                bukkitPlayer.sendMessage("Setting corner two...");
            } else if (player.getCornerOneBuffer() != null) {
                NovsWorld world = novswar.getWorldManager().getWorlds().get(bukkitPlayer.getWorld());
                NovsRegion region = new NovsRegion(world,
                        player.getCornerOneBuffer(), location, player.getRegionTypeBuffer());

                world.getRegions().put(player.getRegionNameBuffer(), region);

                bukkitPlayer.sendMessage("Region set: "+player.getRegionTypeBuffer().toString());
                player.setCornerOneBuffer(null);
                player.setRegionTypeBuffer(null);
                player.setRegionNameBuffer(null);
                player.setSettingRegion(false);
            }
            event.setCancelled(true);
        } else {
        	//Check for sign click
        	if(event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.WALL_SIGN)) {
        		bukkitPlayer.sendMessage("Ouch");
        		Sign clicked = (Sign) event.getClickedBlock().getState();
        		System.out.println("Sign content is...");
            	String[] lines = clicked.getLines();
            	for(int i = 0; i < lines.length; i++) {
            		System.out.println(lines[i]);
            	}
            	if(clicked.getLine(0).toLowerCase().contains("novswar")) {
            		System.out.println("Sign is a NovsWar sign!");
            		//Check for valid command
            		String command = "nw "+clicked.getLine(1);
            		if(CommandType.contains(command)) {
            			bukkitPlayer.performCommand(command);
            		} else {
            			bukkitPlayer.sendMessage("This novswar sign is invalid!");
            		}
            		event.setCancelled(true);
            	}
        	}
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryClick(InventoryClickEvent event) {
    	Inventory inventory = event.getInventory();
    	Game game = novswar.getGameHandler().getGame();
		Inventory ballotBox = game.getBallotBox().getBallots();
		//check to make sure click occurs inside voting Inventory screen
		if(inventory != null && inventory.getName().equals(ballotBox.getName())) {
			System.out.println("InventoryClickEvent! "+event.toString());
			Player player = (Player) event.getWhoClicked();
			int slot = event.getSlot();
			ItemStack clicked = event.getCurrentItem();
			//check that the click was on a BEDROCK voting item
			if(clicked != null && clicked.getType().equals(game.getBallotBox().getVoteItem()) && player != null){
				game.getBallotBox().recordResult(slot);
				player.closeInventory();
				System.out.println(player.getName()+" voted for slot "+slot+", map "+clicked.getItemMeta().getDisplayName());
				player.sendMessage("You voted for "+clicked.getItemMeta().getDisplayName());
				NovsPlayer nplayer = novswar.getPlayerManager().getPlayers().get(player);
				nplayer.setVoted(true);
			}
			event.setCancelled(true);
		}
	}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player bukkitPlayer = event.getPlayer();
        NovsPlayer player = playerManager.getPlayers().get(bukkitPlayer);
        NovsWorld currentGameWorld = novswar.getGameHandler().getGame().getWorld();
        Game game = novswar.getGameHandler().getGame();

        if (player.isDead() && (bukkitPlayer.getSpectatorTarget() == null)) {
            event.setCancelled(true);
            return;
        }

        for (NovsRegion region : currentGameWorld.getEnterableRegions()) {
            if (region.inRegion(event.getTo())) {
                switch(region.getRegionType()) {
                case TEAM_SPAWN :
                	//Assume that players teleporting into the teamspawn do not trigger this code...
                	if(region.inRegion(event.getFrom())==false) { //if the player is moving from outside the Team Spawn
                		bukkitPlayer.teleport(event.getFrom());
                    	bukkitPlayer.sendMessage("You cannot go there!");
                    	event.setCancelled(true);
                	}
                	break;
                case DEATH_REGION :
                	//Determine the player that has done the most damage
                	NovsPlayer attacker = player.getAssistAttacker(null);
                	//if attacker is null, there are no damagers
                	game.killPlayer(player, attacker);
                	break;
                default :
                	break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        NovsPlayer player = playerManager.getPlayers().get(event.getPlayer());

        if (player.isDead() || player.isSpectating()) {
            event.setCancelled(true);
        }
        if(player.isSpectating()) {
        	if(player.isShiftToggled()) {
        		//Switch spectator targets
        		player.setShiftToggled(false);
        		player.getSpectatorTarget().getSpectatorObservers().remove(player);
        		NovsPlayer newTarget = novswar.getPlayerManager().nextSpectatorTarget(player);
        		newTarget.getSpectatorObservers().add(player);
        	} else {
        		player.setShiftToggled(true);
        	}
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
    	NovsPlayer player = playerManager.getPlayers().get(event.getPlayer());

        if (player.isSpectating()) {
            System.out.println("PlayerGameModeChange! Player is spectating... cancelling");
            event.setCancelled(true);
        }
    }

    private void onPlayerAttacked(EntityDamageEvent event, NovsPlayer attacker, NovsPlayer victim, boolean arrowDeath) {
        Game game = novswar.getGameHandler().getGame();
        NovsTeam victimTeam = victim.getTeam();
        NovsTeam attackerTeam = attacker.getTeam();
        if (attackerTeam.equals(victimTeam)) {
            if (!attackerTeam.getFriendlyFire()) {
                event.setCancelled(true);
                return;
            }
        }
        if (!victim.getTeam().canBeDamaged()) {
            event.setCancelled(true);
            return;
        }

        double damage = event.getFinalDamage();
        victim.getStats().incrementDamageTaken(damage);
        attacker.getStats().incrementDamageGiven(damage);
        victim.addAttacker(attacker, damage);

        // if damage is fatal
        if (victim.getBukkitPlayer().getHealth() - damage <= 0) {
            event.setCancelled(true);
            //Send death message
            String deathMessage;
            if (arrowDeath) {
                deathMessage = Messages.SHOT_MESSAGE.toString();
            } else {
                deathMessage = Messages.KILL_MESSAGE.toString();
            }
            deathMessage = deathMessage.replace("%killed_tcolor%", victimTeam.getColor().toString())
                    .replace("%killed%", victim.getBukkitPlayer().getDisplayName())
                    .replace("%killer_tcolor%", attackerTeam.getColor().toString())
                    .replace("%killer%", attacker.getBukkitPlayer().getDisplayName());
            for (NovsPlayer p : playerManager.getPlayers().values()) {
                if (p.canSeeDeathMessages()) {
                    p.getBukkitPlayer().sendMessage(deathMessage);
                }
            }
            //Evaluate statistics
            if (arrowDeath) {
                attacker.getStats().incrementArrowKills();
                victim.getStats().incrementArrowDeaths();
            } else {
                attacker.getStats().incrementKills();
                victim.getStats().incrementDeaths();
            }
            game.killPlayer(victim, attacker);
        }
    }
}
