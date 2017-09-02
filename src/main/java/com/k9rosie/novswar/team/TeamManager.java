package com.k9rosie.novswar.team;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.config.TeamData;
import com.k9rosie.novswar.config.TeamsConfig;
import com.k9rosie.novswar.player.NovsPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.*;

public class TeamManager {

    private NovsWar novswar;
    private TreeMap<String, NovsTeam> teams;

    public TeamManager(NovsWar novswar) {
        this.novswar = novswar;
        teams = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    public void initialize() {
        teams.put("default", new NovsTeam("default", ChatColor.GRAY, false, false, false));
        loadTeams(); // load teams' data from config
    }

    public TreeMap<String, NovsTeam> getTeams() {
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
            ChatColor color = ChatColor.valueOf(data.getColor().toUpperCase());
            NovsTeam team = new NovsTeam(data.getName(), color,
                    data.isCanBeDamaged(),
                    data.isCanAttack(),
                    data.isFriendlyFire());
            teams.put(data.getName(), team);
        }
    }
}
