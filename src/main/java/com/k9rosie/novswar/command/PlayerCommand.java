package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsStats;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.util.ChatFormat;
import com.k9rosie.novswar.util.Messages;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PlayerCommand extends NovsCommand {

    public PlayerCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
    }

    public void execute() {
    	NovsPlayer player = getNovsWar().getNovsPlayerCache().getPlayers().get((Player) getSender());
        if (getArgs().length == 1) {
            printStats(player);
        } else if (getArgs().length == 2) {
            String playerName = getArgs()[1];
            if (NovsWar.isOnline(playerName)) {
                NovsPlayer target = getNovsWar().getNovsPlayerCache().getPlayerFromName(playerName);
                printStats(target);
            } else {
                UUID uuid = NovsWar.getUUID(playerName);
                if (uuid == null) {
                	ChatFormat.sendNotice(player, Messages.PLAYER_DATA_NONEXISTENT.toString());
                    return;
                }
                if (!getNovsWar().getDatabase().exists("stats", "player_uuid", uuid.toString())) {
                	ChatFormat.sendNotice(player, Messages.PLAYER_DATA_NONEXISTENT.toString());
                    return;
                } else {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                    printStats(offlinePlayer);
                }
            }
        } else {
        	ChatFormat.sendNotice(player, Messages.INVALID_PARAMETERS.toString());
        }
    }

    public void printStats(NovsPlayer player) {
        NovsStats stats = player.getStats();
        Player bukkitPlayer = player.getBukkitPlayer();
        NovsTeam team = player.getTeam();
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
                "§7Team: §f" + team.getColor() + team.getTeamName(),
                "§7Kills: §f" + stats.getKills(),
                "§7Deaths: §f" + stats.getDeaths(),
                "§7KD Ratio: §f" + kd,
                "§7Suicides: §f" + stats.getSuicides(),
                "§7Arrow Kills: §f" + stats.getArrowKills(),
                "§7Arrow Deaths: §f" + stats.getArrowDeaths(),
                "§7Wins: §f" + stats.getWins(),
                "§7Games Played: §f" + stats.getGamesPlayed(),
                "§7Damage Given: §f" + stats.getDamageGiven(),
                "§7Damage Taken: §f" + stats.getDamageTaken(),
                "§7Connects: §f" + stats.getConnects(),
                "§7Last Played: §f" + date,
                "§7Logged In Since: §f" + loggedIn(stats.getLoggedIn())
        };
        getSender().sendMessage("§aPlayer data for "+team.getColor()+bukkitPlayer.getDisplayName()+"§a:");
        getSender().sendMessage(statsString);
    }

    public void printStats(OfflinePlayer offlinePlayer) {
        String uuid = offlinePlayer.getUniqueId().toString();

        ResultSet results = getNovsWar().getDatabase().select("stats", "player_uuid", uuid);
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
                    "§7Kills: §f" + results.getInt("kills"),
                    "§7Deaths: §f" + results.getInt("deaths"),
                    "§7KD Ratio: §f" + kd,
                    "§7Suicides: §f" + results.getInt("suicides"),
                    "§7Arrow Kills: §f" + results.getInt("arrow_kills"),
                    "§7Arrow Deaths: §f" + results.getInt("arrow_deaths"),
                    "§7Wins: §f" + results.getInt("wins"),
                    "§7Games Played: §f" + results.getInt("games_played"),
                    "§7Damage Given: §f" + results.getDouble("damage_given"),
                    "§7Damage Taken: §f" + results.getInt("damage_taken"),
                    "§7Connects: §f" + results.getInt("connects"),
                    "§7Last Played: §f" + date
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getSender().sendMessage("§aPlayer data for §7"+offlinePlayer.getName()+"§a:");
        getSender().sendMessage(statsString);

    }

    public String totalTimePlayed(long time) {
        return "";
    }

    public String loggedIn(Timestamp timestamp) {
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
}
