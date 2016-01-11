package com.k9rosie.novswar.game;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsWorld;

public class GameHandler {

    private NovsWar novswar;
    private Game game;

    public GameHandler(NovsWar novswar) {
        this.novswar = novswar;
    }

    public void initialize() {
    }

    public void newGame(NovsWorld world) {
        game = new Game(this, world);
    }

    public Game getGame() {
        return game;
    }

}
