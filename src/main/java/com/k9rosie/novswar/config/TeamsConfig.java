package com.k9rosie.novswar.config;

import java.util.List;
import java.util.Map;

public class TeamsConfig extends NovsConfig {

    public TeamsConfig() {
        super("teams.yml");
    }

    private List<Map<?, ?>> teams;

    public List<Map<?, ?>> getTeams() {
        return teams;
    }

    public void reloadData() {
        teams = getConfig().getMapList("teams");
    }
}
