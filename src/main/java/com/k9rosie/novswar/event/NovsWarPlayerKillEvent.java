package com.k9rosie.novswar.event;

import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NovsWarPlayerKillEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private NovsPlayer killer;
    private NovsPlayer killed;
    private NovsTeam killerTeam;
    private NovsTeam killedTeam;
    private Game game;
    private boolean cancelled;

    public NovsWarPlayerKillEvent(NovsPlayer killer, NovsPlayer killed, NovsTeam killerTeam, NovsTeam killedTeam, Game game) {
        this.game = game;
        this.killer = killer;
        this.killed = killed;
        cancelled = false;
    }

    public NovsPlayer getKiller() {
        return killer;
    }

    public NovsPlayer getKilled() {
        return killed;
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
