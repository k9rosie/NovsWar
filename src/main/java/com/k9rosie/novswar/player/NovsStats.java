package com.k9rosie.novswar.player;

import com.k9rosie.novswar.player.NovsPlayer;

import java.sql.Timestamp;
import java.util.Date;

public class NovsStats {

    private NovsPlayer player;

    private int kills;
    private int arrowKills;
    private int deaths;
    private int arrowDeaths;
    private int suicides;
    private int wins;
    private int gamesPlayed;
    private int connects;
    private double damageGiven;
    private double damageTaken;
    private Date lastPlayed;
    private Timestamp loggedIn;
    private long totalTime;

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

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void incrementGamesPlayed() {
        gamesPlayed++;
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

    public Date getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(Date lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public Timestamp getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(Timestamp loggedIn) {
        this.loggedIn = loggedIn;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public void incrementTotalTime(long totalTime) {
        this.totalTime += totalTime;
    }
}
