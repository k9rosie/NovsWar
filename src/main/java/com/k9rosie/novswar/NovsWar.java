package com.k9rosie.novswar;

import com.k9rosie.novswar.cache.NovsPlayerCache;
import com.k9rosie.novswar.cache.NovsTeamCache;
import com.k9rosie.novswar.command.CommandHandler;
import com.k9rosie.novswar.cache.NovsConfigCache;
import com.k9rosie.novswar.database.DatabaseThread;
import com.k9rosie.novswar.database.NovswarDB;
import com.k9rosie.novswar.event.NovsWarInitializationEvent;
import com.k9rosie.novswar.game.GameHandler;
import com.k9rosie.novswar.gamemode.Gamemode;
import com.k9rosie.novswar.cache.NovsWorldCache;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class NovsWar {

	private NovsWarPlugin plugin;
	private static NovsWar instance;
	
	private NovsConfigCache novsConfigCache;
	private NovsTeamCache novsTeamCache;
	private NovsPlayerCache novsPlayerCache;
	private NovsWorldCache novsWorldCache;
	private DatabaseThread databaseThread;
	private CommandHandler commandHandler;
	private GameHandler gameHandler;

	public HashMap<String, Gamemode> gamemodes;

	public NovsWar(NovsWarPlugin plugin) {
		this.plugin = plugin;
		instance = this;
		
		novsConfigCache = new NovsConfigCache(this);
		novsTeamCache = new NovsTeamCache(this);
		novsPlayerCache = new NovsPlayerCache(this);
		novsWorldCache = new NovsWorldCache(this);
		databaseThread = new DatabaseThread(this);
		commandHandler = new CommandHandler(this);
		gameHandler = new GameHandler(this);

		gamemodes = new HashMap<String, Gamemode>();
	}
	
	public void initialize() {
		NovsWarInitializationEvent event = new NovsWarInitializationEvent(this);
		Bukkit.getPluginManager().callEvent(event);

		novsConfigCache.initialize();
		databaseThread.getThread().start();
		novsTeamCache.initialize();
		novsWorldCache.initialize();
		gameHandler.initialize();
	}
	
	public static NovsWar getInstance() {
		return instance;
	}
	
	public NovsWarPlugin getPlugin() {
		return plugin;
	}

	public NovsConfigCache getNovsConfigCache() {
		return novsConfigCache;
	}
	
	public NovsTeamCache getNovsTeamCache() {
		return novsTeamCache;
	}

	public NovsWorldCache getNovsWorldCache() {
		return novsWorldCache;
	}

	public NovsPlayerCache getNovsPlayerCache() {
		return novsPlayerCache;
	}
    
	public DatabaseThread getDatabaseThread() {
		return databaseThread;
	}

	public NovswarDB getDatabase() {
		return databaseThread.getDatabase();
	}

	public CommandHandler getCommandHandler() {
		return commandHandler;
	}

	public GameHandler getGameHandler() {
		return gameHandler;
	}

	public HashMap<String, Gamemode> getGamemodes() {
		return gamemodes;
	}

	public static void info(String message) {
		Bukkit.getLogger().info(message);
	}

	public static void log(String message) {
		Bukkit.getLogger().log(Level.INFO, message);
	}

	public static void log(Level level, String message) {
		Bukkit.getLogger().log(level, message);
	}

	public static boolean isOnline(String displayName) {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (player.getDisplayName().equalsIgnoreCase(displayName)) {
				return true;
			}
		}
		return false;
	}

	public static UUID getUUID(String displayName) {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (displayName.equalsIgnoreCase(player.getDisplayName())) {
				return player.getUniqueId();
			}
		}
		for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
			if (displayName.equalsIgnoreCase(player.getName())) {
				return player.getUniqueId();
			}
		}
		return null;
	}
}
