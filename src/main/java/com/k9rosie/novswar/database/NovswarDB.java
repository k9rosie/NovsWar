package com.k9rosie.novswar.database;

public class NovswarDB extends Database {
    private String prefix;

    public NovswarDB(DatabaseType type, String prefix) {
        super(type);
        this.prefix = prefix;
    }

    @Override
    public void initialize() {

    }

    public void spawnTables() {

    }
}
