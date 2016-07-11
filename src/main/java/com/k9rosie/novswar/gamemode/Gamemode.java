package com.k9rosie.novswar.gamemode;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.game.TeamData;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;

import java.sql.Time;

public abstract class Gamemode {
    private final String name;
    private Game game;
    private int gameTime;
    private NovsWar novsWarInstance;

    public Gamemode(String name) {
        this.name = name;
        novsWarInstance = NovsWar.getInstance();
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    public NovsWar getNovsWarInstance() {
        return novsWarInstance;
    }

    /*
        hook() should be called when the Gamemode plugin is enabled
    */
    public void hook() {
        novsWarInstance.getGamemodeHandler().getGamemodes().put(name, this);
    }

    public void onPlayerDeath(NovsPlayer player) {
        NovsTeam playerTeam = game.getPlayerTeam(player);
        TeamData teamData = game.getTeamData().get(playerTeam);
        teamData.decrementScore();
    }

    public void onPlayerKill(NovsPlayer killer, NovsPlayer killed, boolean arrowKill) {
        NovsTeam killerTeam = game.getPlayerTeam(killer);
        NovsTeam killedTeam = game.getPlayerTeam(killed);
        TeamData killerTeamData = game.getTeamData().get(killerTeam);
        TeamData killedTeamData = game.getTeamData().get(killedTeam);
        if (arrowKill) {
            killerTeamData.incrementScore(2);
            killedTeamData.incrementScore(2);
        } else {
            killerTeamData.incrementScore();
            killedTeamData.decrementScore();
        }
    }

    /*
        onNewGame() is called at the beginning of every game
    */
    public abstract void onNewGame();

    public abstract void onEndGame();
}
