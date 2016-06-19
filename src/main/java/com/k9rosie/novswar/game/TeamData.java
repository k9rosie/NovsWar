package com.k9rosie.novswar.game;

import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;

import java.util.HashSet;

public class TeamData {

    private NovsTeam team;
    private HashSet<NovsPlayer> players;
    private int score;

    public TeamData(NovsTeam team) {
        this.team = team;
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
