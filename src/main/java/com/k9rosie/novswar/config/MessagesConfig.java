package com.k9rosie.novswar.config;

import com.k9rosie.novswar.NovsWarPlugin;
import org.bukkit.ChatColor;

public class MessagesConfig extends NovsConfig {

    static String PLUGIN_TAG;
    static String INVALID_PARAMETERS;
    static String PLAYER_OFFLINE;
    static String TEAM_MISSING;
    static String SIGN_CREATED;
    static String NO_PERMISSION;
    static String TEAM_FULL;
    static String ALREADY_ASSIGNED;
    static String JOIN_TEAM;
    static String DEATH_MESSAGE;
    static String KILL_MESSAGE;
    static String SHOT_MESSAGE;
    static String SPAWN_SET;
    static String CANNOT_JOIN_TEAM;
    static String CANNOT_JOIN_GAME;
    static String COMMAND_NONEXISTENT;
    static String PLAYER_DATA_NONEXISTENT;
    static String NOT_ENOUGH_PLAYERS;

    public MessagesConfig(NovsWarPlugin plugin) {
        super(plugin, "messages.yml");
    }

    public void reloadData() {
        PLUGIN_TAG = getConfig().getString("plugin_tag");
        INVALID_PARAMETERS = getConfig().getString("invalid_parameters");
        PLAYER_OFFLINE = getConfig().getString("player_offline");
        TEAM_MISSING = getConfig().getString("team_missing");
        SIGN_CREATED = getConfig().getString("sign_created");
        NO_PERMISSION = getConfig().getString("no_permission");
        TEAM_FULL = getConfig().getString("team_full");
        ALREADY_ASSIGNED = getConfig().getString("already_assigned");
        JOIN_TEAM = getConfig().getString("join_team");
        DEATH_MESSAGE = getConfig().getString("death_message");
        KILL_MESSAGE = getConfig().getString("kill_message");
        SHOT_MESSAGE = getConfig().getString("shot_message");
        SPAWN_SET = getConfig().getString("spawn_set");
        CANNOT_JOIN_TEAM = getConfig().getString("cannot_join_team");
        CANNOT_JOIN_GAME = getConfig().getString("cannot_join_game");
        COMMAND_NONEXISTENT = getConfig().getString("command_nonexistent");
        PLAYER_DATA_NONEXISTENT = getConfig().getString("player_data_nonexistent");
        NOT_ENOUGH_PLAYERS = getConfig().getString("not_enough_players");
    }
}