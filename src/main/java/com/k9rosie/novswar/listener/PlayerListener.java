package com.k9rosie.novswar.listener;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.NovsWarPlugin;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.util.packet.NametagEdit;
import org.bukkit.Bukkit;
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
    private Game game;

    public PlayerListener(NovsWarPlugin plugin) {
        this.plugin = plugin;
        novswar = plugin.getNovswarInstance();
        game = novswar.getGameHandler().getGame();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player bukkitPlayer = event.getPlayer();
        NovsPlayer player = novswar.getPlayerManager().createNovsPlayer(bukkitPlayer);
        NovsTeam defaultTeam = novswar.getTeamManager().getDefaultTeam();

        novswar.getDatabase().fetchPlayerData(player);
        game.getTeamData().get(defaultTeam).getPlayers().add(player);
        bukkitPlayer.teleport(novswar.getWorldManager().getLobbyWorld().getTeamSpawns().get(defaultTeam));

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            NametagEdit nametagEdit = new NametagEdit("novswar", defaultTeam.getColor()+bukkitPlayer.getDisplayName(), defaultTeam.getColor());
            nametagEdit.addPlayers(Bukkit.getServer().getOnlinePlayers());
            nametagEdit.sendToPlayer(p);
        }
        player.getStats().incrementConnects();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player bukkitPlayer = event.getPlayer();
        NovsPlayer player = novswar.getPlayerManager().getNovsPlayer(bukkitPlayer);
        NovsTeam team = game.getPlayerTeam(player);

        event.setFormat(team.getColor() + bukkitPlayer.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player bukkitPlayer = event.getPlayer();
        NovsPlayer player = novswar.getPlayerManager().getNovsPlayer(bukkitPlayer);
        NovsTeam team = game.getPlayerTeam(player);

        novswar.getDatabase().flushPlayerData(player);
        game.getTeamData().get(team).getPlayers().remove(player);
        novswar.getPlayerManager().getPlayers().remove(player);
    }

}
