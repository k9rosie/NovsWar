package com.k9rosie.novswar.listener;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.NovsWarPlugin;
import com.k9rosie.novswar.event.NovsWarPlayerKillEvent;
import com.k9rosie.novswar.game.Game;
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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.HashSet;

public class PlayerListener implements Listener {

    private NovsWarPlugin plugin;
    private NovsWar novswar;
    private Game game;
    private PlayerManager playerManager;

    public PlayerListener(NovsWarPlugin plugin) {
        this.plugin = plugin;
        novswar = plugin.getNovswarInstance();
        game = novswar.getGameHandler().getGame();
        playerManager = novswar.getPlayerManager();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player bukkitPlayer = event.getPlayer();
        NovsPlayer player = playerManager.createNovsPlayer(bukkitPlayer);
        NovsTeam defaultTeam = novswar.getTeamManager().getDefaultTeam();

        novswar.getDatabase().fetchPlayerData(player);
        game.getNeutralTeamData().getPlayers().add(player);
        game.getNeutralTeamData().getScoreboardTeam().addEntry(player.getBukkitPlayer().getDisplayName());
        bukkitPlayer.setScoreboard(game.getScoreboard().getBukkitScoreboard());
        bukkitPlayer.teleport(novswar.getWorldManager().getLobbyWorld().getTeamSpawns().get(defaultTeam));

        player.getStats().incrementConnects();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player bukkitPlayer = event.getPlayer();
        NovsPlayer player = playerManager.getNovsPlayer(bukkitPlayer);
        NovsTeam team = game.getPlayerTeam(player);

        event.setFormat(team.getColor() + bukkitPlayer.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player bukkitPlayer = event.getPlayer();
        NovsPlayer player = playerManager.getNovsPlayer(bukkitPlayer);
        NovsTeam team = game.getPlayerTeam(player);

        novswar.getDatabase().flushPlayerData(player);
        if (game.getPlayerTeam(player).equals(novswar.getTeamManager().getDefaultTeam())) {
            game.getNeutralTeamData().getPlayers().remove(player);
        } else {
            game.getTeamData().get(team).getPlayers().remove(player);
        }
        playerManager.getPlayers().remove(player);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
        Player damagedBukkitPlayer;
        Player damagerBukkitPlayer = null;
        boolean arrowDeath = false;

        if (event.getEntity() instanceof Player) {
            damagedBukkitPlayer = (Player) event.getEntity();

            if (event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();

                if (arrow.getShooter() instanceof Player) {
                    damagerBukkitPlayer = (Player) arrow.getShooter();
                    arrowDeath = true;
                }
            } else if (event.getDamager() instanceof Player) {
                damagerBukkitPlayer = (Player) event.getDamager();
            } else { // if neither player nor arrow
                return;
            }

            NovsPlayer damaged = playerManager.getNovsPlayer(damagedBukkitPlayer);
            NovsPlayer damager = playerManager.getNovsPlayer(damagerBukkitPlayer);
            NovsTeam damagedTeam = game.getPlayerTeam(damaged);
            NovsTeam damagerTeam = game.getPlayerTeam(damager);

            if (damagerTeam.equals(damagedTeam)) {
                if (!damagerTeam.getFriendlyFire()) {
                    event.setCancelled(true);
                    return;
                }
            }

            if (!game.getPlayerTeam(damaged).canBeDamaged()) {
                event.setCancelled(true);
                return;
            }

            double damage = event.getFinalDamage();
            damaged.getStats().incrementDamageTaken(damage);
            damager.getStats().incrementDamageGiven(damage);

            // if damage is fatal
            if (damagedBukkitPlayer.getHealth() - damage <= 0) {
                event.setCancelled(true);

                NovsWarPlayerKillEvent invokeEvent = new NovsWarPlayerKillEvent(damager, damaged, damagerTeam, damagedTeam, game);
                Bukkit.getPluginManager().callEvent(invokeEvent);

                String deathMessage;
                if (arrowDeath) {
                    deathMessage = Messages.SHOT_MESSAGE.toString();
                } else {
                    deathMessage = Messages.KILL_MESSAGE.toString();
                }
                deathMessage = deathMessage.replace("%killed_tcolor%", damagedTeam.getColor().toString())
                        .replace("%killed%", damagedBukkitPlayer.getDisplayName())
                        .replace("%killer_tcolor%", damagerTeam.getColor().toString())
                        .replace("%killer%", damagerBukkitPlayer.getDisplayName());

                for (NovsPlayer p : playerManager.getPlayers()) {
                    if (p.canSeeDeathMessages()) {
                        p.getBukkitPlayer().sendMessage(deathMessage);
                    }
                }

                if (arrowDeath) {
                    damager.getStats().incrementArrowKills();
                    damaged.getStats().incrementArrowDeaths();
                } else {
                    damager.getStats().incrementKills();
                    damaged.getStats().incrementDeaths();
                }

                game.scheduleDeath(damaged, game.getGamemode().getDeathTime());
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player bukkitPlayer = (Player) event.getEntity();
            NovsPlayer player = playerManager.getNovsPlayer(bukkitPlayer);

            if (!game.getPlayerTeam(player).canBeDamaged()) {
                event.setCancelled(true);
                return;
            }

            double damage = event.getFinalDamage();
            player.getStats().incrementDamageTaken(damage);

            // if damage is fatal
            if (bukkitPlayer.getHealth() - damage <= 0) {
                event.setCancelled(true);

                String deathMessage = Messages.DEATH_MESSAGE.toString();
                deathMessage = deathMessage.replace("%player_tcolor%", game.getPlayerTeam(player).getColor().toString())
                .replace("%player%", bukkitPlayer.getDisplayName());

                for (NovsPlayer p : playerManager.getPlayers()) {
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
        NovsPlayer player = playerManager.getNovsPlayer(bukkitPlayer);

        if (player.isSettingRegion()) {
            if (event.getClickedBlock() == null) {
                bukkitPlayer.sendMessage("You need to click a block");
                event.setCancelled(true);
                return;
            }
            Location location = event.getClickedBlock().getLocation();

            if (novswar.getWorldManager().getWorld(bukkitPlayer.getWorld()) == null) {
                bukkitPlayer.sendMessage("The world you're in isn't enabled in NovsWar.");
                event.setCancelled(true);
                return;
            }

            if (player.getCornerOneBuffer() == null) {
                player.setCornerOneBuffer(location);
                bukkitPlayer.sendMessage("Setting corner two...");
            } else if (player.getCornerOneBuffer() != null) {
                NovsWorld world = novswar.getWorldManager().getWorld(bukkitPlayer.getWorld());
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
}
