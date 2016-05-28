package com.k9rosie.novswar.database;

import com.k9rosie.novswar.model.NovsPlayer;
import org.bukkit.entity.Player;

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
        insert("players", new String[] {"uuid", "name", "stats_id"}, new String[] {uuid, "k9rosie", "0"});
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

            column = new Column("stats_id");
            column.setType("INTEGER");
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

            column = new Column("player_id");
            column.setType("INTEGER");
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

            column = new Column("damange_taken");
            column.setType("DOUBLE");
            column.setDefaultValue("0");
            stats.add(column);
        }
    }

    public void createPlayerData(Player player) {

    }
}
