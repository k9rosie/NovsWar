package com.k9rosie.novswar.command.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.model.NovsPlayer;

public class HelpAdminCommand extends NovsCommand {
	
	public HelpAdminCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
    }

    public void execute() {
        NovsPlayer player = getNovsWar().getPlayerManager().getPlayers().get((Player) getSender());
        player.getBukkitPlayer().sendMessage("/nw admin kick <name>: Kicks player from current round");
        player.getBukkitPlayer().sendMessage("/nw admin setteam <player> <team>: Force player onto a team");
        player.getBukkitPlayer().sendMessage("/nw admin restart: Forces round to restart");
        player.getBukkitPlayer().sendMessage("/nw admin nextgame: Forces next map to start");
        player.getBukkitPlayer().sendMessage("/nw admin setregion <name> <battlefield | intermission_gate | death_region | objective>");
        player.getBukkitPlayer().sendMessage("/nw admin setspawn <team>");
    }
}
