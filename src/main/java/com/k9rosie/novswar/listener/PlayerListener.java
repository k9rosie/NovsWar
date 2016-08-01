package com.k9rosie.novswar.listener;


import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.NovsWarPlugin;
import com.k9rosie.novswar.event.NovsWarPlayerAssistEvent;
import com.k9rosie.novswar.event.NovsWarPlayerKillEvent;
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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
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
        game.quitGame();
        System.out.println("Player count: " + novswar.getPlayerManager().getPlayers().values().size());
    }

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
            //Check for non-violence conditions
            NovsPlayer victim = playerManager.getPlayers().get(victimBukkitPlayer);
            NovsPlayer attacker = playerManager.getPlayers().get(attackerBukkitPlayer);

            playerKill(event, attacker, victim, arrowDeath);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDamage(EntityDamageEvent event) {
        System.out.println("EntityDamageEvent: " + event.getFinalDamage() + " / " + event.getCause());
    	Game game = novswar.getGameHandler().getGame();
    	//Prevent damage outside of DURING_GAME
    	if(game.getGameState().equals(GameState.DURING_GAME)) {
    		event.setCancelled(true);
            return;
    	}
    	
        if (event.getEntity() instanceof Player) {
            Player bukkitPlayer = (Player) event.getEntity();
            NovsPlayer player = playerManager.getPlayers().get(bukkitPlayer);

            if (!player.getTeam().canBeDamaged()) {
                event.setCancelled(true);
                return;
            }

            if (bukkitPlayer.getKiller() != null) {
                if (bukkitPlayer.getKiller() instanceof Player) {
                    if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                        return;
                    } else {
                        NovsPlayer killer = playerManager.getPlayers().get(bukkitPlayer.getKiller());
                        NovsPlayer victim = playerManager.getPlayers().get(bukkitPlayer);
                        playerKill(event, killer, victim, false);
                    }
                }
            }

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

                player.getStats().incrementSuicides();

                game.scheduleDeath(player, game.getGamemode().getDeathTime());
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

                bukkitPlayer.sendMessage("Region set");
                player.setCornerOneBuffer(null);
                player.setRegionTypeBuffer(null);
                player.setRegionNameBuffer(null);
                player.setSettingRegion(false);
            }

            event.setCancelled(true);
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

        if (player.isDead() && (bukkitPlayer.getSpectatorTarget() == null)) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        NovsPlayer player = playerManager.getPlayers().get(event.getPlayer());

        if (player.isDead()) {
            event.setCancelled(true);
        }
    }

    public void playerKill(EntityDamageEvent event, NovsPlayer attacker, NovsPlayer victim, boolean arrowDeath) {
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
            //Evaluate assists
            NovsPlayer assistAttacker = victim.getAssistAttacker(attacker);
            victim.clearAttackers();
            game.scheduleDeath(victim, game.getGamemode().getDeathTime());
            //Event calls
            NovsWarPlayerKillEvent invokeEvent = new NovsWarPlayerKillEvent(attacker, victim, attackerTeam, victimTeam, game);
            Bukkit.getPluginManager().callEvent(invokeEvent);
            if(assistAttacker != null) {
                NovsWarPlayerAssistEvent invokeEvent_1 = new NovsWarPlayerAssistEvent(assistAttacker, victim, assistAttacker.getTeam(), victimTeam, game);
                Bukkit.getPluginManager().callEvent(invokeEvent_1);
            }
        }
    }
}
