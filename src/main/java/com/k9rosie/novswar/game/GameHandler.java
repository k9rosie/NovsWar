package com.k9rosie.novswar.game;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.gamemode.Gamemode;
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
        NovsWorld initialWorld = novswar.getWorldManager().getWorldFromBukkitWorld(initialBukkitWorld);

        newGame(initialWorld);
        game.startGame();
    }

    public void newGame(NovsWorld world) {
        String gamemodeString = novswar.getConfigurationCache().getConfig("worlds").getString("worlds."+world.getBukkitWorld().getName()+".gamemode");
        Gamemode gamemode;
        if (gamemodeString.equalsIgnoreCase("none")) {
            gamemode = new Gamemode("None") {
                @Override
                public void onNewGame() {
                    novswar.log("No gamemode specified");
                }
            };
        } else {
           gamemode = novswar.getGamemodeHandler().getGamemodes().get(gamemodeString);
        }

        game = new Game(this, world, gamemode);
    }

    public Game getGame() {
        return game;
    }

}
