package com.k9rosie.novswar.model;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashSet;

public class NovsTeam {

    private ChatColor color;
    private boolean canBeDamaged;
    private boolean canAttack;

    public NovsTeam(ChatColor color, boolean canBeDamaged, boolean canAttack) {

        this.color = color;
        this.canBeDamaged = canBeDamaged;
        this.canAttack = canAttack;
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
