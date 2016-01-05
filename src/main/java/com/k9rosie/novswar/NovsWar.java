package com.k9rosie.novswar;

import com.k9rosie.novswar.config.ConfigCache;

public class NovsWar {
	
	private NovsWarPlugin plugin;
	private static NovsWar instance;
	
	private ConfigCache configCache;
	
	public NovsWar(NovsWarPlugin plugin) {
		this.plugin = plugin;
		
		configCache = new ConfigCache(this);
	}
	
	public void initialize() {
		configCache.initialize();
	}
	
	public static NovsWar getInstance() {
		return instance;
	}
	
	public NovsWarPlugin getPlugin() {
		return plugin;
	}
	
}
