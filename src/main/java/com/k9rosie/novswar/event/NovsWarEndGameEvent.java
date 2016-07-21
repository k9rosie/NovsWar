package com.k9rosie.novswar.event;

import com.k9rosie.novswar.game.Game;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NovsWarEndGameEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Game game;
    private boolean cancelled;

    public NovsWarEndGameEvent(Game game) {
        this.game = game;
        cancelled = false;
    }

    public Game getGame() {
        return game;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
