package com.k9rosie.novswar.config;

import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;

import com.k9rosie.novswar.NovsWar;

public class ConfigurationCache {
	
	private NovsWar novswar;
	private HashMap<String, NovsConfig> configCache;
	
	public ConfigurationCache(NovsWar novswar) {
		this.novswar = novswar;
	}
	
	public void initialize() {
		addConfig("core", new NovsConfig("core.yml"));
	}
	
	public FileConfiguration getConfig(String key) {
		return configCache.get(key).getConfig();
	}
	
	public void addConfig(String key, NovsConfig config) {
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
