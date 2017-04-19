package com.k9rosie.novswar.config;

public class TeamData {
    private String name;
    private String color;
    private boolean canBeDamaged;
    private boolean canAttack;
    private boolean friendlyFire;

    public TeamData(String name, String color, boolean canBeDamaged, boolean canAttack, boolean friendlyFire) {
        this.name = name;
        this.color = color;
        this.canBeDamaged = canBeDamaged;
        this.canAttack = canAttack;
        this.friendlyFire = friendlyFire;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public boolean isCanBeDamaged() {
        return canBeDamaged;
    }

    public boolean isCanAttack() {
        return canAttack;
    }

    public boolean isFriendlyFire() {
        return friendlyFire;
    }
}
