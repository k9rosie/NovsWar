package com.k9rosie.novswar.event;

import com.k9rosie.novswar.model.NovsScore;
import com.k9rosie.novswar.model.NovsTeam;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.Score;

public class NovsWarScoreModifyEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private NovsTeam team;
    private NovsScore score;

    public NovsWarScoreModifyEvent(NovsScore score) {
        this.score = score;
        team = score.getTeam();
    }

    public NovsTeam getTeam() {
        return team;
    }

    public NovsScore getNovsScore() {
        return score;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
 }
