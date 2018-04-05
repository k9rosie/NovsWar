package com.k9rosie.novswar.gamemode;

import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.team.NovsTeam;

import java.util.ArrayList;
import java.util.HashMap;

public interface Gamemode {

    /**
     * Gets the initial amount of time a round has
     * @return
     */
    int getGameTime(); // TODO: make it so that a gameTime of -1 allows matches to continue forever

    /**
     * Gets the amount of time a player's death time is
     * @param player
     * @return
     */
    int getDeathTime(NovsPlayer player);

    /**
     * Gets the initial scores for each team
     * @return
     */

    HashMap<NovsTeam, Integer> getInitialScores();

    /**
     * Gets the number of rounds to be played
     * @return
     */
    int getRounds();

    /**
     * Gets the name of the gamemode
     * @return
     */
    String getGamemodeName();

    /**
     * Gets a list of the winning teams at the end of a round
     * @return
     */
    NovsTeam[] getWinningTeams();

    void setInitialScores();

}
