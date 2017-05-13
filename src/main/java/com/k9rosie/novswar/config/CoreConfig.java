package com.k9rosie.novswar.config;

import com.k9rosie.novswar.NovsWarPlugin;

import java.util.List;

public class CoreConfig extends NovsConfig {

    private String databaseConnector;
    private String databasePath;
    private String databasePrefix;
    private int databaseFlushInterval;
    private String databaseHostname;
    private String databasePort;
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
    private int gameAssistTimer;

    private String deathParticleType;
    private int deathParticleCount;
    private String deathSoundType;
    private float deathSoundVolume;
    private float deathSoundPitch;

    private String respawnParticleType;
    private int respawnParticleCount;
    private String respawnSoundType;
    private float respawnSoundVolume;
    private float respawnSoundPitch;

    private boolean debug;

    public CoreConfig(NovsWarPlugin plugin) {
        super(plugin, "core.yml");
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

    public String getDatabasePort() {
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

    public int getGameAssistTimer() { return gameAssistTimer; }

    public String getDeathParticleType() {
        return deathParticleType;
    }

    public int getDeathParticleCount() {
        return deathParticleCount;
    }

    public String getDeathSoundType() {
        return deathSoundType;
    }

    public float getDeathSoundVolume() {
        return deathSoundVolume;
    }

    public float getDeathSoundPitch() {
        return deathSoundPitch;
    }

    public String getRespawnParticleType() {
        return respawnParticleType;
    }

    public int getRespawnParticleCount() {
        return respawnParticleCount;
    }

    public String getRespawnSoundType() {
        return respawnSoundType;
    }

    public float getRespawnSoundVolume() {
        return respawnSoundVolume;
    }

    public float getRespawnSoundPitch() {
        return respawnSoundPitch;
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
        databasePort = getConfig().getString("core.database.mysql.port");
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
        gameAssistTimer = getConfig().getInt("core.game.assist_timer");

        deathParticleType =  getConfig().getString("core.effects.death.particle.type");
        deathParticleCount = getConfig().getInt("core.effects.death.particle.count");
        deathSoundType =     getConfig().getString("core.effects.death.sound.type");
        deathSoundVolume =   (float) getConfig().getDouble("core.effects.death.sound.volume");
        deathSoundPitch =    (float) getConfig().getDouble("core.effects.death.pitch.volume");

        respawnParticleType = getConfig().getString("core.effects.respawn.particle.type");
        respawnParticleCount = getConfig().getInt("core.effects.respawn.particle.count");
        respawnSoundType = getConfig().getString("core.effects.respawn.sound.type");
        respawnSoundVolume = (float) getConfig().getDouble("core.respawn.death.sound.volume");
        respawnSoundPitch = (float) getConfig().getDouble("core.respawn.death.pitch.volume");

        debug = getConfig().getBoolean("core.debug");
    }
}
