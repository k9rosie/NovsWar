package com.k9rosie.novswar.gamemode;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.team.NovsTeam;

import java.util.ArrayList;
import java.util.HashMap;

public class DefaultGamemode implements Gamemode {

    public String getGamemodeName() {
        return "default";
    }

    public int getGameTime() {
        return 120;
    }

    public int getDeathTime(NovsPlayer player) {
        return 5;
    }

    public HashMap<NovsTeam, Integer> getInitialScores() {

        HashMap<NovsTeam, Integer> scores = new HashMap<>();

        for (NovsTeam team : NovsWar.getInstance().getGameHandler().getGame().getEnabledTeams()) {
            scores.put(team, 10);
        }

        return scores;
    }

    public int getRounds() {
        return 1;
    }

    public void setInitialScores() {
        for (NovsTeam team : NovsWar.getInstance().getGameHandler().getGame().getTeams()) {
            team.getTeamState().getScore().setScore(10);
        }
    }

    public NovsTeam[] getWinningTeams() {
        ArrayList<NovsTeam> teams = new ArrayList<>();
        for (NovsTeam team : NovsWar.getInstance().getGameHandler().getGame().getTeams()) {
            teams.add(team);
        }

        return (NovsTeam[]) teams.toArray();
    }
}
