package com.k9rosie.novswar.gamemode;

import com.k9rosie.novswar.game.Game;

public abstract class Gamemode {
    private final String name;
    private Game game;
    private NovsWar novsWarInstance;

    public Gamemode(String name) {
        this.name = name;
        novsWarInstance = NovsWar.getInstance();
    }

    public void setGame(Game game) {
        this.game = game;
    }
    
    /*
        hook() should be called when the Gamemode plugin is enabled
    */
    public void hook() {
        novsWarInstance.getGamemodeHandler().getGamemodes().put(name, this);
    }
    
    /*
        onNewGame() is called at the beginning of every game
    */
    public abstract void onNewGame();
}
