package com.k9rosie.novswar.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.util.ChatFormat;

public class MapCommand extends NovsCommand{
	private Game game;

    public MapCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
        game = getNovsWar().getGameHandler().getGame();
    }

    public void execute() {
        NovsPlayer player = getNovsWar().getNovsPlayerCache().getPlayers().get((Player) getSender());
        ChatFormat.sendNotice(player, "Map: "+game.getWorld().getName()+" playing "+game.getGamemode().getGamemodeName());
    }
}
