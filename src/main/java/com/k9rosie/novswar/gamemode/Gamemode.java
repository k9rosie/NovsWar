package com.k9rosie.novswar.gamemode;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.game.TeamData;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;

import java.sql.Time;

public interface Gamemode {

    public int getGameTime();

    public int getDeathTime();

    public String getGamemodeName();

    public void hook();

}
