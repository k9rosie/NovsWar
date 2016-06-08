package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommand extends NovsCommand {

    public TeamCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
    }

    public void execute() {
        NovsPlayer player = getNovsWar().getPlayerManager().getPlayerFromBukkitPlayer((Player) getSender());
        NovsTeam team = player.getTeam();

        getSender().sendMessage(team.getColor() + team.getTeamName());
    }
}
