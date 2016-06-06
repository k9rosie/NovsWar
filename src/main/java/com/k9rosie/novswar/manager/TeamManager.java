package com.k9rosie.novswar.manager;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.util.ColorParser;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class TeamManager {

    private NovsWar novswar;

    private ArrayList<NovsTeam> teams;
    private NovsTeam defaultTeam;

    public TeamManager(NovsWar novswar) {
        this.novswar = novswar;
        teams = new ArrayList<NovsTeam>();
    }

    public void initialize() {
        loadTeams(); // load teams' data from config
        defaultTeam = new NovsTeam("No Team", ChatColor.GRAY, false, false);
    }

    public ArrayList<NovsTeam> getTeams() {
        return teams;
    }

    public NovsTeam getDefaultTeam() {
        return defaultTeam;
    }

    public void loadTeams() {
        FileConfiguration teamsConfig = novswar.getConfigurationCache().getConfig("teams");
        Set<String> teamNames = teamsConfig.getConfigurationSection("teams").getKeys(false);

        for(String teamName : teamNames) {
            ChatColor color = ColorParser.parseString(teamsConfig.getString("teams."+teamName+".color"));
            boolean canBeDamaged = teamsConfig.getBoolean("teams."+teamName+".can_be_damaged");
            boolean canAttack = teamsConfig.getBoolean("teams."+teamName+".can_attack");

            NovsTeam team = new NovsTeam(teamName, color, canBeDamaged, canAttack);
            teams.add(team);
        }
    }

    public void setPlayerTeam(NovsPlayer player, NovsTeam team) {
        if (player.getTeam() != null) {
            player.getTeam().getPlayers().remove(player);
        }
        player.setTeam(team);
        team.getPlayers().add(player);
    }

}
