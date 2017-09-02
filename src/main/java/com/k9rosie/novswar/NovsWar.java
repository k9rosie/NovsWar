package com.k9rosie.novswar;

import com.k9rosie.novswar.config.*;
import com.k9rosie.novswar.player.PlayerManager;
import com.k9rosie.novswar.team.TeamManager;
import com.k9rosie.novswar.command.CommandHandler;
import com.k9rosie.novswar.database.DatabaseThread;
import com.k9rosie.novswar.database.NovswarDB;
import com.k9rosie.novswar.event.NovsWarInitializationEvent;
import com.k9rosie.novswar.game.GameHandler;
import com.k9rosie.novswar.gamemode.Gamemode;
import com.k9rosie.novswar.world.WorldManager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class NovsWar {

	private NovsWarPlugin plugin;
	private static NovsWar instance;
	
	private ConfigManager configManager;
	private TeamManager teamManager;
	private PlayerManager playerManager;
	private WorldManager worldManager;
	private DatabaseThread databaseThread;
	private CommandHandler commandHandler;
	private GameHandler gameHandler;

	public HashMap<String, Gamemode> gamemodes;

	private static boolean DEBUG;

	public NovsWar(NovsWarPlugin plugin) {
		this.plugin = plugin;
		instance = this;
		
		configManager = new ConfigManager(this);
		teamManager = new TeamManager(this);
		playerManager = new PlayerManager(this);
		worldManager = new WorldManager(this);
		databaseThread = new DatabaseThread(this);
		commandHandler = new CommandHandler(this);
		gameHandler = new GameHandler(this);

		gamemodes = new HashMap<>();

        DEBUG = false;
	}
	
	public void initialize() {
		NovsWarInitializationEvent event = new NovsWarInitializationEvent(this);
		Bukkit.getPluginManager().callEvent(event);

		configManager.saveDefaultConfigs();
		configManager.reloadConfigs();
		databaseThread.getThread().start();
		teamManager.initialize();

		worldManager.loadWorlds();

		gameHandler.initialize();
		commandHandler.initialize();

        DEBUG = configManager.getCoreConfig().getDebug();
	}
	
	public static NovsWar getInstance() {
		return instance;
	}
	
	public NovsWarPlugin getPlugin() {
		return plugin;
	}

	public ConfigManager getConfigManager() {
		return configManager;
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

	public CommandHandler getCommandHandler() {
		return commandHandler;
	}

	public GameHandler getGameHandler() {
		return gameHandler;
	}

	public HashMap<String, Gamemode> getGamemodes() {
		return gamemodes;
	}

	public CoreConfig getCoreConfig() {
		return configManager.getCoreConfig();
	}

	public TeamsConfig getTeamConfig() {
		return configManager.getTeamConfig();
	}

	public MessagesConfig getMessagesConfig() {
		return configManager.getMessagesConfig();
	}

	public RegionsConfig getRegionsConfig() {
		return configManager.getRegionsConfig();
	}

	public WorldsConfig getWorldsConfig() {
		return configManager.getWorldsConfig();
	}


	public static void info(String message) {
		Bukkit.getLogger().info(message);
	}

	public static void log(String message) {
		Bukkit.getLogger().log(Level.INFO, message);
	}

	public static void error(String message) { Bukkit.getLogger().log(Level.SEVERE, message); }

	public static void log(Level level, String message) {
		Bukkit.getLogger().log(level, message);
	}

	public static void debug(String message) {
	    if (DEBUG) {
            Bukkit.getLogger().info(message);
        }
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
