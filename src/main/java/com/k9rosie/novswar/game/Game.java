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

    public void startGame() {}

    public void pauseGame() {}

    public void endGame() {}




}
