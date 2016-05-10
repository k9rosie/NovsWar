package com.k9rosie.novswar.model;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashSet;

public class NovsTeam {

    private String teamName;
    private ChatColor color;
    private boolean canBeDamaged;
    private boolean canAttack;

    public NovsTeam(String teamName, ChatColor color, boolean canBeDamaged, boolean canAttack) {
        this.teamName = teamName;
        this.color = color;
        this.canBeDamaged = canBeDamaged;
        this.canAttack = canAttack;
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

}
