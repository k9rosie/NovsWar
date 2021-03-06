package com.k9rosie.novswar.database;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.player.NovsStats;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class NovswarDB extends Database {

    public NovswarDB(NovsWar novsWar, String type, String prefix) {
        super(novsWar, type, prefix);
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

            column = new Column("death_messages");
            column.setType("TINYINT(1)");
            column.setDefaultValue("1");
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

            column = new Column("games_played");
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

            column = new Column("last_played");
            column.setType("TIMESTAMP");
            column.setDefaultValue("CURRENT_TIMESTAMP");
            stats.add(column);

            column = new Column("total_time");
            column.setType("BIGINT");
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

        ResultSet data = select("players", "uuid", bukkitPlayer.getUniqueId().toString());

        NovsStats playerStats = player.getStats();
        ResultSet stats = select("stats", "player_uuid", bukkitPlayer.getUniqueId().toString());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        playerStats.setLoggedIn(timestamp);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            while (data.next()) {
                player.setDeathMessages(data.getBoolean("death_messages"));
            }
            while (stats.next()) {
                playerStats.setKills(stats.getInt("kills"));
                playerStats.setArrowKills(stats.getInt("arrow_kills"));
                playerStats.setDeaths(stats.getInt("deaths"));
                playerStats.setArrowDeaths(stats.getInt("arrow_deaths"));
                playerStats.setSuicides(stats.getInt("suicides"));
                playerStats.setWins(stats.getInt("wins"));
                playerStats.setGamesPlayed(stats.getInt("games_played"));
                playerStats.setConnects(stats.getInt("connects"));
                playerStats.setDamageGiven(stats.getDouble("damage_given"));
                playerStats.setDamageTaken(stats.getDouble("damage_taken"));
                playerStats.setTotalTime(stats.getLong("total_time"));
                try {
                    playerStats.setLastPlayed(dateFormat.parse(stats.getString("last_played")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayerData(NovsPlayer player) {
        Player bukkitPlayer = player.getBukkitPlayer();
        String uuid = bukkitPlayer.getUniqueId().toString();
        String displayName = bukkitPlayer.getDisplayName();
        insert("players", new String[] {"uuid", "name"}, new String[] {uuid, displayName});
        insert("stats", new String[] {"player_uuid"}, new String[] {uuid});
    }

    public void flushPlayerData(NovsPlayer player) {
        String playerUUIDString = player.getBukkitPlayer().getUniqueId().toString();
        NovsStats stats = player.getStats();
        String deathMessages = player.canSeeDeathMessages() ? "1" : "0";
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = dateFormat.format(date);
        Timestamp loggedIn = stats.getLoggedIn();
        long totalTime = stats.getTotalTime() + loggedIn.getTime();
        set("players", "name", "'"+player.getBukkitPlayer().getDisplayName()+"'", "uuid", playerUUIDString);
        set("players", "death_messages", deathMessages, "uuid", playerUUIDString);
        set("stats", "kills", Integer.toString(stats.getKills()), "player_uuid", playerUUIDString);
        set("stats", "arrow_kills", Integer.toString(stats.getArrowKills()), "player_uuid", playerUUIDString);
        set("stats", "deaths", Integer.toString(stats.getDeaths()), "player_uuid", playerUUIDString);
        set("stats", "arrow_deaths", Integer.toString(stats.getArrowDeaths()), "player_uuid", playerUUIDString);
        set("stats", "suicides", Integer.toString(stats.getSuicides()), "player_uuid", playerUUIDString);
        set("stats", "wins", Integer.toString(stats.getWins()), "player_uuid", playerUUIDString);
        set("stats", "games_played", Integer.toString(stats.getGamesPlayed()), "player_uuid", playerUUIDString);
        set("stats", "connects", Integer.toString(stats.getConnects()), "player_uuid", playerUUIDString);
        set("stats", "damage_given", Double.toString(stats.getDamageGiven()), "player_uuid", playerUUIDString);
        set("stats", "damage_taken", Double.toString(stats.getDamageTaken()), "player_uuid", playerUUIDString);
        set("stats", "last_played", "'"+currentDate+"'", "player_uuid", playerUUIDString);
        set("stats", "total_time", Long.toString(totalTime), "player_uuid", playerUUIDString);
    }
}
