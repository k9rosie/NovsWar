package com.k9rosie.novswar;

import com.k9rosie.novswar.config.ConfigurationCache;
import com.k9rosie.novswar.manager.PlayerManager;
import com.k9rosie.novswar.manager.TeamManager;
import com.k9rosie.novswar.manager.WorldManager;
import org.bukkit.configuration.file.FileConfiguration;

public class NovsWar {
	
	private NovsWarPlugin plugin;
	private static NovsWar instance;
	
	private ConfigurationCache configurationCache;
	private TeamManager teamManager;
	private PlayerManager playerManager;
	private WorldManager worldManager;

	private boolean lobbyEnabled;

	public NovsWar(NovsWarPlugin plugin) {
		this.plugin = plugin;
		
		configurationCache = new ConfigurationCache(this);
		teamManager = new TeamManager(this);
		playerManager = new PlayerManager(this);
		worldManager = new WorldManager(this);

		lobbyEnabled = true;
	}
	
	public void initialize() {
		configurationCache.initialize();
		lobbyEnabled = configurationCache.getConfig("core").getBoolean("core.lobby.enabled");

		teamManager.initialize();
		worldManager.initialize();
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

	public boolean isLobbyEnabled() {
		return lobbyEnabled;
	}
}
