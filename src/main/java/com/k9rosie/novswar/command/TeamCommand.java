package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TeamCommand extends NovsCommand {

    public TeamCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
    }

    public void execute() {
        NovsPlayer player = getNovsWar().getPlayerManager().getPlayerFromBukkitPlayer((Player) getSender());
        NovsTeam team = getNovsWar().getGameHandler().getGame().getGamePlayers().get(player);

        StringBuilder playersList = new StringBuilder();
        HashMap<NovsPlayer, NovsTeam> players = getNovsWar().getGameHandler().getGame().getGamePlayers();
        Object[] playersArray = players.keySet().toArray();
        for (int i = 0; i < playersArray.length; i++) {
            NovsPlayer p = (NovsPlayer) playersArray[i];
            if (players.get(p).equals(team)) {
                playersList.append(team.getColor()+p.getBukkitPlayer().getDisplayName());
                if (i != playersArray.length-1) {
                    playersList.append(ChatColor.GRAY+", ");
                }
            }
        }
        getSender().sendMessage(team.getColor()+team.getTeamName());
        getSender().sendMessage(playersList.toString());

    }
}
