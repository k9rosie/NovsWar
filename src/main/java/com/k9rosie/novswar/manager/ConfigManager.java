package com.k9rosie.novswar.manager;

import java.util.HashMap;

import com.k9rosie.novswar.model.NovsConfig;
import org.bukkit.configuration.file.FileConfiguration;

import com.k9rosie.novswar.NovsWar;

public class ConfigManager {
	
	private NovsWar novswar;
	private HashMap<String, NovsConfig> configCache;
	
	public ConfigManager(NovsWar novswar) {
		this.novswar = novswar;
        configCache = new HashMap<String, NovsConfig>();
	}
	
	public void initialize() {
		addConfig("core", new NovsConfig("core.yml"));
		addConfig("teams", new NovsConfig("teams.yml"));
		addConfig("worlds", new NovsConfig("worlds.yml"));
		addConfig("regions", new NovsConfig("regions.yml"));
		addConfig("messages", new NovsConfig("messages.yml"));
	}
	
	public FileConfiguration getConfig(String key) {
		return configCache.get(key).getConfig();
	}
	
	public void addConfig(String key, NovsConfig config) {
		config.saveDefaultConfig();
		config.reloadConfig();
		configCache.put(key, config);
	}
	
	public HashMap<String, NovsConfig> getConfigCache() {
		return configCache;
	}
	
	public void reloadConfigs() {
		for (NovsConfig config : configCache.values()) {
			config.reloadConfig();
		}
	}
	
	public void saveConfigs() {
		for (NovsConfig config : configCache.values()) {
			config.saveConfig();
		}
	}
}
