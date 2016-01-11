package com.k9rosie.novswar.listener;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.NovsWarPlugin;
import org.bukkit.event.Listener;

public class PlayerListener implements Listener {

    private NovsWarPlugin plugin;
    private NovsWar novswar;

    public PlayerListener(NovsWarPlugin plugin) {
        this.plugin = plugin;
        novswar = plugin.getNovswarInstance();
    }



}
