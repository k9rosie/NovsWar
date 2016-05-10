package com.k9rosie.novswar.gamemode;

import com.k9rosie.novswar.game.Game;

public class TeamDeathmatch implements Gamemode {

    private String name;
    private Game game;

    public TeamDeathmatch() {
        name = "tdm";
    }

    public void initialize() {
        // TODO: set game timer
        // TODO: set game score
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getName() {
        return name;
    }
}
