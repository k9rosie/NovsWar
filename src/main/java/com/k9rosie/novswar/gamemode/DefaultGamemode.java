package com.k9rosie.novswar.gamemode;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.team.NovsTeam;

import java.util.ArrayList;

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

    public int getMaxScore() {
        return 10;
    }

    public int getRounds() {
        return 1;
    }

    public void setInitialScores() {
        for (NovsTeam team : NovsWar.getInstance().getGameHandler().getGame().getTeams()) {
            team.getTeamState().getScore().setScore(10);
        }
    }

    public ArrayList<NovsTeam> getWinningTeams() {
        ArrayList<NovsTeam> teams = new ArrayList<>();
        for (NovsTeam team : NovsWar.getInstance().getGameHandler().getGame().getTeams()) {
            teams.add(team);
        }
        return teams;
    }
}
