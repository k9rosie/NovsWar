package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsStats;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements ICommand {

    public NovsWar novsWar;
    public CommandSender sender;

    public StatsCommand(NovsWar novsWar, CommandSender sender) {
        this.novsWar = novsWar;
        this.sender = sender;
    }

    public void execute() {
        NovsPlayer player = novsWar.getPlayerManager().getPlayerFromBukkitPlayer((Player) sender);
        NovsStats stats = player.getStats();

        player.getBukkitPlayer().sendMessage(new String[] {
                "Kills: " + stats.getKills(),
                "Deaths: " + stats.getDeaths(),
                "Suicides: " + stats.getSuicides(),
                "Arrow Kills: " + stats.getArrowKills(),
                "Arrow Deaths: " + stats.getArrowDeaths(),
                "Wins: " + stats.getWins(),
                "Losses: " + stats.getLosses(),
                "Damage Given: " + stats.getDamageGiven(),
                "Damange Taken: " + stats.getDamageTaken(),
                "Connects: " + stats.getConnects()
        });
    }
}
