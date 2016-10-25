package com.k9rosie.novswar.gamemode;

public interface Gamemode {

    enum ScoreType {
        ASCENDING,
        DESCENDING
    }

    int getGameTime();
    int getDeathTime();
    int getMaxScore();
    int getRounds();
    ScoreType getScoreType();
    String getGamemodeName();

}
