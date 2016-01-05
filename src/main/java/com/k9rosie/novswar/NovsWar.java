package com.k9rosie.novswar;

public class NovsWar {
	
	private NovsWarPlugin plugin;
	private static NovsWar instance;
	
	public NovsWar(NovsWarPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void initialize() {
		
	}
	
	public static NovsWar getInstance() {
		return instance;
	}
	
	public NovsWarPlugin getPlugin() {
		return plugin;
	}
	
}
