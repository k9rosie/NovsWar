package com.k9rosie.novswar.game;

import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.model.NovsWorld;
import com.k9rosie.novswar.util.Gamemode;

import java.util.HashMap;
import java.util.HashSet;

public class Game {

    private GameHandler gameHandler;
    private NovsWorld world;
    private Gamemode gamemode;
    private HashMap<NovsTeam, HashSet<NovsPlayer>> activeTeams;

    public Game(GameHandler gameHandler, NovsWorld world) {
        this.gameHandler = gameHandler;
        this.world = world;
        gamemode = world.getGamemode();
        activeTeams = new HashMap<NovsTeam, HashSet<NovsPlayer>>();
    }

    public void startGame() {
        // TODO: check if there are enough players to start
        // TODO: start timer
        // TODO: adjust game score according to gamemode
        // TODO: teleport all players to their team's designated spawn points
        // TODO: start schedulers for the world's regions
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




}
