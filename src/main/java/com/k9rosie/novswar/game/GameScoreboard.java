package com.k9rosie.novswar.game;

import com.k9rosie.novswar.team.NovsTeam;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class GameScoreboard {

    private Game game;
    private Scoreboard scoreboard;
    private Objective sidebar;
    private Objective healthObjective;
    private String displayName;

    public GameScoreboard(Game game) {
        this.game = game;
        scoreboard = game.getGameHandler().getScoreboardManager().getNewScoreboard();
        sidebar = scoreboard.registerNewObjective("sidebar", "dummy");
        healthObjective = scoreboard.registerNewObjective("showhealth", "health");
        displayName = "";
    }

    public void initialize() {
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (NovsTeam team : game.getTeams()) {
            Score score = sidebar.getScore(team.getColor() + team.getTeamName());
            team.getTeamState().getScore().setScoreboardScore(score);
            score.setScore(team.getTeamState().getScore().value());
        }

        healthObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        healthObjective.setDisplayName("/ 20");

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
    }

    public Team createScoreboardTeam(NovsTeam team) {
        Team scoreboardTeam = scoreboard.registerNewTeam(team.getColor()+team.getTeamName());
        scoreboardTeam.setPrefix(team.getColor().toString());
        scoreboardTeam.setDisplayName(team.getColor()+team.getTeamName());
        scoreboardTeam.setAllowFriendlyFire(team.getFriendlyFire());
        scoreboardTeam.setCanSeeFriendlyInvisibles(false);
        return scoreboardTeam;
    }

    public void setSidebarTitle(String displayName) {
        this.displayName = displayName;
        sidebar.setDisplayName(displayName);
    }

    public String getSidebarTitle() {
        return displayName;
    }

    public Scoreboard getBukkitScoreboard() {
        return scoreboard;
    }
}
