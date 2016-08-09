package com.k9rosie.novswar.cache;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.game.Game;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.util.ColorParser;
import com.k9rosie.novswar.util.SendTitle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class NovsTeamCache {

    private NovsWar novswar;
    private ArrayList<NovsTeam> teams;
    private NovsTeam defaultTeam;
    private int messageTime;
    private int messageTask;

    public NovsTeamCache(NovsWar novswar) {
        this.novswar = novswar;
        teams = new ArrayList<NovsTeam>();
        messageTime = 0;
        messageTask = 0;
    }

    public void initialize() {
        teams.add(new NovsTeam("default", ChatColor.GRAY, false, false, false));
        loadTeams(); // load teams' data from config
    }

    public ArrayList<NovsTeam> getTeams() {
        return teams;
    }

    public NovsTeam getDefaultTeam() {
        return teams.get(0);
    }

    public NovsTeam getTeam(String teamName) {
        for (NovsTeam team : teams) {
            if (teamName.equalsIgnoreCase(team.getTeamName())) {
                return team;
            }
        }
        return null;
    }

    public void loadTeams() {
        FileConfiguration teamsConfig = novswar.getNovsConfigCache().getConfig("teams");
        List<Map<?, ?>> teamInfoMaps = teamsConfig.getMapList("teams");


        for(Map<?, ?> teamInfoMap : teamInfoMaps) {
            HashMap<String, Object> teamInfo = (HashMap<String, Object>) teamInfoMap;
            String teamName = (String) teamInfo.get("name");
            ChatColor color = ColorParser.parseString((String) teamInfo.get("color"));
            boolean canBeDamaged = (boolean) teamInfo.get("can_be_damaged");
            boolean canAttack = (boolean) teamInfo.get("can_attack");
            boolean friendlyFire = (boolean) teamInfo.get("can_attack");
            NovsTeam team = new NovsTeam(teamName, color, canBeDamaged, canAttack, friendlyFire);
            teams.add(team);
        }
    }
}
