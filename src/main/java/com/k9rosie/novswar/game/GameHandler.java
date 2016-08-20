package com.k9rosie.novswar.game;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.gamemode.Gamemode;
import com.k9rosie.novswar.model.NovsWorld;
import com.k9rosie.novswar.util.ChatUtil;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.scoreboard.ScoreboardManager;

public class GameHandler {

    private NovsWar novswar;
    private Game game;
    private ScoreboardManager scoreboardManager;

    public GameHandler(NovsWar novswar) {
        this.novswar = novswar;
        scoreboardManager = Bukkit.getScoreboardManager();
    }

    public void initialize() {
        String firstWorld = novswar.getNovsConfigCache().getConfig("core").getStringList("core.world.enabled_worlds").get(0);
        World initialBukkitWorld = novswar.getPlugin().getServer().getWorld(firstWorld);
        NovsWorld initialWorld = novswar.getNovsWorldCache().getWorlds().get(initialBukkitWorld);

        newGame(initialWorld);

    }

    public void newGame(NovsWorld world) {
        String gamemodeString = novswar.getNovsConfigCache().getConfig("worlds").getString("worlds."+world.getBukkitWorld().getName()+".gamemode");
        Gamemode gamemode = novswar.getGamemodes().get(gamemodeString);

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

    public void updateInfoSigns() {
    	ChatUtil.printDebug("Updating info signs...");
        for (Sign sign : novswar.getNovsWorldCache().getActiveSigns()) {
        	ChatUtil.printDebug(sign.getLocation().toString());
            sign.setLine(1, game.getWorld().getName());
            sign.setLine(2, game.getGamemode().getGamemodeName());
            sign.setLine(3, novswar.getNovsPlayerCache().getGamePlayers().size() + " players");
            sign.update();
        }
    }
}
