package com.k9rosie.novswar.event;

import com.k9rosie.novswar.gamemode.GamemodeHandler;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GamemodeHandlerInitialization extends Event {
    private static final HandlerList handlers = new HandlerList();
    private GamemodeHandler gamemodeHandler;

    public GamemodeHandlerInitialization(GamemodeHandler gamemodeHandler) {
        this.gamemodeHandler = gamemodeHandler;
    }

    public GamemodeHandler getGamemodeHandler() {
        return gamemodeHandler;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
