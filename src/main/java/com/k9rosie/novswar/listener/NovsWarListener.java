package com.k9rosie.novswar.listener;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.NovsWarPlugin;
import com.k9rosie.novswar.event.NovsWarScoreModifyEvent;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.model.NovsScore;
import com.k9rosie.novswar.model.NovsTeam;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class NovsWarListener implements Listener {
    private NovsWarPlugin plugin;
    private NovsWar novswar;

    public NovsWarListener(NovsWarPlugin plugin) {
        this.plugin = plugin;
        novswar = plugin.getNovswarInstance();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onNovsWarScoreModify(NovsWarScoreModifyEvent event) {
        NovsTeam team = event.getTeam();
        NovsScore score = event.getNovsScore();
        Game game = novswar.getGameHandler().getGame();
        int maxScore = game.getGamemode().getMaxScore();
        if (score.getScore() >= maxScore) {
            game.endGame();
        }
    }
}
