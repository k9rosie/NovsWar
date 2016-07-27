package com.k9rosie.novswar.event;

import com.k9rosie.novswar.NovsWar;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NovsWarInitializationEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private NovsWar novswar;

    public NovsWarInitializationEvent(NovsWar novswar) {
        this.novswar = novswar;
    }

    public NovsWar getNovsWar() {
        return novswar;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
