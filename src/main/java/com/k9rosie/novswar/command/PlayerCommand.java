package com.k9rosie.novswar.command;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.model.NovsPlayer;
import com.k9rosie.novswar.model.NovsStats;
import com.k9rosie.novswar.model.NovsTeam;
import com.k9rosie.novswar.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerCommand extends NovsCommand {

    public PlayerCommand(NovsWar novsWar, CommandSender sender, String[] args) {
        super(novsWar, sender, args);
    }

    public void execute() {
        if (getArgs().length == 1) {
            NovsPlayer player = getNovsWar().getPlayerManager().getNovsPlayer((Player) getSender());
            printStats(player);
        } else if (getArgs().length == 2) {
            String playerName = getArgs()[1];
            if (NovsWar.isOnline(playerName)) {
                NovsPlayer player = getNovsWar().getPlayerManager().getNovsPlayer(playerName);
                printStats(player);
            } else {
                UUID uuid = NovsWar.getUUID(playerName);
                if (uuid == null) {
                    getSender().sendMessage(Messages.PLAYER_DATA_NONEXISTENT.toString());
                    return;
                }
                if (!getNovsWar().getDatabase().exists("stats", "player_uuid", uuid.toString())) {
                    getSender().sendMessage(Messages.PLAYER_DATA_NONEXISTENT.toString());
                    return;
                } else {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                    printStats(offlinePlayer);
                }
            }
        } else {
            getSender().sendMessage(Messages.INVALID_PARAMETERS.toString());
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
                "§7Losses: §f" + stats.getLosses(),
                "§7Damage Given: §f" + stats.getDamageGiven(),
                "§7Damage Taken: §f" + stats.getDamageTaken(),
                "§7Connects: §f" + stats.getConnects()
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
            statsString = new String[] {
                    "§7§o§lOFFLINE",
                    "§7Kills: §f" + results.getInt("kills"),
                    "§7Deaths: §f" + results.getInt("deaths"),
                    "§7KD Ratio: §f" + kd,
                    "§7Suicides: §f" + results.getInt("suicides"),
                    "§7Arrow Kills: §f" + results.getInt("arrow_kills"),
                    "§7Arrow Deaths: §f" + results.getInt("arrow_deaths"),
                    "§7Wins: §f" + results.getInt("wins"),
                    "§7Losses: §f" + results.getInt("losses"),
                    "§7Damage Given: §f" + results.getDouble("damage_given"),
                    "§7Damage Taken: §f" + results.getInt("damage_taken"),
                    "§7Connects: §f" + results.getInt("connects")
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getSender().sendMessage("§aPlayer data for §7"+offlinePlayer.getName()+"§a:");
        getSender().sendMessage(statsString);

    }
}
