package com.k9rosie.novswar.command.admin;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.model.NovsWorld;
import com.k9rosie.novswar.util.Messages;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
//import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends NovsCommand {
    //private FileConfiguration regions;

    public SetSpawnCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
        //regions = novsWar.getConfigurationCache().getConfig("regions");
    }

    public void execute() {
        //nw admin setspawn red
        if (getArgs().length != 3) {
            getSender().sendMessage(Messages.INVALID_PARAMETERS.toString());
            return;
        } else {
            Player bukkitPlayer = (Player) getSender();
            World bukkitWorld = bukkitPlayer.getWorld();
            NovsWorld world = getNovsWar().getWorldManager().getWorlds().get(bukkitWorld);

            if (world == null) {
                bukkitPlayer.sendMessage("The world you're in isn't enabled in NovsWar.");
                return;
            }

            String teamName = getArgs()[2];
            NovsTeam team = getNovsWar().getTeamManager().getTeam(teamName);

            if (team == null || getNovsWar().getGameHandler().getGame().getTeams().contains(team)==false) {
                bukkitPlayer.sendMessage("That team doesn't exist or is not enabled for this world.");
                return;
            }

            Location location = bukkitPlayer.getLocation();
            double x = location.getX();
            double y = location.getY();
            double z = location.getZ();
            float pitch = location.getPitch();
            float yaw = location.getYaw();
            world.getTeamSpawns().put(team, new Location(location.getWorld(), x, y, z, pitch, yaw));

            bukkitPlayer.sendMessage("Spawn set for team " + team.getColor()+team.getTeamName());

        }
    }
}
