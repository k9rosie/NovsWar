package com.k9rosie.novswar.database;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private Connection connection;
    private String path;
    private Properties properties;
    private Database database;

    public DatabaseConnection(Database database) {
        this.database = database;
        this.path = path;
    }

    public void pingDatabase() {

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
        DatabaseType type = database.getType();

        switch (type) {
            case MySQL:
                className = "com.mysql.jdbc.Driver";
            case SQLite:
                className = "org.sqlite.JDBC";
        }

        Driver driver = (Driver) classLoader.loadClass(className).newInstance();

        try {
            connection = driver.connect("jdbc:" + type.toString().toLowerCase() + ":" + path, properties);
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        connection = null;
    }

    public Connection getConnection() {
        return connection;
    }
}
