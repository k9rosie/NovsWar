package com.k9rosie.novswar.model;

import com.k9rosie.novswar.event.NovsWarScoreModifyEvent;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Score;

public class NovsScore {
    private NovsTeam team;
    private int score;
    private Score scoreboardScore;

    public NovsScore(NovsTeam team) {
        this.team = team;
        score = 0;
    }

    public NovsScore(NovsTeam team, int score) {
        this.team = team;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;

        if (scoreboardScore != null) {
            scoreboardScore.setScore(score);
        }

        callEvent();
    }

    public void incrementScore() {
        this.score++;

        if (scoreboardScore != null) {
            scoreboardScore.setScore(score);
        }

        callEvent();
    }

    public void incrementScore(int increment) {
        this.score += increment;

        if (scoreboardScore != null) {
            scoreboardScore.setScore(score);
        }

        callEvent();
    }

    public void decrementScore() {
        this.score--;

        if (scoreboardScore != null) {
            scoreboardScore.setScore(score);
        }

        callEvent();
    }

    public void decrementScore(int decrement) {
        this.score -= decrement;

        if (scoreboardScore != null) {
            scoreboardScore.setScore(score);
        }

        callEvent();
    }

    public NovsTeam getTeam() {
        return team;
    }

    public Score getScoreboardScore() {
        return scoreboardScore;
    }

    public void setScoreboardScore(Score scoreboardScore) {
        this.scoreboardScore = scoreboardScore;
    }

    public void callEvent() {
        NovsWarScoreModifyEvent event = new NovsWarScoreModifyEvent(this);
        Bukkit.getPluginManager().callEvent(event);
    }
}
