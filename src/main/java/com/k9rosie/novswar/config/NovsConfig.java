package com.k9rosie.novswar.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.k9rosie.novswar.NovsWar;
import com.k9rosie.novswar.NovsWarPlugin;

public abstract class NovsConfig {
	private File file;
	private String fileName;
	private FileConfiguration config;
	
	private NovsWarPlugin plugin;
	
	public NovsConfig(NovsWarPlugin plugin, String fileName) {
		this.fileName = fileName;
		this.plugin = plugin;
	}
	
	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(file);
		
		// look for defaults in jar
		try {
			Reader defaultConfigStream = new InputStreamReader(plugin.getResource(fileName), "UTF8");
			
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
			reloadData();
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
	        file = new File(plugin.getDataFolder(), fileName);
	    }
	    
	    if (!file.exists()) {
	    	plugin.saveResource(fileName, false);
	    }
	}

    public abstract void reloadData();
}
