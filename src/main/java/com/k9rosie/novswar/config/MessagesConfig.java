package com.k9rosie.novswar.config;

import com.k9rosie.novswar.NovsWarPlugin;
import org.bukkit.ChatColor;

public class MessagesConfig extends NovsConfig {
    private static String pluginTag;
    private static String invalidParameters;
    private static String playerOffline;
    private static String teamMissing;
    private static String signCreated;
    private static String noPermission;
    private static String teamFull;
    private static String alreadyAssigned;
    private static String joinTeam;
    private static String deathMessage;
    private static String killMessage;
    private static String shotMessage;
    private static String spawnSet;
    private static String cannotJoinTeam;
    private static String cannotJoinGame;
    private static String commandNonexistent;
    private static String playerDataNonexistent;
    private static String notEnoughPlayers;

    public static String getPluginTag() {
        String message = ChatColor.translateAlternateColorCodes('&', pluginTag) + " ";
        return message;
    }

    public static String getInvalidParameters() {
        String message = ChatColor.translateAlternateColorCodes('&', invalidParameters);
        return message;
    }

    public static String getPlayerOffline(String player) {
        String message = playerOffline.replace(":player", player);
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public static String getTeamMissing(String teamColor, String team) {
        String message = teamMissing.replace(":t_color", teamColor)
                .replace(":team", team);
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public static String getSignCreated() {
        String message = ChatColor.translateAlternateColorCodes('&', signCreated);
        return message;
    }

    public static String getNoPermission() {
        String message = ChatColor.translateAlternateColorCodes('&', noPermission);
        return message;
    }

    public static String getTeamFull() {
        String message = ChatColor.translateAlternateColorCodes('&', teamFull);
        return message;
    }

    public static String getAlreadyAssigned() {
        String message = ChatColor.translateAlternateColorCodes('&', alreadyAssigned);
        return message;
    }

    public static String getJoinTeam(String teamColor, String team) {
        String message = joinTeam.replace(":t_color", teamColor)
                .replace(":team", team);
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public static String getDeathMessage(String playerColor, String player) {
        String message = deathMessage.replace(":p_color", playerColor)
                .replace(":player", player);
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public static String getKillMessage(String killerColor, String killer, String killedColor, String killed) {
        String message = killMessage.replace(":kr_color", killerColor)
                .replace(":killer", killer)
                .replace(":kd_color", killedColor)
                .replace(":killed", killed);
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public static String getShotMessage(String killerColor, String killer, String killedColor, String killed) {
        String message = shotMessage.replace(":kr_color", killerColor)
                .replace(":killer", killer)
                .replace(":kd_color", killedColor)
                .replace(":killed", killed);
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public static String getSpawnSet(String teamColor, String team) {
        String message = spawnSet.replace(":t_color", teamColor)
                .replace(":team", team);
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public static String getCannotJoinTeam(String teamColor, String team) {
        String message = cannotJoinTeam.replace(":t_color", teamColor)
                .replace(":team", team);
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public static String getCannotJoinGame() {
        String message = cannotJoinGame;
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public static String getCommandNonexistent() {
        String message = commandNonexistent;
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public static String getPlayerDataNonexistent() {
        String message = playerDataNonexistent;
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public static String getNotEnoughPlayers(String minimum) {
        String message = notEnoughPlayers.replace(":minimum", minimum);
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public MessagesConfig(NovsWarPlugin plugin) {
        super(plugin, "messages.yml");
    }

    public void reloadData() {
        pluginTag = getConfig().getString("plugin_tag");
        invalidParameters = getConfig().getString("invalid_parameters");
        playerOffline = getConfig().getString("player_offline");
        teamMissing = getConfig().getString("team_missing");
        signCreated = getConfig().getString("sign_created");
        noPermission = getConfig().getString("no_permission");
        teamFull = getConfig().getString("team_full");
        alreadyAssigned = getConfig().getString("already_assigned");
        joinTeam = getConfig().getString("join_team");
        deathMessage = getConfig().getString("death_message");
        killMessage = getConfig().getString("kill_message");
        shotMessage = getConfig().getString("shot_message");
        spawnSet = getConfig().getString("spawn_set");
        cannotJoinTeam = getConfig().getString("cannot_join_team");
        cannotJoinGame = getConfig().getString("cannot_join_game");
        commandNonexistent = getConfig().getString("command_nonexistent");
        playerDataNonexistent = getConfig().getString("player_data_nonexistent");
        notEnoughPlayers = getConfig().getString("not_enough_players");
    }
}