package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
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
        Game game = getNovsWar().getGameHandler().getGame();
        NovsPlayer player = getNovsWar().getPlayerManager().getNovsPlayer((Player) getSender());
        NovsTeam team = game.getPlayerTeam(player);

        StringBuilder playersList = new StringBuilder();
        Object[] playersArray = game.getTeamData().get(team).getPlayers().toArray();
        for (int i = 0; i < playersArray.length; i++) {
            NovsPlayer p = (NovsPlayer) playersArray[i];
            if (game.getPlayerTeam(p).equals(team)) {
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
