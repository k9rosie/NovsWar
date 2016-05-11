package com.k9rosie.novswar.gamemode;

import com.k9rosie.novswar.game.Game;

public abstract class Gamemode {

    public abstract void initialize();
    public abstract void setGame(Game game);
    public abstract String getName();
}
