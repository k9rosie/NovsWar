package com.k9rosie.novswar;

import com.k9rosie.novswar.command.CommandHandler;
import com.k9rosie.novswar.config.ConfigurationCache;
import com.k9rosie.novswar.database.DatabaseThread;
import com.k9rosie.novswar.database.NovswarDB;
import com.k9rosie.novswar.game.GameHandler;
import com.k9rosie.novswar.gamemode.GamemodeHandler;
import com.k9rosie.novswar.manager.PlayerManager;
import com.k9rosie.novswar.manager.TeamManager;
import com.k9rosie.novswar.manager.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.logging.Level;

public class NovsWar {
	
	private NovsWarPlugin plugin;
	private static NovsWar instance;
	
	private ConfigurationCache configurationCache;
	private TeamManager teamManager;
	private PlayerManager playerManager;
	private WorldManager worldManager;
	private DatabaseThread databaseThread;
	private GamemodeHandler gamemodeHandler;
	private CommandHandler commandHandler;
	private GameHandler gameHandler;

	public NovsWar(NovsWarPlugin plugin) {
		this.plugin = plugin;
		instance = this;
		
		configurationCache = new ConfigurationCache(this);
		teamManager = new TeamManager(this);
		playerManager = new PlayerManager(this);
		worldManager = new WorldManager(this);
		databaseThread = new DatabaseThread(this);
		gamemodeHandler = new GamemodeHandler(this);
		commandHandler = new CommandHandler(this);
		gameHandler = new GameHandler(this);
	}
	
	public void initialize() {
		configurationCache.initialize();

		databaseThread.getThread().start();

		teamManager.initialize();
		worldManager.initialize();
		gamemodeHandler.initialize();
		gameHandler.initialize();
	}
	
	public static NovsWar getInstance() {
		return instance;
	}
	
	public NovsWarPlugin getPlugin() {
		return plugin;
	}

	public ConfigurationCache getConfigurationCache() {
		return configurationCache;
	}
	
	public TeamManager getTeamManager() {
		return teamManager;
	}

	public WorldManager getWorldManager() {
		return worldManager;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}
    
	public DatabaseThread getDatabaseThread() {
		return databaseThread;
	}

	public NovswarDB getDatabase() {
		return databaseThread.getDatabase();
	}
    
	public GamemodeHandler getGamemodeHandler() {
		return gamemodeHandler;
	}

	public CommandHandler getCommandHandler() {
		return commandHandler;
	}

	public GameHandler getGameHandler() {
		return gameHandler;
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
