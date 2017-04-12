package com.k9rosie.novswar.config;

import java.util.List;

public class CoreConfig extends NovsConfig {

    private String databaseConnector;
    private String databasePath;
    private String databasePrefix;
    private int databaseFlushInterval;
    private String databaseHostname;
    private int databasePort;
    private String databaseName;
    private String databaseUsername;
    private String databasePassword;

    private List<String> enabledWorlds;
    private String lobbyWorld;

    private boolean votingEnabled;

    private int gameMinimumPlayers;
    private int gameLargestTeamImbalance;
    private boolean gameJoinInProgress;
    private int gamePreGameTimer;
    private int gamePostGameTimer;
    private int gameRespawnTimer;
    private boolean gameEnableHunger;
    private boolean gameFastHealthRegen;

    private boolean debug;

    public CoreConfig() {
        super("core.yml");
    }

    public String getDatabaseConnector() {
        return databaseConnector;
    }

    public String getDatabasePath() {
        return databasePath;
    }

    public String getDatabasePrefix() {
        return databasePrefix;
    }

    public int getDatabaseFlushInterval() {
        return databaseFlushInterval;
    }

    public String getDatabaseHostname() {
        return databaseHostname;
    }

    public int getDatabasePort() {
        return databasePort;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public List<String> getEnabledWorlds() {
        return enabledWorlds;
    }

    public String getLobbyWorld() {
        return lobbyWorld;
    }

    public boolean getVotingEnabled() {
        return votingEnabled;
    }

    public int getGameMinimumPlayers() {
        return gameMinimumPlayers;
    }

    public int getGameLargestTeamImbalance() {
        return gameLargestTeamImbalance;
    }

    public boolean getGameJoinInProgress() {
        return gameJoinInProgress;
    }

    public int getGamePreGameTimer() {
        return gamePreGameTimer;
    }

    public int getGamePostGameTimer() {
        return gamePostGameTimer;
    }

    public int getGameRespawnTimer() {
        return gameRespawnTimer;
    }

    public boolean getGameEnableHunger() {
        return gameEnableHunger;
    }

    public boolean getGameFastHealthRegen() {
        return gameFastHealthRegen;
    }

    public boolean getDebug() {
        return debug;
    }

    public void reloadData() {
        databaseConnector = getConfig().getString("core.database.connector");
        databasePath = getConfig().getString("core.database.path");
        databasePrefix = getConfig().getString("core.database.prefix");
        databaseFlushInterval = getConfig().getInt("core.database.flush_interval");
        databaseHostname = getConfig().getString("core.database.mysql.hostname");
        databasePort = getConfig().getInt("core.database.mysql.port");
        databaseName = getConfig().getString("core.database.mysql.database");
        databaseUsername = getConfig().getString("core.database.mysql.username");
        databasePassword = getConfig().getString("core.database.mysql.password");

        enabledWorlds = getConfig().getStringList("core.world.enabled_worlds");
        lobbyWorld = getConfig().getString("core.world.lobby_world");

        votingEnabled = getConfig().getBoolean("core.voting.enabled");

        gameMinimumPlayers = getConfig().getInt("core.game.minimum_players");
        gameLargestTeamImbalance = getConfig().getInt("core.game.largest_team_imbalance");
        gameJoinInProgress = getConfig().getBoolean("core.game.join_in_progress");
        gamePreGameTimer = getConfig().getInt("core.game.pre_game_timer");
        gamePostGameTimer = getConfig().getInt("core.game.post_game_timer");
        gameRespawnTimer = getConfig().getInt("core.game.respawn_timer");
        gameEnableHunger = getConfig().getBoolean("core.game.enable_hunger");
        gameFastHealthRegen = getConfig().getBoolean("core.game.fast_health_regen");

        debug = getConfig().getBoolean("core.debug");
    }
}
