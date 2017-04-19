package com.k9rosie.novswar.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.player.NovsPlayer;

public class NovsWarLeaveTeamEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private NovsPlayer player;
    private Game game;
    private boolean cancelled;
    
    public NovsWarLeaveTeamEvent(NovsPlayer player, Game game) {
    	this.player = player;
        this.game = game;
        cancelled = false;
    }

    public NovsPlayer getPlayer() {
    	return player;
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
