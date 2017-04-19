package com.k9rosie.novswar.team;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.config.Messages;
import com.k9rosie.novswar.config.TeamData;
import com.k9rosie.novswar.config.TeamsConfig;
import com.k9rosie.novswar.event.NovsWarJoinTeamEvent;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.team.NovsTeam;
import com.k9rosie.novswar.util.ChatUtil;
import com.k9rosie.novswar.util.ColorParser;
import com.k9rosie.novswar.util.SendTitle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class TeamManager {

    private NovsWar novswar;
    private HashMap<String, NovsTeam> teams;
    private int messageTime;
    private int messageTask;

    public TeamManager(NovsWar novswar) {
        this.novswar = novswar;
        teams = new HashMap<>();
        messageTime = 0;
        messageTask = 0;
    }

    public void initialize() {
        teams.put("default", new NovsTeam("default", ChatColor.GRAY, false, false, false));
        loadTeams(); // load teams' data from config
    }

    public HashMap<String, NovsTeam> getTeams() {
        return teams;
    }

    public NovsTeam getTeam(String teamName) {
        return teams.get(teamName);
    }

    public NovsTeam getDefaultTeam() {
        return teams.get("default");
    }

    public void clearTeamStates() {
        for (NovsTeam team : teams.values()) {
            team.setTeamState(null);
        }
    }

    public void loadTeams() {
        TeamsConfig teamsConfig = novswar.getConfigManager().getTeamConfig();
        for (TeamData data : teamsConfig.getTeamData()) {
            ChatColor color = ChatColor.valueOf(data.getColor());
            NovsTeam team = new NovsTeam(data.getName(), color,
                    data.isCanBeDamaged(),
                    data.isCanAttack(),
                    data.isFriendlyFire());
            teams.put(data.getName(), team);
        }
    }
    
    public void assignTeam(NovsPlayer player) {
    	ArrayList<NovsTeam> enabledTeams = novswar.getGameHandler().getGame().getTeams();

    	// novsloadout has its own way of sorting players, only run this code if it isnt enabled
        if (!Bukkit.getPluginManager().isPluginEnabled("NovsLoadout")) {

        	//Determine which team has the fewest players and put them on there
        	NovsTeam smallestTeam = enabledTeams.get(0);
        	int smallest = smallestTeam.getTeamState().getPlayers().size();
            for (NovsTeam team : enabledTeams) {
            	if(team.getTeamState().getPlayers().size() <= smallest) {
            		smallest = team.getTeamState().getPlayers().size();
            		smallestTeam = team;
            	}
            }
            smallestTeam.getTeamState().addPlayer(player);
        }
    }
}
