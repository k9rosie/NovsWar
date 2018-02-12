package com.k9rosie.novswar.database;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.config.CoreConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DatabaseConnection {
    private NovsWar novswar;

    private Connection connection;
    private Properties properties;
    private Database database;
    private ExecutorService queryExecutor;

    public DatabaseConnection(NovsWar novswar, Database database) {
        this.novswar = novswar;
        this.database = database;
        queryExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        properties = new Properties();
    }

    public void connect() throws Exception {
        if (connection != null) {
            return;
        }

        CoreConfig config = novswar.getConfigManager().getCoreConfig();
        String hostnameString;
        String portString;
        String databaseString;
        String userString;
        String passwordString;
        String pathString;

        if (!database.getType().equalsIgnoreCase("sqlite")) {
            hostnameString = config.getDatabaseHostname();
            portString = config.getDatabasePort();
            databaseString = config.getDatabasePort();
            userString = config.getDatabaseUsername();
            passwordString = config.getDatabasePassword();
            pathString = "//" + hostnameString+":"+portString + "/" + databaseString;
            setProperties("true", userString, passwordString);
            System.out.println("hostname: " + hostnameString);
            System.out.println("port: " + portString);
            System.out.println("database: " + databaseString);
            System.out.println("username: " + userString);
            System.out.println("password: " + passwordString);
            System.out.println("path: " + pathString);
            System.out.println("full path: " + "jdbc:" + database.getType().toLowerCase() + ":" + pathString);
        } else {
            pathString = config.getDatabasePath();
        }

        try {
            connection = DriverManager.getConnection("jdbc:" + database.getType().toLowerCase() + ":" + pathString, properties);

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

    public void executeUpdate(String query) {
        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public PreparedStatement prepare(String sql) {
        if (connection == null) {
            return null;
        }

        try {
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResultSet scheduleQuery(String query) {
        Future<ResultSet> futureResult = queryExecutor.submit(new AsyncQuerySQL(this, query));

        try {
            return futureResult.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void scheduleUpdate(String query) {
        queryExecutor.execute(new ASyncUpdateSQL(this, query));
    }

    public void pingDatabase() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeQuery("SELECT 1;");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setProperties(String autoReconnect, String user, String password) {
        properties.put("autoReconnect", autoReconnect);
        properties.put("user", user);
        properties.put("password", password);
    }

    public Connection getConnection() {
        return connection;
    }
}
