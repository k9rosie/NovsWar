package com.k9rosie.novswar.game;

import com.k9rosie.novswar.model.NovsTeam;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.HashMap;

public class GameScoreboard {

    private Game game;
    private Scoreboard scoreboard;
    private Objective sidebar;
    private HashMap<NovsTeam, Score> scores;
    private Objective healthObjective;
    private String displayName;
    private ArrayList<Team> scoreboardTeams;

    public GameScoreboard(Game game) {
        this.game = game;
        scoreboard = game.getGameHandler().getScoreboardManager().getNewScoreboard();
        sidebar = scoreboard.registerNewObjective("sidebar", "dummy");
        healthObjective = scoreboard.registerNewObjective("showhealth", "health");
        scores = new HashMap<NovsTeam, Score>();
        displayName = "";
    }

    public void initialize() {
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (NovsTeam team : game.getTeams()) {
            int score = team.getScore();
            Score scScore = sidebar.getScore(team.getColor() + team.getTeamName());
            scScore.setScore(score);
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
