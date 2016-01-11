package com.k9rosie.novswar.model;

import org.bukkit.ChatColor;

import java.util.ArrayList;

public class NovsTeam {

    private String name;
    private ChatColor color;
    private boolean canBeDamaged;
    private boolean canAttack;

    private NovsTeam(String name, ChatColor color) {
        this.name = name;
        this.color = color;
        canBeDamaged = true;
        canAttack = true;
    }

}
