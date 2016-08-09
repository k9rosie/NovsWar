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
        //regions = novsWar.getNovsConfigCache().getConfig("regions");
    }

    public void execute() {
        //nw admin setspawn red
        if (getArgs().length != 3) {
            getSender().sendMessage(Messages.INVALID_PARAMETERS.toString());
            return;
        } else {
            Player bukkitPlayer = (Player) getSender();
            World bukkitWorld = bukkitPlayer.getWorld();
            NovsWorld world; 
            //Get the NovsTeam
            String teamName = getArgs()[2];
            NovsTeam team;
            if(teamName.equalsIgnoreCase("default")) {
            	team = getNovsWar().getNovsTeamCache().getDefaultTeam();
            } else {
            	team = getNovsWar().getNovsTeamCache().getTeam(teamName);
            }
            
            //Check which world the sender is in
            if(bukkitWorld.equals(getNovsWar().getNovsWorldCache().getLobbyWorld().getBukkitWorld())) {
            	//The player is in the lobby world
            	world = getNovsWar().getNovsWorldCache().getLobbyWorld();
            	if(team == null || team.equals(getNovsWar().getNovsTeamCache().getDefaultTeam())==false) {
            		bukkitPlayer.sendMessage("You can only set the default team's spawn in Lobby World");
                    return;
            	}
            } else {
            	//The player is in a game world
            	world = getNovsWar().getNovsWorldCache().getWorlds().get(bukkitWorld);
            	if (team == null || getNovsWar().getGameHandler().getGame().getTeams().contains(team)==false) {
                    bukkitPlayer.sendMessage("That team doesn't exist or is not enabled for this world.");
                    return;
                }
            }
            if (world == null) {
                bukkitPlayer.sendMessage("The world you're in isn't enabled in NovsWar.");
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
