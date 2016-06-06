package com.k9rosie.novswar.listener;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.NovsWarPlugin;
import com.k9rosie.novswar.model.NovsPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private NovsWarPlugin plugin;
    private NovsWar novswar;

    public PlayerListener(NovsWarPlugin plugin) {
        this.plugin = plugin;
        novswar = plugin.getNovswarInstance();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player bukkitPlayer = event.getPlayer();
        NovsPlayer player = novswar.getPlayerManager().createNovsPlayer(bukkitPlayer);

        novswar.getDatabase().fetchPlayerData(player);
        novswar.getTeamManager().setPlayerTeam(player, novswar.getTeamManager().getDefaultTeam());
        bukkitPlayer.teleport(novswar.getWorldManager().getLobbyWorld().getTeamSpawns().get(player.getTeam()));

        player.getStats().incrementConnects();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player bukkitPlayer = event.getPlayer();
        NovsPlayer player = novswar.getPlayerManager().getPlayerFromBukkitPlayer(bukkitPlayer);

        event.setFormat(player.getTeam().getColor() + bukkitPlayer.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player bukkitPlayer = event.getPlayer();
        NovsPlayer player = novswar.getPlayerManager().getPlayerFromBukkitPlayer(bukkitPlayer);

        novswar.getDatabase().flushPlayerData(player);
    }

}
