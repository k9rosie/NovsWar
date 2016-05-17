package com.k9rosie.novswar.game;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsWorld;
import org.bukkit.World;

public class GameHandler {

    private NovsWar novswar;
    private Game game;

    public GameHandler(NovsWar novswar) {
        this.novswar = novswar;
    }

    public void initialize() {
        String firstWorld = novswar.getConfigurationCache().getConfig("core").getStringList("core.world.enabled_worlds").get(0);
        World initialBukkitWorld = novswar.getPlugin().getServer().getWorld(firstWorld);
        NovsWorld initialWorld = novswar.getWorldManager().getWorlds().get(initialBukkitWorld);

        newGame(initialWorld);
        game.startGame();
    }

    public void newGame(NovsWorld world) {
        game = new Game(this, world);
    }

    public Game getGame() {
        return game;
    }

}
