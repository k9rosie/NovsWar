package com.k9rosie.novswar.database;

public abstract class Database {
    private DatabaseType type;

    private DatabaseConnection databaseConnection;

    public Database(DatabaseType type) {
        this.type = type;

        databaseConnection = new DatabaseConnection(this);
    }

    public abstract void initialize();

    public DatabaseType getType() {
        return type;
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }
}
