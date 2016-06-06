package com.k9rosie.novswar.database;

import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsStats;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class NovswarDB extends Database {

    public NovswarDB(DatabaseType type, String prefix) {
        super(type, prefix);
    }

    @Override
    public void initialize() {
        try {
            getDatabaseConnection().connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        spawnTables();
        String uuid = UUID.randomUUID().toString();
    }

    public void spawnTables() {
        Column column;

        Table players = new Table("players", this);
        {
            column = new Column("id");
            column.setType("INTEGER");
            column.setPrimary(true);
            column.setAutoIncrement(true);
            players.add(column);

            column = new Column("uuid");
            column.setType("CHAR(36)");
            players.add(column);

            column = new Column("name");
            column.setType("VARCHAR(255)");
            players.add(column);
        }
        players.execute();

        Table stats = new Table("stats", this);
        {
            column = new Column("id");
            column.setType("INTEGER");
            column.setPrimary(true);
            column.setAutoIncrement(true);
            stats.add(column);

            column = new Column("player_uuid");
            column.setType("CHAR(36)");
            stats.add(column);

            column = new Column("kills");
            column.setType("INTEGER");
            column.setDefaultValue("0");
            stats.add(column);

            column = new Column("arrow_kills");
            column.setType("INTEGER");
            column.setDefaultValue("0");
            stats.add(column);

            column = new Column("deaths");
            column.setType("INTEGER");
            column.setDefaultValue("0");
            stats.add(column);

            column = new Column("arrow_deaths");
            column.setType("INTEGER");
            column.setDefaultValue("0");
            stats.add(column);

            column = new Column("suicides");
            column.setType("INTEGER");
            column.setDefaultValue("0");
            stats.add(column);

            column = new Column("wins");
            column.setType("INTEGER");
            column.setDefaultValue("0");
            stats.add(column);

            column = new Column("losses");
            column.setType("INTEGER");
            column.setDefaultValue("0");
            stats.add(column);

            column = new Column("connects");
            column.setType("INTEGER");
            column.setDefaultValue("0");
            stats.add(column);

            column = new Column("damage_given");
            column.setType("DOUBLE");
            column.setDefaultValue("0");
            stats.add(column);

            column = new Column("damage_taken");
            column.setType("DOUBLE");
            column.setDefaultValue("0");
            stats.add(column);
        }
        stats.execute();
    }

    public void fetchPlayerData(NovsPlayer player) {
        Player bukkitPlayer = player.getBukkitPlayer();

        if (!exists("players", "uuid", bukkitPlayer.getUniqueId().toString())) {
            createPlayerData(player);
        }

        NovsStats playerStats = player.getStats();
        ResultSet stats = select("stats", "player_uuid", bukkitPlayer.getUniqueId().toString());

        try {
            while (stats.next()) {
                playerStats.setKills(stats.getInt("kills"));
                playerStats.setArrowKills(stats.getInt("arrow_kills"));
                playerStats.setDeaths(stats.getInt("deaths"));
                playerStats.setArrowDeaths(stats.getInt("arrow_deaths"));
                playerStats.setSuicides(stats.getInt("suicides"));
                playerStats.setWins(stats.getInt("wins"));
                playerStats.setLosses(stats.getInt("losses"));
                playerStats.setConnects(stats.getInt("connects"));
                playerStats.setDamageGiven(stats.getDouble("damage_given"));
                playerStats.setDamageTaken(stats.getDouble("damage_taken"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayerData(NovsPlayer player) {
        Player bukkitPlayer = player.getBukkitPlayer();
        insert("players", new String[] {"uuid", "name"}, new String[] {bukkitPlayer.getUniqueId().toString(), bukkitPlayer.getDisplayName()});
        insert("stats", new String[] {"player_uuid"}, new String[] {bukkitPlayer.getUniqueId().toString()});
    }

    public void flushPlayerData(NovsPlayer player) {
        String playerUUIDString = player.getBukkitPlayer().getUniqueId().toString();
        NovsStats stats = player.getStats();
        set("stats", "kills", Integer.toString(stats.getKills()), "player_uuid", playerUUIDString);
        set("stats", "arrow_kills", Integer.toString(stats.getArrowKills()), "player_uuid", playerUUIDString);
        set("stats", "deaths", Integer.toString(stats.getDeaths()), "player_uuid", playerUUIDString);
        set("stats", "arrow_deaths", Integer.toString(stats.getArrowDeaths()), "player_uuid", playerUUIDString);
        set("stats", "suicides", Integer.toString(stats.getSuicides()), "player_uuid", playerUUIDString);
        set("stats", "wins", Integer.toString(stats.getWins()), "player_uuid", playerUUIDString);
        set("stats", "losses", Integer.toString(stats.getLosses()), "player_uuid", playerUUIDString);
        set("stats", "connects", Integer.toString(stats.getConnects()), "player_uuid", playerUUIDString);
        set("stats", "damage_given", Double.toString(stats.getDamageGiven()), "player_uuid", playerUUIDString);
        set("stats", "damage_taken", Double.toString(stats.getDamageTaken()), "player_uuid", playerUUIDString);
    }
}
