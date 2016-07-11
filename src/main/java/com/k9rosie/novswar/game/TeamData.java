package com.k9rosie.novswar.game;

import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;

public class TeamData {

    private NovsTeam team;
    private Team scoreboardTeam;
    private HashSet<NovsPlayer> players;
    private int score;

    public TeamData(NovsTeam team, Team scoreboardTeam) {
        this.team = team;
        this.scoreboardTeam = scoreboardTeam;
        players = new HashSet<NovsPlayer>();
        score = 0;
    }

    public NovsTeam getTeam() {
        return team;
    }

    public HashSet<NovsPlayer> getPlayers() {
        return players;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setScoreboardTeam(Team scoreboardTeam) {
        this.scoreboardTeam = scoreboardTeam;
    }

    public Team getScoreboardTeam() {
        return scoreboardTeam;
    }

    public void incrementScore() {
        score++;
    }

    public void incrementScore(int increment) {
        score += increment;
    }

    public void decrementScore() {
        score--;
    }

    public void decrementScore(int increment) {
        score -= increment;
    }
}
