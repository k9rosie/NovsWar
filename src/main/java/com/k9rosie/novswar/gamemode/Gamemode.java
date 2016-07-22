package com.k9rosie.novswar.gamemode;

import com.k9rosie.novswar.game.Game;

public interface Gamemode {

    public int getGameTime();

    public int getDeathTime();
    
    public int getMaxScore();

    public String getGamemodeName();

    public void hook(Game game);

}
