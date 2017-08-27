package com.k9rosie.novswar.command.admin;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.command.NovsCommand;
import com.k9rosie.novswar.config.MessagesConfig;
import com.k9rosie.novswar.team.NovsTeam;
import com.k9rosie.novswar.world.NovsWorld;
import com.k9rosie.novswar.util.ChatUtil;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
//import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends NovsCommand {

    public SetSpawnCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
    }

    public void execute() {
        //nw admin setspawn red
        if (getArgs().length != 3) {
            ChatUtil.sendError((Player) getSender(), MessagesConfig.getInvalidParameters());
            return;
        } else {
            Player bukkitPlayer = (Player) getSender();
            World bukkitWorld = bukkitPlayer.getWorld();
            NovsWorld world; 
            String teamName = getArgs()[2];
            NovsTeam team = getNovsWar().getTeamManager().getTeam(teamName);
            
            // Check which world the sender is in
            if (bukkitWorld.equals(getNovsWar().getWorldManager().getLobbyWorld().getBukkitWorld())) {
            	//The player is in the lobby world
            	world = getNovsWar().getWorldManager().getLobbyWorld();
            	if (team == null || team.equals(getNovsWar().getTeamManager().getDefaultTeam()) == false) {
                    ChatUtil.sendError(bukkitPlayer, "You can only set the default team's spawn in Lobby World");
                    return;
            	}
            } else {
            	//The player is in a game world
            	world = getNovsWar().getWorldManager().getWorlds().get(bukkitWorld);
            	if (team == null || getNovsWar().getGameHandler().getGame().getTeams().contains(team) == false) {
                    ChatUtil.sendError(bukkitPlayer, "That team doesn't exist or is not enabled for this world.");
                    return;
                }
            }
            if (world == null) {
                ChatUtil.sendError(bukkitPlayer, "The world you're in isn't enabled in NovsWar.");
                return;
            }

            world.getTeamSpawns().put(team, bukkitPlayer.getLocation());
            String notice = MessagesConfig.getSpawnSet(team.getColor().toString(), team.getTeamName());
            ChatUtil.sendNotice(bukkitPlayer, notice);
        }
    }
}
