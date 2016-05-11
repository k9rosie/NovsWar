package com.k9rosie.novswar.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Database {
    private String prefix;
    private DatabaseType type;
    private String path;

    private DatabaseConnection databaseConnection;
    private ExecutorService queryExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

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
