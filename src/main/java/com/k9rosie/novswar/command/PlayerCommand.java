package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.config.MessagesConfig;
import com.k9rosie.novswar.player.NovsPlayer;
import com.k9rosie.novswar.player.NovsStats;
import com.k9rosie.novswar.team.NovsTeam;
import com.k9rosie.novswar.util.ChatUtil;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PlayerCommand implements NovsCommand {
    private String permissions;
    private String description;
    private int requiredNumofArgs;
    private boolean playerOnly;
    private NovsWar novsWar;

    public PlayerCommand(NovsWar novsWar) {
        permissions = "novswar.command.player";
        description = "Display stats on yourself or another player";
        requiredNumofArgs = 0;
        playerOnly = false;
        this.novsWar = novsWar;
    }

    public void execute(CommandSender sender, String[] args) {
    	if (args.length <= 1 && !(sender instanceof Player)) {
    	    ChatUtil.sendError(sender, "You need to be a player to display stats on another player");
    	    return;
        }

        if (args.length == 1) {
            printStats(sender, novsWar.getPlayerManager().getPlayers().get(sender));
        } else if (args.length == 2) {
            String playerName = args[1];

            if (NovsWar.isOnline(playerName)) {
                NovsPlayer target = novsWar.getPlayerManager().getPlayer(playerName);
                printStats(sender, target);
            } else {
                UUID uuid = NovsWar.getUUID(playerName);

                if (uuid == null) {
                	ChatUtil.sendNotice(sender, MessagesConfig.getPlayerDataNonexistent());
                    return;
                }
                if (!novsWar.getDatabase().exists("stats", "player_uuid", uuid.toString())) {
                	ChatUtil.sendNotice(sender, MessagesConfig.getPlayerDataNonexistent());
                    return;
                } else {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                    printStats(sender, offlinePlayer);
                }
            }
        }
    }

    private void printStats(CommandSender sender, NovsPlayer player) {
        NovsStats stats = player.getStats();
        Player bukkitPlayer = player.getBukkitPlayer();
        NovsTeam team = player.getPlayerState().getTeam();
        double kd = 0.0;
        if ((stats.getDeaths()+stats.getArrowDeaths()) == 0) {
            kd = stats.getKills()+stats.getArrowKills();
        } else {
            kd = (stats.getKills()+stats.getArrowKills())/(stats.getDeaths()+stats.getArrowDeaths());
        }

        Date lastPlayed = stats.getLastPlayed();
        SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy h:mm a");
        String date = format.format(lastPlayed);
        String[] statsString = {
                "§a§o§lONLINE",
                "§7Team: §a" + team.getColor() + team.getTeamName(),
                "§7Kills: §a" + stats.getKills(),
                "§7Deaths: §a" + stats.getDeaths(),
                "§7KD Ratio: §a" + kd,
                "§7Suicides: §a" + stats.getSuicides(),
                "§7Arrow Kills: §a" + stats.getArrowKills(),
                "§7Arrow Deaths: §a" + stats.getArrowDeaths(),
                "§7Wins: §a" + stats.getWins(),
                "§7Games Played: §a" + stats.getGamesPlayed(),
                "§7Damage Given: §a" + stats.getDamageGiven(),
                "§7Damage Taken: §a" + stats.getDamageTaken(),
                "§7Connects: §a" + stats.getConnects(),
                "§7Last Played: §a" + date,
                "§7Logged In Since: §a" + loggedIn(stats.getLoggedIn())
        };
        sender.sendMessage("§aPlayer data for "+team.getColor()+bukkitPlayer.getDisplayName()+"§a:");
        sender.sendMessage(statsString);
    }

    private void printStats(CommandSender sender, OfflinePlayer offlinePlayer) {
        String uuid = offlinePlayer.getUniqueId().toString();

        ResultSet results = novsWar.getDatabase().select("stats", "player_uuid", uuid);
        String[] statsString = {""};
        double kd = 0.0;

        try {
            results.next();
            if ((results.getInt("deaths")+results.getInt("arrow_deaths")) == 0) {
                kd = results.getInt("kills")+results.getInt("arrow_kills");
            } else {
                kd = (results.getInt("kills")+results.getInt("arrow_kills"))/(results.getInt("deaths")+results.getInt("arrow_deaths"));
            }

            Date lastPlayed = results.getDate("last_played");
            SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy h:mm a");
            String date = format.format(lastPlayed);
            statsString = new String[] {
                    "§7§o§lOFFLINE",
                    "§7Kills: §a" + results.getInt("kills"),
                    "§7Deaths: §a" + results.getInt("deaths"),
                    "§7KD Ratio: §a" + kd,
                    "§7Suicides: §a" + results.getInt("suicides"),
                    "§7Arrow Kills: §a" + results.getInt("arrow_kills"),
                    "§7Arrow Deaths: §a" + results.getInt("arrow_deaths"),
                    "§7Wins: §a" + results.getInt("wins"),
                    "§7Games Played: §a" + results.getInt("games_played"),
                    "§7Damage Given: §a" + results.getDouble("damage_given"),
                    "§7Damage Taken: §a" + results.getInt("damage_taken"),
                    "§7Connects: §a" + results.getInt("connects"),
                    "§7Last Played: §a" + date
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sender.sendMessage("§aPlayer data for §7"+offlinePlayer.getName()+"§a:");
        sender.sendMessage(statsString);

    }

    private String totalTimePlayed(long time) {
        return "";
    } // TODO: do something about this

    private String loggedIn(Timestamp timestamp) {
        long currentTime = System.currentTimeMillis();
        long time = currentTime - timestamp.getTime();
        long seconds = time/1000;
        long minutes = seconds/60;
        long hours = minutes/60;
        long days = hours/24;
        long years = days/360;

        StringBuilder builder = new StringBuilder();
        if (years == 1) {
            builder.append(Long.toString(years) + " year ");
        } else if (years != 0) {
            builder.append(Long.toString(years) + " years ");
        }

        if (days == 1) {
            builder.append(Long.toString(days % 360) + " day ");
        } else if (days != 0) {
            builder.append(Long.toString(days % 360) + " days ");
        }

        if (hours == 1) {
            builder.append(Long.toString(hours % 24) + " hour ");
        } else if (hours != 0) {
            builder.append(Long.toString(hours % 24) + " hours ");
        }

        if (minutes == 1) {
            builder.append(Long.toString(minutes % 60) + " minute ");
        } else if (minutes != 0) {
            builder.append(Long.toString(minutes % 60) + " minutes ");
        }

        if (seconds == 1) {
            builder.append(Long.toString(seconds % 60) + " second ");
        } else if (seconds != 0) {
            builder.append(Long.toString(seconds % 60) + " seconds ");
        }

        return builder.toString();
    }

    @Override
    public String getPermissions() {
        return permissions;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getRequiredNumofArgs() {
        return requiredNumofArgs;
    }

    @Override
    public boolean isPlayerOnly() {
        return playerOnly;
    }
}
