package com.k9rosie.novswar.gamemode;

import com.k9rosie.novswar.game.Game;

public abstract class Gamemode {
    private Game game;
    private String name;

    public Gamemode(String name, Game game) {
        this.name = name;
        this.game = game;

    }

    public abstract void initialize();

    public void setGame(Game game) {
        this.game = game;
    }
}
