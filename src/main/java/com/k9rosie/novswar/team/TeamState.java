package com.k9rosie.novswar.team;

import com.k9rosie.novswar.config.MessagesConfig;
import com.k9rosie.novswar.event.NovsWarJoinTeamEvent;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;

public class TeamState {
    private Game game;
    private NovsTeam team;
    private ArrayList<NovsPlayer> players;
    private NovsScore score;
    private Team scoreboardTeam;

    public TeamState(Game game, NovsTeam team, Team scoreboardTeam) {
        this.game = game;
        this.team = team;
        this.scoreboardTeam = scoreboardTeam;
        players = new ArrayList<>();
        score = new NovsScore(team);
    }

    public Game getGame() {
        return game;
    }

    public NovsTeam getTeam() {
        return team;
    }

    public Team getScoreboardTeam() {
        return scoreboardTeam;
    }

    public NovsScore getScore() {
        return score;
    }

    public ArrayList<NovsPlayer> getPlayers() {
        return players;
    }

    public void addPlayer(NovsPlayer player) {
        String message;
        if (game.getTeams().contains(team)) { // if the game has this team enabled
            player.getPlayerState().setTeam(team);
            Player bukkitPlayer = player.getBukkitPlayer();
            bukkitPlayer.teleport(game.getWorld().getTeamSpawnLoc(team));
            bukkitPlayer.setHealth(player.getBukkitPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            bukkitPlayer.setFoodLevel(20);
            message = MessagesConfig.getJoinTeam(team.getColor().toString(), team.getTeamName());
        } else {
            message = MessagesConfig.getCannotJoinTeam(team.getColor().toString(), team.getTeamName());
        }

        ChatUtil.sendNotice(player.getBukkitPlayer(), message);
        NovsWarJoinTeamEvent event = new NovsWarJoinTeamEvent(game, player, team);
        Bukkit.getServer().getPluginManager().callEvent(event);
    }

}
