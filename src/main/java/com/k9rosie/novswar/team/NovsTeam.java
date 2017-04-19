package com.k9rosie.novswar.team;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

public class NovsTeam {

    private String teamName;
    private ChatColor color;
    private boolean canBeDamaged;
    private boolean canAttack;
    private boolean friendlyFire;
    private TeamState teamState;

    public NovsTeam(String teamName, ChatColor color, boolean canBeDamaged, boolean canAttack, boolean friendlyFire) {
        this.teamName = teamName;
        this.color = color;
        this.canBeDamaged = canBeDamaged;
        this.canAttack = canAttack;
        this.friendlyFire = friendlyFire;
    }

    public void setTeamState(TeamState teamState) {
        this.teamState = teamState;
    }

    public TeamState getTeamState() {
        return teamState;
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
}
