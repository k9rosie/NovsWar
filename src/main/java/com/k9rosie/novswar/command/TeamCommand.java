package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class TeamCommand extends NovsCommand {

    private Game game;

    public TeamCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
        game = getNovsWar().getGameHandler().getGame();
    }

    public void execute() {
        if (getArgs().length == 1) {
            NovsPlayer player = getNovsWar().getPlayerManager().getPlayers().get((Player) getSender());
            NovsTeam team = player.getTeam();
            printTeam(team);
        } else if (getArgs().length == 2) {
            String arg = getArgs()[1];
            NovsTeam team = null;
            for (NovsTeam t : game.getTeams()) {
                if (t.getTeamName().equalsIgnoreCase(arg)) {
                    team = t;
                }
            }

            if (team != null) {
                printTeam(team);
                return;
            } else {
                NovsPlayer player = getNovsWar().getPlayerManager().getNovsPlayerFromUsername(arg);

                if (player == null) {
                    getSender().sendMessage("That specific player/team couldn't be found");
                    return;
                } else {
                    printTeam(player);
                    return;
                }
            }

        }
    }

    public void printTeam(NovsTeam team) {
        getSender().sendMessage(team.getColor()+team.getTeamName());
        getSender().sendMessage(generatePlayerList(team));
    }

    public void printTeam(NovsPlayer player) {
        NovsTeam team = player.getTeam();
        getSender().sendMessage(team.getColor()+team.getTeamName());
        getSender().sendMessage(generatePlayerList(team));
    }

    public String generatePlayerList(NovsTeam team) {
        StringBuilder playersList = new StringBuilder();
        HashSet<NovsPlayer> players = team.getPlayers();
        for (int i = 0; i < players.toArray().length; i++) {
            NovsPlayer p = (NovsPlayer) players.toArray()[i];
            playersList.append(team.getColor()+p.getBukkitPlayer().getDisplayName());
            if (i != players.toArray().length-1) {
                playersList.append(ChatColor.GRAY+", ");
            }
            }
        /*if (team.equals(getNovsWar().getTeamManager().getDefaultTeam())) {
            playersArray = game.getNeutralTeamData().getPlayers().toArray();
        } else {
            playersArray = game.getTeamData().get(team).getPlayers().toArray();
        }

        }*/
        return playersList.toString();
    }
}
