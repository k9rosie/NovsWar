package com.k9rosie.novswar.event;

import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.team.NovsTeam;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NovsWarPlayerAssistEvent  extends Event {
	
	private static final HandlerList handlers = new HandlerList();
    private NovsPlayer assistAttacker;
    private NovsPlayer victim;
    private NovsTeam assistAttackerTeam;
    private NovsTeam victimTeam;
    private Game game;
    private boolean cancelled;

    public NovsWarPlayerAssistEvent(NovsPlayer killer, NovsPlayer killed, NovsTeam killerTeam, NovsTeam killedTeam, Game game) {
        this.game = game;
        this.assistAttacker = killer;
        this.victim = killed;
        this.assistAttackerTeam = killerTeam;
        this.victimTeam = killedTeam;
        cancelled = false;
    }

    public NovsPlayer getAssistAttacker() {
        return assistAttacker;
    }

    public NovsPlayer getKilled() {
        return victim;
    }

    public NovsTeam getAssistAttackerTeam() {
        return assistAttackerTeam;
    }

    public NovsTeam getKilledTeam() {
        return victimTeam;
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
