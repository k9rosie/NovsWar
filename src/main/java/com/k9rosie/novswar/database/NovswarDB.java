package com.k9rosie.novswar.database;

public class NovswarDB extends Database {
    private String prefix;

    public NovswarDB(DatabaseType type, String prefix) {
        super(type);
        this.prefix = prefix;
    }

    @Override
    public void initialize() {
        spawnTables();
    }

    public void spawnTables() {
        Column column;

        Table players = new Table("Players", prefix, this);
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
