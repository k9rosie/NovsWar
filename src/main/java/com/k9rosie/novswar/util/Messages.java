package com.k9rosie.novswar.util;

import com.k9rosie.novswar.NovsWar;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public enum Messages {
    PLUGIN_TAG("plugin_tag", "[NovsWar]"),
    INVALID_PARAMETERS("invalid_params", "The parameters you have specified are not valid"),
    PLAYER_OFFLINE("player_offline", "%player% is not online"),
    TEAM_MISSING("team_missing", "The team %team_color%%team% doesn't exist"),
    SIGN_CREATED("sign_created", "Sign created"),
    NO_PERMISSION("no_permission", "You don't have the required permission to do that"),
    TEAM_FULL("team_full", "That team has too many players"),
    ALREADY_ASSIGNED("already_assigned", "You've already been assigned to that team"),
    JOIN_TEAM("join_team", "You've joined %team_color%%team%"),
    DEATH_MESSAGE("death_message", "%player% has died"),
    KILL_MESSAGE("kill_message", "%killed_tcolor%%killed% was slain by %killer_tcolor%%killer% with %weapon%"),
    SHOT_MESSAGE("shot_message", "%killed_tcolor%%killed% was shot by %killer% with %killer_tcolor%%weapon%"),
    SPAWN_SET("spawn_set", "Spawn point created for %team_color%%team%"),
    CANNOT_JOIN_TEAM("cannot_join_team", "Can't join team %team_color%%team%");

    private NovsWar novsWar;
    private FileConfiguration config;

    private String path;
    private String defaultMessage;

    Messages(String path, String defaultMessage) {
        this.path = path;
        this.defaultMessage = defaultMessage;

        novsWar = NovsWar.getInstance();
        config = novsWar.getConfigurationCache().getConfig("messages");
    }

    public String toString() {
        if (this == PLUGIN_TAG) {
            return ChatColor.translateAlternateColorCodes('&', config.getString(this.path, defaultMessage)) + " ";
        }
        return ChatColor.translateAlternateColorCodes('&', config.getString(this.path, defaultMessage));
    }

    public String getDefault() {
        return this.defaultMessage;
    }

    public String getPath() {
        return this.path;
    }

}
