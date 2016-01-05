package com.k9rosie.novswar.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.NovsWarPlugin;

public class NovsConfig {
	private File file;
	private String name;
	private FileConfiguration config;
	
	private NovsWarPlugin plugin;
	
	public NovsConfig(String name) {
		this.name = name;
		plugin = NovsWar.getInstance().getPlugin();
	}
	
	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(file);
		
		// look for defaults in jar
		try {
			Reader defaultConfigStream = new InputStreamReader(plugin.getResource(name), "UTF8");
			
			if (defaultConfigStream != null) {
				YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultConfigStream);
				config.setDefaults(defaultConfig);
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public FileConfiguration getConfig() {
		if (config == null) {
			reloadConfig();
		}
		
		return config;
	}
	
	public void saveConfig() {
		if (config == null || file == null) {
	        return;
	    }
	    try {
	        config.save(file);
	    } catch (IOException exception) {
	    	exception.printStackTrace();
	    }
	}
	
	public void saveDefaultConfig() {
	    if (file == null) {
	        file = new File(plugin.getDataFolder(), name);
	    }
	    
	    if (!file.exists()) {
	    	plugin.saveResource(name, false);
	    }
	}
}
