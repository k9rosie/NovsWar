package com.k9rosie.novswar.util;

import com.k9rosie.novswar.NovsWar;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public enum Messages {
    PLUGIN_TAG("plugin_tag"),
    INVALID_PARAMETERS("invalid_parameters"),
    PLAYER_OFFLINE("player_offline"),
    TEAM_MISSING("team_missing"),
    SIGN_CREATED("sign_created"),
    NO_PERMISSION("no_permission"),
    TEAM_FULL("team_full"),
    ALREADY_ASSIGNED("already_assigned"),
    JOIN_TEAM("join_team"),
    DEATH_MESSAGE("death_message"),
    KILL_MESSAGE("kill_message"),
    SHOT_MESSAGE("shot_message"),
    SPAWN_SET("spawn_set"),
    CANNOT_JOIN_TEAM("cannot_join_team"),
    CANNOT_JOIN_GAME("cannot_join_game"),
    COMMAND_NONEXISTENT("command_nonexistent"),
    PLAYER_DATA_NONEXISTENT("player_data_nonexistent"),
    NOT_ENOUGH_PLAYERS("not_enough_players");

    private NovsWar novsWar;
    private FileConfiguration config;

    private String path;

    Messages(String path) {
        this.path = path;

        novsWar = NovsWar.getInstance();
        config = novsWar.getNovsConfigCache().getConfig("messages");
    }

    public String toString() {
        if (this == PLUGIN_TAG) {
            return ChatColor.translateAlternateColorCodes('&', config.getString(this.path)) + " ";
        }
        return ChatColor.translateAlternateColorCodes('&', config.getString(this.path));
    }

    public String getPath() {
        return this.path;
    }

}
