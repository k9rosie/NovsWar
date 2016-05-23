package com.k9rosie.novswar.database;

import java.sql.SQLException;

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

        insert("players", new String[] {"uuid", "name", "stats_id"}, new String[] {"0", "k9rosie", "0"});
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
    }
}
