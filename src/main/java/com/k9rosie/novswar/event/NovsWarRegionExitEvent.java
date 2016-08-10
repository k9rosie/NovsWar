package com.k9rosie.novswar.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsRegion;

public class NovsWarRegionExitEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
	private NovsPlayer player;
    private Game game;
    private NovsRegion region;
    private boolean cancelled;
    
    public NovsWarRegionExitEvent(Game game, NovsPlayer player, NovsRegion region) {
    	this.player = player;
        this.game = game;
        this.region = region;
        cancelled = false;
    }

    public NovsPlayer getPlayer() {
    	return player;
    }

    public Game getGame() {
        return game;
    }
    
    public NovsRegion getRegion() {
    	return region;
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
