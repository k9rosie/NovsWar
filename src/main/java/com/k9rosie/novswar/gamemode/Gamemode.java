package com.k9rosie.novswar.gamemode;

import com.k9rosie.novswar.team.NovsTeam;

import java.util.ArrayList;

public interface Gamemode {

    int getGameTime();
    int getDeathTime();
    int getMaxScore();
    int getRounds();
    String getGamemodeName();
    void setInitialScores();
    ArrayList<NovsTeam> getWinningTeams();

}
