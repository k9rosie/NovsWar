package com.k9rosie.novswar.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;

public class HelpCommand extends NovsCommand {

    public HelpCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
    }

    public void execute() {
        NovsPlayer player = getNovsWar().getPlayerManager().getPlayers().get((Player) getSender());
        player.getBukkitPlayer().sendMessage("/nw join: Join the round");
        player.getBukkitPlayer().sendMessage("/nw leave: Leave the round");
        player.getBukkitPlayer().sendMessage("/nw map: current map info");
        player.getBukkitPlayer().sendMessage("/nw player [name]: Player stats");
        player.getBukkitPlayer().sendMessage("/nw team [name]: Team info for players/teams");
        player.getBukkitPlayer().sendMessage("/nw vote: Prompts the voting screen");
        player.getBukkitPlayer().sendMessage("/nw spectate: Enter spectator mode");
    }
}
