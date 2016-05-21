package com.k9rosie.novswar.database;

import com.k9rosie.novswar.NovsWar;
import org.bukkit.configuration.file.FileConfiguration;

public class DatabaseThread implements Runnable {
    private final NovsWar novsWar;

    private NovswarDB database;
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

        database = new NovswarDB(DatabaseType.matchType(type), prefix);

    }

    public Thread getThread() {
        return thread;
    }

    public NovswarDB getDatabase() {
        return database;
    }
}
