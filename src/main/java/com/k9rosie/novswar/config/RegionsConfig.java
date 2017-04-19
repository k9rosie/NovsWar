package com.k9rosie.novswar.config;

import com.k9rosie.novswar.NovsWarPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionsConfig extends NovsConfig {

    private List<Map<?, ?>> regionList;
    private HashMap<String, RegionData> regionData;

    public RegionsConfig(NovsWarPlugin plugin) {
        super(plugin, "regions.yml");
        regionData = new HashMap<>();
    }

    public List<Map<?, ?>> getRegionList() {
        return regionList;
    }

    public HashMap<String, RegionData> getRegionData() {
        return regionData;
    }

    public void reloadData() {
        regionData.clear();
        regionList = getConfig().getMapList("regions");
        parseRegionList();
    }

    public void populate() {
        getConfig().set("regions", null);

        getConfig().createSection("regions", regionData);
    }

    public void parseRegionList() {
        for (Map<?, ?> map : regionList) {
            HashMap<String, Object> data = (HashMap<String, Object>) map;

            String world = (String) data.get("world");

            ArrayList<Object> spawns = (ArrayList<Object>) data.get("spawns");
            ArrayList<Object> cuboids = (ArrayList<Object>) data.get("cuboids");
            ArrayList<Object> signs = (ArrayList<Object>) data.get("signs");

            ArrayList<SpawnData> spawnData = new ArrayList<>();
            ArrayList<CuboidData> cuboidData = new ArrayList<>();
            ArrayList<SignData> signData = new ArrayList<>();

            // iterate through spawns and populate spawnData with data
            for (Object obj : spawns) {
                HashMap<String, Object> d = (HashMap<String, Object>) obj;

                String team = (String) d.get("team");
                Double x = (Double) d.get("x");
                Double y = (Double) d.get("y");
                Double z = (Double) d.get("z");
                Float pitch = (Float) d.get("pitch");
                Float yaw = (Float) d.get("yaw");

                spawnData.add(new SpawnData(team, x, y, z, pitch, yaw));
            }

            // iterate through cuboids and populate cuboidData with data
            for (Object obj : cuboids) {
                HashMap<String, Object> d = (HashMap<String, Object>) obj;

                String name = (String) d.get("name");
                String type = (String) d.get("type");
                Double cornerOneX = (Double) d.get("double_one_x");
                Double cornerOneY = (Double) d.get("double_one_y");
                Double cornerOneZ = (Double) d.get("double_one_z");
                Double cornerTwoX = (Double) d.get("double_two_x");
                Double cornerTwoY = (Double) d.get("double_two_y");
                Double cornerTwoZ = (Double) d.get("double_two_z");

                cuboidData.add(new CuboidData(name, type, cornerOneX, cornerOneY, cornerOneZ, cornerTwoX, cornerTwoY, cornerTwoZ));
            }

            // iterate through signs and populate signData with data
            for (Object obj : signs) {
                HashMap<String, Object> d = (HashMap<String, Object>) obj;

                Double x = (Double) d.get("x");
                Double y = (Double) d.get("y");
                Double z = (Double) d.get("z");

                signData.add(new SignData(x, y, z));
            }

            regionData.put(world, new RegionData(world, spawnData, cuboidData, signData));
        }
    }
}
