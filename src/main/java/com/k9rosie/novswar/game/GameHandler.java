package com.k9rosie.novswar.game;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.gamemode.DefaultGamemode;
import com.k9rosie.novswar.gamemode.Gamemode;
import com.k9rosie.novswar.world.NovsWorld;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scoreboard.ScoreboardManager;

public class GameHandler {

    private NovsWar novswar;
    private Game game;
    private ScoreboardManager scoreboardManager;
    private DefaultGamemode defaultGamemode;

    public GameHandler(NovsWar novswar) {
        this.novswar = novswar;
        scoreboardManager = Bukkit.getScoreboardManager();
        defaultGamemode = new DefaultGamemode();
    }

    public void initialize() {
        String firstWorld = novswar.getCoreConfig().getEnabledWorlds().get(0);
        World initialBukkitWorld = novswar.getPlugin().getServer().getWorld(firstWorld);
        NovsWorld initialWorld = novswar.getWorldManager().getWorlds().get(initialBukkitWorld);

        newGame(initialWorld);

    }

    public void newGame(NovsWorld world) {
        String gamemodeString = novswar.getWorldsConfig().getWorldData().get(world.getBukkitWorld().getName()).getGamemode();
        Gamemode gamemode = novswar.getGamemodes().get(gamemodeString);

        if (gamemode == null) {
            NovsWar.error(gamemodeString + " doesn't exist or hasn't been initialized. Default gamemode loaded.");
            gamemode = defaultGamemode;
        }

        game = new Game(this, world, gamemode);

        //Moved this event call to Game's initialize method
        //NovsWarNewGameEvent event = new NovsWarNewGameEvent(game);
        //Bukkit.getPluginManager().callEvent(event);

        //if (!event.isCancelled()) {
        game.initialize();
        //}

    }

    public Game getGame() {
        return game;
    }

    public NovsWar getNovsWarInstance() {
        return novswar;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public Gamemode getDefaultGamemode() {
        return defaultGamemode;
    }

}
