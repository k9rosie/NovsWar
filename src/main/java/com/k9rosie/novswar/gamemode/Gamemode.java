package com.k9rosie.novswar.gamemode;

import com.k9rosie.novswar.game.Game;

public interface Gamemode {

    public void initialize();
    public void setGame(Game game);
    public String getName();
}
