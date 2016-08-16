package com.k9rosie.novswar.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.model.NovsPlayer;

public class NovsWarPlayerRespawnEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
    private NovsPlayer player;
    private Game game;
    private boolean cancelled;

    public NovsWarPlayerRespawnEvent(NovsPlayer player, Game game) {
        this.game = game;
        this.player = player;
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
