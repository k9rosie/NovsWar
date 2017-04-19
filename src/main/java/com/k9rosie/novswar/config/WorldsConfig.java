package com.k9rosie.novswar.config;

import com.k9rosie.novswar.NovsWarPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldsConfig extends NovsConfig {
    private List<Map<?, ?>> worldList;
    private HashMap<String, WorldData> worldData;

    public WorldsConfig(NovsWarPlugin plugin) {
        super(plugin, "worlds.yml");
        worldData = new HashMap<>();
    }

    public List<Map<?, ?>> getWorldList() {
        return worldList;
    }

    public HashMap<String, WorldData> getWorldData() {
        return worldData;
    }

    public ArrayList<WorldData> getWorlds() {
        return (ArrayList<WorldData>) worldData.values();
    }

    public void reloadData() {
        worldData.clear();
        worldList = getConfig().getMapList("worlds");
        parseWorldList();
    }

    public void parseWorldList() {
        for (Map<?, ?> map : worldList) {
            HashMap<String, Object> data = (HashMap<String, Object>) map;

            String world = (String) data.get("world");
            String name = (String) data.get("name");
            String gamemode = (String) data.get("gamemode");
            String[] enabledTeams = (String[]) data.get("enabled_teams");

            worldData.put(world, new WorldData(world, name, gamemode, enabledTeams));
        }
    }
}
