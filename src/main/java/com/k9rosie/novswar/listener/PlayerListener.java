package com.k9rosie.novswar.listener;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.NovsWarPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    private NovsWarPlugin plugin;
    private NovsWar novswar;

    public PlayerListener(NovsWarPlugin plugin) {
        this.plugin = plugin;
        novswar = plugin.getNovswarInstance();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        novswar.getPlayerManager().createNovsPlayer(player);

        // TODO: if lobby enabled:
        //   put player on default team
        // TODO: if lobby not enabled:
        //   if join_in_progress false:
        //     kick player and let them know a game is in progress
        //  if join_in_progress true:
        //     put them on a team
        // TODO: spawn player on team spawn point
    }

}
