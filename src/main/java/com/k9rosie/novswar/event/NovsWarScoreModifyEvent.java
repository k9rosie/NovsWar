package com.k9rosie.novswar.event;

import com.k9rosie.novswar.team.NovsScore;
import com.k9rosie.novswar.team.NovsTeam;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

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
