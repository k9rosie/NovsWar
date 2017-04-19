package com.k9rosie.novswar.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.team.NovsTeam;

public class NovsWarJoinTeamEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Game game;
    private NovsPlayer player;
    private NovsTeam team;
    private boolean cancelled;

    public NovsWarJoinTeamEvent(Game game, NovsPlayer player, NovsTeam team) {
        this.game = game;
        this.player = player;
        this.team = team;
        cancelled = false;
    }

    public Game getGame() {
        return game;
    }

    public NovsPlayer getPlayer() {
        return player;
    }
    
    public NovsTeam getTeam() {
    	return team;
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
