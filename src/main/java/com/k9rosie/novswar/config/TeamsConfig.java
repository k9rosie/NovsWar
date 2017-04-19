package com.k9rosie.novswar.config;

import com.k9rosie.novswar.NovsWarPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamsConfig extends NovsConfig {
    private List<Map<?, ?>> teamList;
    private ArrayList<TeamData> teamData;

    public TeamsConfig(NovsWarPlugin plugin) {
        super(plugin, "teams.yml");
    }

    public List<Map<?, ?>> getTeams() {
        return teamList;
    }

    public ArrayList<TeamData> getTeamData() {
        return teamData;
    }

    public void reloadData() {
        teamList.clear();
        teamList = getConfig().getMapList("teams");
        parseTeamList();
    }

    public void parseTeamList() {
        for (Map<?, ?> map : teamList) {
            HashMap<String, Object> data = (HashMap<String, Object>) map;

            String name = (String) data.get("name");
            String color = (String) data.get("color");
            boolean canBeDamaged = (Boolean) data.get("can_be_damaged");
            boolean canAttack = (Boolean) data.get("can_attack");
            boolean friendlyFire = (Boolean) data.get("friendly_fire");

            teamData.add(new TeamData(name, color, canBeDamaged, canAttack, friendlyFire));
        }
    }
}
