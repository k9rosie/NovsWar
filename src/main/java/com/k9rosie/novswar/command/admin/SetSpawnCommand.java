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

public class SetSpawnCommand implements NovsCommand {
    private String permissions;
    private String description;
    private int requiredNumofArgs;
    private boolean playerOnly;
    private NovsWar novsWar;

    public SetSpawnCommand(NovsWar novsWar) {
        permissions = "novswar.command.admin.setspawn";
        description = "Sets a spawn point for a specified team";
        requiredNumofArgs = 1;
        playerOnly = true;
        this.novsWar = novsWar;
    }

    public void execute(CommandSender sender, String[] args) {
        Player bukkitPlayer = (Player) sender;
        World bukkitWorld = bukkitPlayer.getWorld();
        NovsWorld world;
        String teamName = args[2];
        NovsTeam team = novsWar.getTeamManager().getTeam(teamName);

        // Check which world the sender is in
        if (bukkitWorld.equals(novsWar.getWorldManager().getLobbyWorld().getBukkitWorld())) {
        	// The player is in the lobby world
        	world = novsWar.getWorldManager().getLobbyWorld();
        	if (team == null || team.equals(novsWar.getTeamManager().getDefaultTeam()) == false) {
                ChatUtil.sendError(bukkitPlayer, "You can only set the default team's spawn in Lobby World");
                return;
        	}
        } else {
        	// The player is in a game world
        	world = novsWar.getWorldManager().getWorlds().get(bukkitWorld);
        	if (team == null || novsWar.getGameHandler().getGame().getTeams().contains(team) == false) {
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

    @Override
    public String getPermissions() {
        return permissions;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getRequiredNumofArgs() {
        return requiredNumofArgs;
    }

    @Override
    public boolean isPlayerOnly() {
        return playerOnly;
    }
}
