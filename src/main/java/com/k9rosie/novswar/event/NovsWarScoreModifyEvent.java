package com.k9rosie.novswar.event;

import com.k9rosie.novswar.model.NovsTeam;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.Score;

public class NovsWarScoreModifyEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private NovsTeam team;
    private int newScore;
    private int oldScore;
    private boolean cancelled;

    public NovsWarScoreModifyEvent(NovsTeam team, int newScore, int oldScore) {
        this.team = team;
        this.newScore = newScore;
        this.oldScore = oldScore;
        cancelled = false;
    }

    public NovsTeam getTeam() {
        return team;
    }

    public int getNewScore() {
        return newScore;
    }

    public int getOldScore() {
        return oldScore;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
 }
