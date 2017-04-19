package com.k9rosie.novswar.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.k9rosie.novswar.player.NovsPlayer;

public class NovsWarJoinServerEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private NovsPlayer player;
    private boolean cancelled;

    public NovsWarJoinServerEvent(NovsPlayer player) {
        this.player = player;
        cancelled = false;
    }

    public NovsPlayer getPlayer() {
        return player;
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
