package com.k9rosie.novswar.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.team.NovsTeam;

public class NovsWarPlayerDeathEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
    private NovsPlayer victim;
    private NovsTeam victimTeam;
    private Game game;
    private boolean cancelled;
    private boolean suicide;

    public NovsWarPlayerDeathEvent(NovsPlayer victim, NovsTeam victimTeam, boolean suicide, Game game) {
        this.game = game;
        this.victim = victim;
        this.victimTeam = victimTeam;
        this.suicide = true;
        cancelled = false;
    }

    public NovsPlayer getVictim() {
        return victim;
    }

    public NovsTeam getVictimTeam() {
        return victimTeam;
    }

    public boolean isSuicide() { return suicide; }

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
