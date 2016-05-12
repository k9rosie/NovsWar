package com.k9rosie.novswar.database;

import com.k9rosie.novswar.NovsWar;
import org.bukkit.configuration.file.FileConfiguration;

public class DatabaseThread implements Runnable {
    private final NovsWar novsWar;

    private Database database;
    private Thread thread;

    private boolean running = false;

    public DatabaseThread(NovsWar novsWar) {
        this.novsWar = novsWar;
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void run() {
        running = true;

        createDatabase();
        database.initialize();
    }

    public void createDatabase() {
        FileConfiguration coreConfig = novsWar.getConfigurationCache().getConfig("core");
        String type = coreConfig.getString("novswar.database.connector");
        String prefix = coreConfig.getString("novswar.database.prefix");

        if (!type.equalsIgnoreCase("sqlite")) {
            String hostname = coreConfig.getString("novswar.database.mysql.hostname");
            String port = coreConfig.getString("novswar.database.mysql.port");
            String database = coreConfig.getString("novswar.database.mysql.database");
            String user = coreConfig.getString("novswar.database.mysql.username");
            String password = coreConfig.getString("novswar.database.mysql.password");

            String path = "//" + hostname+":"+port + "/" + database;
            this.database = new Database(prefix, DatabaseType.matchType(type), path);
            this.database.getDatabaseConnection().setProperties("true", user, password);
        } else {
            String path = coreConfig.getString("novswar.database.path");
            database = new Database(prefix, DatabaseType.matchType(type), path);
        }
    }

    public Thread getThread() {
        return thread;
    }
}
