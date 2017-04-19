package com.k9rosie.novswar.database;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.config.CoreConfig;
import com.k9rosie.novswar.player.NovsPlayer;

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
            int sleep = novsWar.getConfigManager().getCoreConfig().getDatabaseFlushInterval();
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(sleep*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    NovsWar.debug("Flushing database..");
                    flushDatabase();
                    NovsWar.debug("Database flushed.");
                }
            }
        });

        databaseFlusher.start();
    }

    public void createDatabase() {
        CoreConfig coreConfig = novsWar.getConfigManager().getCoreConfig();
        String type = coreConfig.getDatabaseConnector();
        String prefix = coreConfig.getDatabasePrefix();

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
