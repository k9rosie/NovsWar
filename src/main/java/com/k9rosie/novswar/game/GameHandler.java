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
        World initialBukkitWorld = novswar.getPlugin().getServer().getWorld(novswar.getConfigurationCache().getConfig("core").getStringList("core.world.enabled_worlds").get(0));
        NovsWorld initialWorld = novswar.getWorldManager().getNovsWorld(initialBukkitWorld);

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
