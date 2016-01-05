package com.k9rosie.novswar;

import org.bukkit.plugin.java.JavaPlugin;

public class NovsWarPlugin extends JavaPlugin {
	
	private NovsWar novswar;
	
	public void onEnable() {
		novswar = new NovsWar(this);
		novswar.initialize();
				
	}
	
	public void onDisable() {
		
	}
	
	public NovsWar getNovswarInstance() {
		return novswar;
	}
}