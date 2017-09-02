package com.k9rosie.novswar.config;

import com.k9rosie.novswar.NovsWarPlugin;
import com.k9rosie.novswar.team.NovsTeam;
import com.k9rosie.novswar.world.NovsCuboid;
import com.k9rosie.novswar.world.NovsWorld;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;

import javax.security.auth.login.Configuration;
import java.lang.reflect.Array;
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
        ArrayList<HashMap<String, Object>> regions = new ArrayList<>();
        getConfig().set("regions", null);

        for (NovsWorld world : getNovsWar().getWorldManager().getWorlds().values()) {
            HashMap<String, Object> obj = new HashMap<>();
            obj.put("world", world.getName());

            if (!world.getTeamSpawns().isEmpty()) {
                ArrayList<HashMap<String, Object>> spawns = new ArrayList<>();
                for (Map.Entry<NovsTeam, Location> entry : world.getTeamSpawns().entrySet()) {
                    HashMap<String, Object> data = new HashMap<>();
                    NovsTeam team = entry.getKey();
                    Location loc = entry.getValue();

                    data.put("team", team.getTeamName());
                    data.put("x", loc.getX());
                    data.put("y", loc.getY());
                    data.put("z", loc.getZ());
                    data.put("pitch", loc.getPitch());
                    data.put("yaw", loc.getYaw());
                    spawns.add(data);
                }
                obj.put("spawns", spawns);
            }

            if (!world.getCuboids().isEmpty()) {
                ArrayList<HashMap<String, Object>> cuboids = new ArrayList<>();
                for (Map.Entry<String, NovsCuboid> entry : world.getCuboids().entrySet()) {
                    HashMap<String, Object> data = new HashMap<>();
                    String cuboidName = entry.getKey();
                    NovsCuboid cuboid = entry.getValue();

                    data.put("name", cuboidName);
                    data.put("type", cuboid.getCuboidType().toString());
                    data.put("corner_one_x", cuboid.getCornerOne().getX());
                    data.put("corner_one_y", cuboid.getCornerOne().getY());
                    data.put("corner_one_z", cuboid.getCornerOne().getZ());
                    data.put("corner_two_x", cuboid.getCornerTwo().getX());
                    data.put("corner_two_y", cuboid.getCornerTwo().getY());
                    data.put("corner_two_z", cuboid.getCornerTwo().getZ());
                    cuboids.add(data);
                }
                obj.put("cuboids", cuboids);
            }

            if (!world.getSigns().isEmpty()) {
                ArrayList<HashMap<String, Object>> signs = new ArrayList<>();
                for (Location loc : world.getSigns().keySet()) {
                    HashMap<String, Object> data = new HashMap<>();

                    data.put("x", loc.getX());
                    data.put("y", loc.getY());
                    data.put("z", loc.getZ());
                    signs.add(data);
                }
                obj.put("signs", signs);
            }

            regions.add(obj);
        }

        getConfig().set("regions", regions);
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
            if (spawns != null) {
                for (Object obj : spawns) {
                    HashMap<String, Object> d = (HashMap<String, Object>) obj;

                    String team = (String) d.get("team");
                    Double x = new Double(d.get("x").toString());
                    Double y = new Double(d.get("y").toString());
                    Double z = new Double(d.get("z").toString());
                    Double pitch = new Double(d.get("pitch").toString());
                    Double yaw = new Double(d.get("yaw").toString());

                    spawnData.add(new SpawnData(team, x, y, z, pitch.floatValue(), yaw.floatValue()));
                }
            }

            // iterate through cuboids and populate cuboidData with data
            if (cuboids != null) {
                for (Object obj : cuboids) {
                    HashMap<String, Object> d = (HashMap<String, Object>) obj;

                    String name = (String) d.get("name");
                    String type = (String) d.get("type");
                    Double cornerOneX = new Double(d.get("corner_one_x").toString());
                    Double cornerOneY = new Double(d.get("corner_one_y").toString());
                    Double cornerOneZ = new Double(d.get("corner_one_z").toString());
                    Double cornerTwoX = new Double(d.get("corner_two_x").toString());
                    Double cornerTwoY = new Double(d.get("corner_two_y").toString());
                    Double cornerTwoZ = new Double(d.get("corner_two_z").toString());

                    cuboidData.add(new CuboidData(name, type, cornerOneX, cornerOneY, cornerOneZ, cornerTwoX, cornerTwoY, cornerTwoZ));
                }
            }

            // iterate through signs and populate signData with data
            if (signs != signs) {
                for (Object obj : signs) {
                    HashMap<String, Object> d = (HashMap<String, Object>) obj;

                    Double x = new Double(d.get("x").toString());
                    Double y = new Double(d.get("y").toString());
                    Double z = new Double(d.get("z").toString());

                    signData.add(new SignData(x, y, z));
                }
            }

            regionData.put(world, new RegionData(world, spawnData, cuboidData, signData));
        }
    }
}
