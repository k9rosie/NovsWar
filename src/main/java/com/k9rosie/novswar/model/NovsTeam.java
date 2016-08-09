package com.k9rosie.novswar.model;

import com.k9rosie.novswar.NovsWar;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;

public class NovsTeam {

    private String teamName;
    private ChatColor color;
    private Team scoreboardTeam;
    private boolean canBeDamaged;
    private boolean canAttack;
    private boolean friendlyFire;
    private NovsScore score;

    public NovsTeam(String teamName, ChatColor color, boolean canBeDamaged, boolean canAttack, boolean friendlyFire) {
        this.teamName = teamName;
        this.color = color;
        this.canBeDamaged = canBeDamaged;
        this.canAttack = canAttack;
        this.friendlyFire = friendlyFire;
        score = new NovsScore(this);
    }
    
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public boolean canBeDamaged() {
        return canBeDamaged;
    }

    public void setCanBeDamaged(boolean canBeDamaged) {
        this.canBeDamaged = canBeDamaged;
    }

    public boolean canAttack() {
        return canAttack;
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    public boolean getFriendlyFire() {
        return friendlyFire;
    }

    public NovsScore getNovsScore() {
        return score;
    }

    public void setScoreboardTeam(Team scoreboardTeam) {
        this.scoreboardTeam = scoreboardTeam;
    }

    public Team getScoreboardTeam() {
        return scoreboardTeam;
    }

    public HashSet<NovsPlayer> getPlayers() {
        HashSet<NovsPlayer> teamMembers = new HashSet<NovsPlayer>();
        for (NovsPlayer player : NovsWar.getInstance().getNovsPlayerCache().getPlayers().values()) {
            if (player.getTeam().equals(this)) {
                teamMembers.add(player);
            }
        }

        return teamMembers;
    }
}
