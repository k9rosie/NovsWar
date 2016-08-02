package com.k9rosie.novswar.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.model.NovsPlayer;

public class SpectateCommand extends NovsCommand{
	private Game game;

    public SpectateCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
        game = getNovsWar().getGameHandler().getGame();
    }

    public void execute() {
        NovsPlayer player = getNovsWar().getPlayerManager().getPlayers().get((Player) getSender());

        player.getBukkitPlayer().sendMessage("Game world is "+game.getWorld().getName());
    }
}
