package com.k9rosie.novswar.config;

import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;

import com.k9rosie.novswar.NovsWar;

public class ConfigManager {

	NovsWar novswar;
	private ArrayList<NovsConfig> configCache;
	
	public ConfigManager(NovsWar novswar) {
		this.novswar = novswar;
        configCache = new ArrayList<>();
	}
	
	public void initialize() {
		addConfig(new CoreConfig());
		addConfig(new TeamsConfig());
		addConfig("worlds", new NovsConfig("worlds.yml"));
		addConfig("regions", new NovsConfig("regions.yml"));
		addConfig("messages", new NovsConfig("messages.yml"));
	}
	
	public FileConfiguration getConfig(String key) {
		return configCache.get(key).getConfig();
	}
	
	public void addConfig(NovsConfig config) {
		config.saveDefaultConfig();
		config.reloadConfig();
		configCache.add(config);
	}
	
    //TODO: use this method when adding a /nw reload command
	public void reloadConfigs() {
		for (NovsConfig config : configCache) {
			config.reloadConfig();
		}
	}
	
	public void saveConfigs() {
		for (NovsConfig config : configCache) {
			config.saveConfig();
		}
	}
}
