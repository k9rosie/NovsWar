package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommand implements ICommand {

    public NovsWar novsWar;
    public CommandSender sender;

    public TeamCommand(NovsWar novsWar, CommandSender sender) {
        this.novsWar = novsWar;
        this.sender = sender;
    }

    public void execute() {
        NovsPlayer player = novsWar.getPlayerManager().getPlayerFromBukkitPlayer((Player) sender);
        NovsTeam team = player.getTeam();

        sender.sendMessage(team.getColor() + team.getTeamName());
    }
}
