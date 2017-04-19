package com.k9rosie.novswar.event;

import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.player.NovsPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NovsWarJoinGameEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Game game;
    private NovsPlayer player;
    private boolean cancelled;

    public NovsWarJoinGameEvent(Game game, NovsPlayer player) {
        this.game = game;
        this.player = player;
        cancelled = false;
    }

    public Game getGame() {
        return game;
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
