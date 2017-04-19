package com.k9rosie.novswar.event;

import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.team.NovsTeam;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NovsWarTeamVictoryEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
    private NovsTeam victorTeam;
    private Game game;
    private boolean cancelled;
    
    public NovsWarTeamVictoryEvent(NovsTeam victorTeam, Game game) {
        this.game = game;
        this.victorTeam = victorTeam;
        cancelled = false;
    }

    public NovsTeam getVictorTeam() {
        return victorTeam;
    }

    public Game getGame() {
        return game;
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
