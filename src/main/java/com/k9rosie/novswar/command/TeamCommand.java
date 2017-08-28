package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.team.NovsTeam;
import com.k9rosie.novswar.util.ChatUtil;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;

public class TeamCommand implements NovsCommand {
    private String permissions;
    private String description;
    private int requiredNumofArgs;
    private boolean playerOnly;
    private NovsWar novsWar;

    public TeamCommand(NovsWar novsWar) {
        permissions = "novswar.command.team";
        description = "Display team info";
        requiredNumofArgs = 0;
        playerOnly = false;
        this.novsWar = novsWar;
    }

    public void execute(CommandSender sender, String[] args) {
    	if (args.length <= 1 && !(sender instanceof Player)) {
            ChatUtil.sendError(sender, "You need to be a player to display stats on another player");
            return;
        }

        Game game = novsWar.getGameHandler().getGame();

        if (args.length == 1) {
            NovsPlayer player = novsWar.getPlayerManager().getPlayers().get(sender);
            NovsTeam team = player.getPlayerState().getTeam();
            printTeam(sender, team);
        } else if (args.length == 2) {
            String arg = args[1];
            NovsTeam team = null;
            for (NovsTeam t : game.getTeams()) {
                if (t.getTeamName().equalsIgnoreCase(arg)) {
                    team = t;
                }
            }

            if (team != null) {
                printTeam(sender, team);
                return;
            } else {
                NovsPlayer target = novsWar.getPlayerManager().getPlayer(arg);

                if (target == null) {
                	ChatUtil.sendNotice(sender, "That specific player/team couldn't be found");
                    return;
                } else {
                    printTeam(sender, target);
                    return;
                }
            }

        }
    }

    private void printTeam(CommandSender sender, NovsTeam team) {
        ChatUtil.sendNotice(sender, team.getColor()+team.getTeamName());
        sender.sendMessage(generatePlayerList(team));
    }

    private void printTeam(CommandSender sender, NovsPlayer player) {
        NovsTeam team = player.getPlayerState().getTeam();
        ChatUtil.sendNotice(sender, team.getColor()+team.getTeamName());
        sender.sendMessage(generatePlayerList(team));
    }

    private String generatePlayerList(NovsTeam team) {
        StringBuilder playersList = new StringBuilder();
        ArrayList<NovsPlayer> players = team.getTeamState().getPlayers();
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

    @Override
    public String getPermissions() {
        return permissions;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getRequiredNumofArgs() {
        return requiredNumofArgs;
    }

    @Override
    public boolean isPlayerOnly() {
        return playerOnly;
    }
}
