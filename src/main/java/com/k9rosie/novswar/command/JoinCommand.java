package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.model.NovsPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand extends NovsCommand {

    private Game game;

    public JoinCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
        game = getNovsWar().getGameHandler().getGame();
    }

    public void execute() {
        NovsPlayer player = getNovsWar().getPlayerManager().getNovsPlayer((Player) getSender());

        game.joinGame(player);
    }
}
