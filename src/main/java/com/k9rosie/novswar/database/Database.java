package com.k9rosie.novswar.database;

public class Database {
    private String prefix;
    private DatabaseType type;
    private String path;

    private DatabaseConnection databaseConnection;

    public Database(String prefix, DatabaseType type, String path) {
        this.prefix = prefix;
        this.type = type;
        this.path = path;

        databaseConnection = new DatabaseConnection(this);
    }

    public void initialize() {
        try {
            databaseConnection.connect();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public DatabaseType getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }
}
