package com.k9rosie.novswar.game;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.event.NovsWarEndGameEvent;
import com.k9rosie.novswar.event.NovsWarNewGameEvent;
import com.k9rosie.novswar.gamemode.Gamemode;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scoreboard.ScoreboardManager;

public class GameHandler {

    private class EmptyGamemode implements Gamemode {
        int gameTime = 60;
        String gamemodeName = "none";

        public int getGameTime() {
            return gameTime;
        }

        public String getGamemodeName() {
            return gamemodeName;
        }

        public int getDeathTime() {
            return 5;
        }

        public void hook() {
            novswar.getGamemodeHandler().getGamemodes().put(gamemodeName, this);
        }

    }

    private NovsWar novswar;
    private Game game;
    private ScoreboardManager scoreboardManager;

    public GameHandler(NovsWar novswar) {
        this.novswar = novswar;
        scoreboardManager = Bukkit.getScoreboardManager();
    }

    public void initialize() {
        String firstWorld = novswar.getConfigurationCache().getConfig("core").getStringList("core.world.enabled_worlds").get(0);
        World initialBukkitWorld = novswar.getPlugin().getServer().getWorld(firstWorld);
        NovsWorld initialWorld = novswar.getWorldManager().getWorld(initialBukkitWorld);

        newGame(initialWorld);

    }

    public void newGame(NovsWorld world) {
        String gamemodeString = novswar.getConfigurationCache().getConfig("worlds").getString("worlds."+world.getBukkitWorld().getName()+".gamemode");
        Gamemode gamemode;
        if (gamemodeString.equalsIgnoreCase("none")) {
            gamemode = new EmptyGamemode();
        } else {
           gamemode = novswar.getGamemodeHandler().getGamemodes().get(gamemodeString);
        }

        game = new Game(this, world, gamemode);
        NovsWarNewGameEvent event = new NovsWarNewGameEvent(game);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            game.initialize();
        }

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
}
