package com.k9rosie.novswar.database;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private Connection connection;
    private Properties properties;
    private DatabaseType type;
    private Database database;

    public DatabaseConnection(String name, DatabaseType type) {
        this.type = type;
        database = new Database(name);
    }

    public void pingDatabase() {

    }

    public String getDatabasePath() {
        return "";
    }

    public void setProperties(String autoReconnect, String user, String password) {
        properties.put("autoReconnect", autoReconnect);
        properties.put("user", user);
        properties.put("password", password);
    }

    public void connect() throws Exception {
        if (connection != null) {
            return;
        }

        ClassLoader classLoader = Bukkit.getServer().getClass().getClassLoader();
        String className = "";
        switch (type) {
            case MySQL:
                className = "com.mysql.jdbc.Driver";
            case SQLite:
                className = "org.sqlite.JDBC";
        }

        Driver driver = (Driver) classLoader.loadClass(className).newInstance();

        try {
            connection = driver.connect("jdbc:" + type.toString().toLowerCase() + ":" + getDatabasePath(), properties);
            return;
        } catch (SQLException e) {
            
        }

    }

    public void disconnect() {

    }
}
