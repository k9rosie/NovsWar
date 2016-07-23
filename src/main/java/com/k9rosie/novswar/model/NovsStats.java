package com.k9rosie.novswar.model;

import com.k9rosie.novswar.NovsWar;

public class NovsStats {

    private NovsPlayer player;

    private int kills;
    private int arrowKills;
    private int deaths;
    private int arrowDeaths;
    private int suicides;
    private int wins;
    private int losses;
    private int connects;
    private double damageGiven;
    private double damageTaken;

    public NovsStats(NovsPlayer player) {
        this.player = player;
    }

    public NovsPlayer getPlayer() {
        return player;
    }

    public void setPlayer(NovsPlayer player) {
        this.player = player;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void incrementKills() {
        kills++;
    }

    public int getArrowKills() {
        return arrowKills;
    }

    public void setArrowKills(int arrowKills) {
        this.arrowKills = arrowKills;
    }

    public void incrementArrowKills() {
        arrowKills++;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void incrementDeaths() {
        deaths++;
    }

    public int getArrowDeaths() {
        return arrowDeaths;
    }

    public void setArrowDeaths(int arrowDeaths) {
        this.arrowDeaths = arrowDeaths;
    }

    public void incrementArrowDeaths() {
        arrowDeaths++;
    }

    public int getSuicides() {
        return suicides;
    }

    public void setSuicides(int suicides) {
        this.suicides = suicides;
    }

    public void incrementSuicides() {
        suicides++;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void incrementWins() {
        wins++;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void incrementLosses() {
        losses++;
    }

    public int getConnects() {
        return connects;
    }

    public void setConnects(int connects) {
        this.connects = connects;
    }

    public void incrementConnects() {
        connects++;
    }

    public double getDamageGiven() {
        return damageGiven;
    }

    public void setDamageGiven(double damageGiven) {
        this.damageGiven = damageGiven;
    }

    public void incrementDamageGiven() {
        damageGiven++;
    }

    public void incrementDamageGiven(double damage) {
        damageGiven += damage;
    }

    public double getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(double damageTaken) {
        this.damageTaken = damageTaken;
    }

    public void incrementDamageTaken() {
        damageTaken++;
    }

    public void incrementDamageTaken(double damage) {
        damageTaken += damage;
    }
}
