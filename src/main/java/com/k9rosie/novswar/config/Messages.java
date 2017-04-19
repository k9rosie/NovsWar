package com.k9rosie.novswar.config;

import org.bukkit.ChatColor;

public enum Messages {
    //Indented enums are in use. Others are not.
    PLUGIN_TAG(MessagesConfig.PLUGIN_TAG),
    INVALID_PARAMETERS(MessagesConfig.INVALID_PARAMETERS),
    PLAYER_OFFLINE(MessagesConfig.PLAYER_OFFLINE),
    TEAM_MISSING(MessagesConfig.TEAM_MISSING),
    SIGN_CREATED(MessagesConfig.SIGN_CREATED),
    NO_PERMISSION(MessagesConfig.NO_PERMISSION),
    TEAM_FULL(MessagesConfig.TEAM_FULL),
    ALREADY_ASSIGNED(MessagesConfig.ALREADY_ASSIGNED),
    JOIN_TEAM(MessagesConfig.JOIN_TEAM),
    DEATH_MESSAGE(MessagesConfig.DEATH_MESSAGE),
    KILL_MESSAGE(MessagesConfig.KILL_MESSAGE),
    SHOT_MESSAGE(MessagesConfig.SHOT_MESSAGE),
    SPAWN_SET(MessagesConfig.SPAWN_SET),
    CANNOT_JOIN_TEAM(MessagesConfig.CANNOT_JOIN_TEAM),
    CANNOT_JOIN_GAME(MessagesConfig.CANNOT_JOIN_GAME),
    COMMAND_NONEXISTENT(MessagesConfig.COMMAND_NONEXISTENT),
    PLAYER_DATA_NONEXISTENT(MessagesConfig.PLAYER_DATA_NONEXISTENT),
    NOT_ENOUGH_PLAYERS(MessagesConfig.NOT_ENOUGH_PLAYERS);

    private String message;

    Messages(String message) {
        this.message = message;
    }

    public String toString() {
        if (this == PLUGIN_TAG) {
            return ChatColor.translateAlternateColorCodes('&', message) + " ";
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}