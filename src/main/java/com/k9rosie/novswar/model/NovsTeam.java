package com.k9rosie.novswar.model;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashSet;

public class NovsTeam {

    private String name;
    private ChatColor color;
    private boolean canBeDamaged;
    private boolean canAttack;

    public NovsTeam(String name, ChatColor color, boolean canBeDamaged, boolean canAttack) {
        this.name = name;
        this.color = color;
        this.canBeDamaged = canBeDamaged;
        this.canAttack = canAttack;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
