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
        String type = coreConfig.getString("core.database.connector");
        String prefix = coreConfig.getString("core.database.prefix");

        if (!type.equalsIgnoreCase("sqlite")) {
            String hostname = coreConfig.getString("core.database.mysql.hostname");
            String port = coreConfig.getString("core.database.mysql.port");
            String database = coreConfig.getString("core.database.mysql.database");
            String user = coreConfig.getString("core.database.mysql.username");
            String password = coreConfig.getString("core.database.mysql.password");

            String path = "//" + hostname+":"+port + "/" + database;
            this.database = new Database(prefix, DatabaseType.matchType(type), path);
            this.database.getDatabaseConnection().setProperties("true", user, password);
        } else {
            String path = coreConfig.getString("core.database.path");
            database = new Database(prefix, DatabaseType.matchType(type), path);
        }
    }

    public Thread getThread() {
        return thread;
    }
}
