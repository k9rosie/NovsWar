package com.k9rosie.novswar.listener;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.NovsWarPlugin;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.manager.PlayerManager;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
        bukkitPlayer.setScoreboard(game.getScoreboard());
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
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player killedBukkitPlayer = event.getEntity();
        NovsPlayer killed = playerManager.getNovsPlayer(killedBukkitPlayer);
        NovsTeam killedTeam = game.getPlayerTeam(killed);

        Player killerBukkitPlayer = null;
        boolean arrowDeath = false;
        if (event.getEntity().getKiller() instanceof Arrow) {
            killerBukkitPlayer = (Player) ((Arrow) event.getEntity().getKiller()).getShooter();
            arrowDeath = true;
        } else if (event.getEntity().getKiller() == null) { // if death was a suicide
            String deathMessage = Messages.DEATH_MESSAGE.toString().replace("%player_tcolor%", killedTeam.getColor().toString())
                    .replace("%player%", killedBukkitPlayer.getDisplayName());

            for (NovsPlayer p : playerManager.getPlayers()) {
                if (p.canSeeDeathMessages()) {
                    p.getBukkitPlayer().sendMessage(deathMessage);
                }
            }

            killed.getStats().incrementSuicides();
            return;
        } else {
            killerBukkitPlayer = event.getEntity().getKiller();
        }

        NovsPlayer killer = playerManager.getNovsPlayer(killerBukkitPlayer);
        NovsTeam killerTeam = game.getPlayerTeam(killer);

        game.getGamemode().onPlayerKill(killer, killed, false);
        event.setDeathMessage("");
        String deathMessage;
        if (arrowDeath) {
            deathMessage = Messages.SHOT_MESSAGE.toString();
        } else {
            deathMessage = Messages.KILL_MESSAGE.toString();
        }
        deathMessage = deathMessage.replace("%killed_tcolor%", killedTeam.getColor().toString())
                .replace("%killed%", killedBukkitPlayer.getDisplayName())
                .replace("%killer_tcolor%", killerTeam.getColor().toString())
                .replace("%killer%", killerBukkitPlayer.getDisplayName());

        for (NovsPlayer p : playerManager.getPlayers()) {
            if (p.canSeeDeathMessages()) {
                p.getBukkitPlayer().sendMessage(deathMessage);
            }
        }

        if (arrowDeath) {
            killer.getStats().incrementArrowKills();
            killed.getStats().incrementArrowDeaths();
        } else {
            killer.getStats().incrementKills();
            killed.getStats().incrementDeaths();
        }

        // TODO: respawn player at team spawn point
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
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damagerBukkitPlayer = (Player) event.getDamager();
            Player damagedBukkitPlayer = (Player) event.getEntity();
            NovsPlayer damager = playerManager.getNovsPlayer(damagerBukkitPlayer);
            NovsPlayer damaged = playerManager.getNovsPlayer(damagedBukkitPlayer);
            NovsTeam damagerTeam = game.getPlayerTeam(damager);
            NovsTeam damagedTeam = game.getPlayerTeam(damaged);

            if (damagerTeam.equals(damagedTeam)) {
                if (!damagerTeam.getFriendlyFire()) {
                    event.setCancelled(true);
                    return;
                }
            }

            double damage = event.getFinalDamage();

            damager.getStats().incrementDamageGiven(damage);
            damaged.getStats().incrementDamageTaken(damage);
        }
    }

}
