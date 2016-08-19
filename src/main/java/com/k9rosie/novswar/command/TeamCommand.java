package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.util.ChatUtil;

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
    	NovsPlayer player = getNovsWar().getNovsPlayerCache().getPlayers().get((Player) getSender());
    	if (getArgs().length == 1) {
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
                NovsPlayer target = getNovsWar().getNovsPlayerCache().getPlayerFromName(arg);

                if (target == null) {
                	ChatUtil.sendNotice(player, "That specific player/team couldn't be found");
                    return;
                } else {
                    printTeam(target);
                    return;
                }
            }

        }
    }

    public void printTeam(NovsTeam team) {
        ChatUtil.sendNotice((Player) getSender(), team.getColor()+team.getTeamName());
        getSender().sendMessage(generatePlayerList(team));
    }

    public void printTeam(NovsPlayer player) {
        NovsTeam team = player.getTeam();
        ChatUtil.sendNotice((Player) getSender(), team.getColor()+team.getTeamName());
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
        /*if (team.equals(getNovsWar().getNovsTeamCache().getDefaultTeam())) {
            playersArray = game.getNeutralTeamData().getPlayers().toArray();
        } else {
            playersArray = game.getTeamData().get(team).getPlayers().toArray();
        }

        }*/
        return playersList.toString();
    }
}
