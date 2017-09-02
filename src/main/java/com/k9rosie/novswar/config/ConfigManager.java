package com.k9rosie.novswar.config;

import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;

import com.k9rosie.novswar.NovsWar;

public class ConfigManager {

	NovsWar novswar;
	private CoreConfig coreConfig;
	private TeamsConfig teamConfig;
	private WorldsConfig worldsConfig;
	private RegionsConfig regionsConfig;
	private MessagesConfig messagesConfig;
	
	public ConfigManager(NovsWar novswar) {
		this.novswar = novswar;
		coreConfig = new CoreConfig(novswar.getPlugin());
		teamConfig = new TeamsConfig(novswar.getPlugin());
		worldsConfig = new WorldsConfig(novswar.getPlugin());
		regionsConfig = new RegionsConfig(novswar.getPlugin());
		messagesConfig = new MessagesConfig(novswar.getPlugin());
	}
	
	public void reloadConfigs() {
        coreConfig.reloadConfig();
        coreConfig.reloadData();
        teamConfig.reloadConfig();
        teamConfig.reloadData();
        worldsConfig.reloadConfig();
        worldsConfig.reloadData();
        regionsConfig.reloadConfig();
        regionsConfig.reloadData();
        messagesConfig.reloadConfig();
        messagesConfig.reloadData();
	}
	
	public void saveConfigs() {
        coreConfig.saveConfig();
        teamConfig.saveConfig();
        worldsConfig.saveConfig();
        regionsConfig.saveConfig();
        messagesConfig.saveConfig();
	}

	public void saveDefaultConfigs() {
	    coreConfig.saveDefaultConfig();
	    teamConfig.saveDefaultConfig();
	    worldsConfig.saveDefaultConfig();
	    regionsConfig.saveDefaultConfig();
	    messagesConfig.saveDefaultConfig();
    }

    public CoreConfig getCoreConfig() {
        return coreConfig;
    }

    public TeamsConfig getTeamConfig() {
        return teamConfig;
    }

    public WorldsConfig getWorldsConfig() {
        return worldsConfig;
    }

    public RegionsConfig getRegionsConfig() {
        return regionsConfig;
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }
}
