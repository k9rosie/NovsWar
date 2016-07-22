package com.k9rosie.novswar.database;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;
import org.bukkit.configuration.file.FileConfiguration;

public class DatabaseThread implements Runnable {
    private final NovsWar novsWar;

    private NovswarDB database;
    private Thread thread;

    private boolean running = false;

    public DatabaseThread(NovsWar novsWar) {
        this.novsWar = novsWar;
        thread = new Thread(this);
    }

    public void run() {
        running = true;

        createDatabase();
        database.initialize();

        Thread databaseFlusher = new Thread(new Runnable() {
            int sleep = novsWar.getConfigurationCache().getConfig("core").getInt("core.database.flush_interval");
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(sleep*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    flushDatabase();
                }
            }
        });

        databaseFlusher.run();
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

    public void flushDatabase() {
        for (NovsPlayer player : novsWar.getPlayerManager().getPlayers().values()) {
            database.flushPlayerData(player);
        }
    }
}
