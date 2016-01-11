package com.k9rosie.novswar;

import com.k9rosie.novswar.config.ConfigurationCache;

public class NovsWar {
	
	private NovsWarPlugin plugin;
	private static NovsWar instance;
	
	private ConfigurationCache configurationCache;
	
	public NovsWar(NovsWarPlugin plugin) {
		this.plugin = plugin;
		
		configurationCache = new ConfigurationCache(this);
	}
	
	public void initialize() {
		configurationCache.initialize();
	}
	
	public static NovsWar getInstance() {
		return instance;
	}
	
	public NovsWarPlugin getPlugin() {
		return plugin;
	}
	
}
