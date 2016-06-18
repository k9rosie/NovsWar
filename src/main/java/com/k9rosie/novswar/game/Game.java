package com.k9rosie.novswar.game;

import com.k9rosie.novswar.gamemode.Gamemode;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.model.NovsWorld;

import java.util.HashMap;
import java.util.HashSet;

public class Game {
    private GameHandler gameHandler;
    private NovsWorld world;
    private Gamemode gamemode;
    private GameState gameState;
    private HashMap<NovsPlayer, NovsTeam> players;
    private HashMap<NovsTeam, Integer> score;

    public Game(GameHandler gameHandler, NovsWorld world, Gamemode gamemode) {
        this.gameHandler = gameHandler;
        this.world = world;
        this.gamemode = gamemode;
        players = new HashMap<NovsPlayer, NovsTeam>();
        score = new HashMap<NovsTeam, Integer>();
        gameState = GameState.PRE_GAME;
    }

    public void initialize() {
        gamemode.setGame(this);
    }

    public void startGame() {
        gamemode.onNewGame();
        // TODO: check if there are enough players to start
        // TODO: start timer
        // TODO: adjust game score according to gamemode
        // TODO: teleport all players to their team's designated spawn points
        // TODO: start schedulers for the world's regions
        // TODO: start game timer according to gamemode
    }

    public void pauseGame() {
        // TODO: teleport all players to their spawn points
        // TODO: stop timer
        // TODO: stop schedulers for the world's regions
    }

    public void endGame() {
        // TODO: stop timer
        // TODO: teleport players to spawn points
        // TODO: start voting if enabled
        // TODO: pick next world and request a new game from the gameHandler
    }

    public HashMap<NovsPlayer, NovsTeam> getGamePlayers() {
        return players;
    }

    public HashMap<NovsTeam, Integer> getGameScore() {
        return score;
    }
}
